#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <BluetoothSerial.h>  // Include Bluetooth library

#define MQ3_PIN 13     // Pin analog đọc tín hiệu từ MQ3
#define LED_BLUE 27    // GPIO27 cho đèn LED xanh
#define LED_RED 32     // GPIO32 cho đèn LED đỏ
#define BUZZER_PIN 4   // GPIO4 cho buzzer
#define BUTTON_PIN 33  // GPIO33 cho nút nhấn

BluetoothSerial ESP_BT;  // Create BluetoothSerial object

LiquidCrystal_I2C lcd(0x27, 16, 2);  // Địa chỉ I2C của LCD, 16 cột và 2 dòng

float RL = 4.7;   // Điện trở tải RL
float R0 = 10.0;  // Giá trị hiệu chuẩn cho không khí sạch
float MaxAlcohol = 0;
String AlcoholStatus = "";
bool playingBluetooth = false;
unsigned long buttonPressTime = 0;  // Variable to store button press duration
bool isButtonPressed = false;       // Flag to track button press status
bool mesuring = false;
bool isConnected = false;

void BTLEDMode(void *pvParameters) {
  while (playingBluetooth) {
    digitalWrite(LED_RED, HIGH);
    digitalWrite(LED_BLUE, LOW);
    vTaskDelay(500 / portTICK_PERIOD_MS);
    digitalWrite(LED_RED, LOW);
    digitalWrite(LED_BLUE, HIGH);
    vTaskDelay(500 / portTICK_PERIOD_MS);
  }
  digitalWrite(LED_BLUE, LOW);
  digitalWrite(LED_RED, LOW);
  vTaskDelete(NULL);
}
TaskHandle_t connectionTaskHandle = NULL;  // Handle để quản lý task connectionTask
TaskHandle_t checkConnectionTaskHandle = NULL;
bool recievedMsg = false;
int timeStartSending = 0;
void checkConnectionTask(void *parameters) {
  bool sending = false;
  timeStartSending = 0;
  while (true) {
    if (!sending) {
      sending = true;
      recievedMsg = false;
      ESP_BT.println("DeviceCheckConnection");
      timeStartSending = millis();
    }
    if (!recievedMsg) {
      int waitingTime = millis() - timeStartSending;
      if (waitingTime > 5000) {
        // if (connectionTaskHandle != NULL) {
        //   vTaskDelete(connectionTaskHandle);
        //   connectionTaskHandle = NULL;
        // }
        break;
      }
    }
    int waitingTime = millis() - timeStartSending;
    if (waitingTime > 5000) {
      sending = false;
    }
    vTaskDelay(100 / portTICK_PERIOD_MS);  // Độ trễ nhỏ để tránh vòng lặp chiếm CPU
  }
  xTaskCreate(disconnect, "DisconnectTask", 4096, NULL, 1, NULL);
  if (checkConnectionTaskHandle != NULL) {
    vTaskDelete(checkConnectionTaskHandle);
    checkConnectionTaskHandle = NULL;
  }
}
void disconnect(void *para) {
  Serial.println("Disconnecting");
  ESP_BT.disconnect();
  vTaskDelay(100 / portTICK_PERIOD_MS);  // Allow time for Bluetooth stack to handle disconnection
  ESP_BT.end();
  vTaskDelay(100 / portTICK_PERIOD_MS);  // Allow more time before continuing

  // Clean up resources or reset state
  isConnected = false;
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Disconnect");

  Serial.println("End Connection!");
  vTaskDelay(1500 / portTICK_PERIOD_MS);  // Allow more time before continuing
  resetLCD();
  // Delete the task if it was created
  vTaskDelete(NULL);
}

void connectionTask(void *pvParameters) {
  if(ESP_BT.hasClient()){
    ESP_BT.println("Confirm Device");
  }
  // Kiểm tra nếu có dữ liệu từ client
  while (ESP_BT.hasClient()) {
    // if(!isConnected){
    //   break;
    // }
    if (ESP_BT.available()) {
      String request = ESP_BT.readString();
      // Đọc dữ liệu từ client
      Serial.print("Received request: ");
      Serial.println(request);  // In ra dữ liệu nhận được để kiểm tra

      int seperatorIndex = request.indexOf('|');

      if (seperatorIndex != -1) {
        String eventName = request.substring(0, seperatorIndex);
        String data = request.substring(seperatorIndex + 1);
        Serial.print("Event: ");
        Serial.println(eventName);
        Serial.print("Data: ");
        Serial.println(data);
        if (eventName == "TestMessage") {
          ESP_BT.println("Message received");
        } else if (eventName == "DeviceCheckConnection") {
          recievedMsg = true;
          timeStartSending = millis();
        } else if (eventName == "CheckConnection") {
          ESP_BT.println("CheckConnection");
        } else if (eventName == "GetAlcohol") {
          xTaskCreate(detectingAlcoholTask, "Detecting Alcohol", 4096, NULL, 1, NULL);
        } else if (eventName == "Disconnect") {
          ESP_BT.println("Disconnect");
          break;
        }
      }


      // Xử lý request tùy ý (bạn có thể thêm xử lý riêng cho request ở đây)

      // Ví dụ: Phản hồi lại client
    }

    vTaskDelay(100 / portTICK_PERIOD_MS);  // Độ trễ nhỏ để tránh vòng lặp chiếm CPU
  }
  xTaskCreate(disconnect, "DisconnectTask", 4096, NULL, 1, NULL);
  if (connectionTaskHandle != NULL) {
    vTaskDelete(connectionTaskHandle);
    connectionTaskHandle = NULL;
  }
}

void detectingAlcoholTask(void *pvParameters) {
  detectAlcohol();
  vTaskDelete(NULL);
}

void BTAdvertisingTask(void *pvParameters) {
  if (!ESP_BT.begin("ESP32_Bluetooth_Server")) {  // Initialize Bluetooth with device name
    Serial.println("Bluetooth initialization failed.");
    return;
  }



  playingBluetooth = true;
  xTaskCreate(BTLEDMode, "BTLEDMode", 4096, NULL, 1, NULL);
  Serial.println("Bluetooth started, waiting for connection...");

  lcd.setCursor(0, 0);
  lcd.print("Bluetooth con...");
  lcd.setCursor(0, 1);
  lcd.print("................");

  unsigned long startTime = millis();
  isConnected = false;


  // Wait for up to 10 seconds for a connection
  while (millis() - startTime < 30000) {  // 20 seconds
    if (ESP_BT.hasClient()) {             // Check if a Bluetooth client has connected
      isConnected = true;
      Serial.println("Bluetooth client connected!");
      lcd.setCursor(0, 1);
      lcd.print("Connected!    ");
      // vTaskDelay(1000 / portTICK_PERIOD_MS);
      break;  // Exit loop if connected
    }
    vTaskDelay(100 / portTICK_PERIOD_MS);  // Check every 100 ms
  }
  if (isConnected) {
    xTaskCreate(connectionTask, "connectionTask", 4096, NULL, 200, &connectionTaskHandle);
    // xTaskCreate(checkConnectionTask, "CheckConnectionTask", 4096, NULL, 50, &checkConnectionTaskHandle);
  }
  // If no connection was established, end Bluetooth
  if (!isConnected) {
    ESP_BT.end();

    Serial.println("No connection established. Bluetooth advertising stopped.");
    lcd.setCursor(0, 1);
    lcd.print("No connection");
    vTaskDelay(1000 / portTICK_PERIOD_MS);
  }
  resetLCD();
  // End this task
  playingBluetooth = false;
  vTaskDelete(NULL);
}

void setup() {
  Serial.begin(9600);

  pinMode(MQ3_PIN, INPUT);
  pinMode(LED_BLUE, OUTPUT);
  pinMode(LED_RED, OUTPUT);
  pinMode(BUZZER_PIN, OUTPUT);
  pinMode(BUTTON_PIN, INPUT_PULLDOWN);  // Nút nhấn với chế độ PULLUP

  // Khởi động LCD
  lcd.init();
  lcd.backlight();

  // Hiển thị thông báo khởi động
  resetLCD();


  digitalWrite(LED_BLUE, LOW);
  digitalWrite(LED_RED, LOW);


  delay(2000);

  // if (!ESP_BT.begin("ESP32_Bluetooth_Server")) {  // Tên thiết bị Bluetooth của ESP32
  //   Serial.println("Bluetooth initialization failed.");
  //   return;
  // }

  // Serial.println("Bluetooth started, waiting for connection...");
}

void loop() {
  if (digitalRead(BUTTON_PIN) == LOW && !playingBluetooth) {  // Kiểm tra nếu nút nhấn được bấm


    // delay(1000);
  }
  unsigned long pressingTime = 0;
  if (digitalRead(BUTTON_PIN) == HIGH) {
    buttonPressTime = millis();

    while (digitalRead(BUTTON_PIN) == HIGH) {
      unsigned long currentTime = millis();
      pressingTime = currentTime - buttonPressTime;
      // lastTime = currentTime;
      if (pressingTime >= 3000) {
        xTaskCreate(BTAdvertisingTask, "BTAdvertisingTask", 4096, NULL, 1, NULL);
        break;
      }
    }
    Serial.print((float)(pressingTime / 1000));
    Serial.print("\n");
  }


  if (!mesuring && !playingBluetooth && pressingTime <= 2000 && pressingTime > 100) {  // Kiểm tra nếu nút nhấn được bấm
    detectAlcohol();
    // xTaskCreate(detectingAlcoholTask, "Detecting Alcohol", 4096, NULL, 1, NULL);
  }
}
// void postDelay(int time, int delayTime, void (*func)()) {
//   unsigned long lastTime = millis();
//   unsigned long passingTime = 0;
//   while (passingTime <= time) {
//     unsigned long currentTime = millis();


//     Serial.print("\n");
//     Serial.print(passingTime);
//     Serial.print("\n");
//     func();
//     delay(delayTime);


//     passingTime += currentTime - lastTime;
//     lastTime = currentTime;
//   }
// }
void resetLCD() {
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Alcohol Detector");
  lcd.setCursor(0, 1);
  lcd.print("Press button...");
}
void detectAlcohol() {
  if (!mesuring) {
    mesuring = true;
    if (ESP_BT.hasClient()) {
      ESP_BT.println("StartMesuring");
    }
    MaxAlcohol = 0;
    postDelay(5000, 500, showAlcoholDetail);
    digitalWrite(LED_BLUE, LOW);
    digitalWrite(LED_RED, LOW);
    resetLCD();
    if (ESP_BT.hasClient()) {
      ESP_BT.println("GetAlcohol|{\"alcohol_level\":" + String(MaxAlcohol, 2) + ",\"status\":\"" + (MaxAlcohol > 0.2 ? "HIGH" : "LOW") + "\"}");
    }
    mesuring = false;
    noTone(BUZZER_PIN);
  }
}
void postDelay(int time, int delayTime, void (*func)()) {
  int lastTime = millis();
  int passingTime = 0;
  while (passingTime <= time) {
    int currentTime = millis();
    Serial.print("\n");
    Serial.print(passingTime);
    Serial.print("\n");
    func();
    vTaskDelay(delayTime / portTICK_PERIOD_MS);
    passingTime += currentTime - lastTime;
    lastTime = currentTime;
  }
}
float getAlcohol() {
  float mq3_value = analogRead(MQ3_PIN);           // Đọc giá trị từ cảm biến MQ3
  float sensorVoltage = mq3_value * (5.0 / 4095);  // Chuyển đổi giá trị 12 bit thành giá trị điện áp thực (0-5V)
  float RS = ((5.0 * RL) / sensorVoltage) - RL;    // Tính điện trở của cảm biến
  float ratio = RS / R0;                           // Tính tỉ lệ RS/R0
  float mgL = pow(ratio / 1.5, 1.0 / -0.5);
  return mgL;
}

void showAlcoholDetail() {
  float mgL = getAlcohol()/2;
  if (mgL > MaxAlcohol) MaxAlcohol = mgL;
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Alcohol Level:");

  // float val = (mq3_value/10) * (5.0/4095);
  // float mgL = val*0.67;

  // Hiển thị và phản ứng theo giá trị cồn
  if (mgL < 0.2) {
    // Serial.print(mq3_value);
    Serial.print("\n");
    Serial.print(mgL, 2);
    lcd.setCursor(0, 1);
    // lcd.print("No alcohol.");
    lcd.print(mgL, 2);
    digitalWrite(LED_BLUE, HIGH);
    digitalWrite(LED_RED, LOW);
    noTone(BUZZER_PIN);
  } else if (mgL >= 0.2 && mgL <= 0.4) {
    // Serial.print(mq3_value);
    Serial.print("\n");
    Serial.print(mgL, 2);
    lcd.setCursor(0, 1);
    lcd.print(mgL, 2);
    digitalWrite(LED_BLUE, LOW);
    digitalWrite(LED_RED, HIGH);
    tone(BUZZER_PIN, 1000);
  } else {
    // Serial.print(mq3_value);
    Serial.print("\n");
    Serial.print(mgL, 2);
    lcd.setCursor(0, 1);
    lcd.print(mgL, 2);
    digitalWrite(LED_BLUE, LOW);
    digitalWrite(LED_RED, HIGH);
    tone(BUZZER_PIN, 1000);
  }
  Serial.print("\n\n");
  // delay(1000);
}
