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

bool playingBluetooth = false;
unsigned long buttonPressTime = 0;  // Variable to store button press duration
bool isButtonPressed = false;       // Flag to track button press status

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
void connectionTask(void *pvParameters) {
  // Kiểm tra nếu có dữ liệu từ client
  while (ESP_BT.hasClient()) {
    if (ESP_BT.available()) {
      String request = ESP_BT.readString();  // Đọc dữ liệu từ client
      Serial.print("Received request: ");
      Serial.println(request);  // In ra dữ liệu nhận được để kiểm tra
      if (request == "TestMessage") {
        ESP_BT.println("Message received");
      } else if (request == "GetAlcohol") {
        detectAlcohol();
        ESP_BT.println(MaxAlcohol);
      }
      // Xử lý request tùy ý (bạn có thể thêm xử lý riêng cho request ở đây)

      // Ví dụ: Phản hồi lại client
    }

    vTaskDelay(100 / portTICK_PERIOD_MS);  // Độ trễ nhỏ để tránh vòng lặp chiếm CPU
  }
  if (connectionTaskHandle != NULL) {
    vTaskDelete(connectionTaskHandle);
    connectionTaskHandle = NULL;
  }
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
  bool isConnected = false;

  // Wait for up to 10 seconds for a connection
  while (millis() - startTime < 30000) {  // 20 seconds
    if (ESP_BT.hasClient()) {             // Check if a Bluetooth client has connected
      isConnected = true;
      Serial.println("Bluetooth client connected!");
      lcd.setCursor(0, 1);
      lcd.print("Connected!    ");
      vTaskDelay(1000 / portTICK_PERIOD_MS);
      break;  // Exit loop if connected
    }
    vTaskDelay(100 / portTICK_PERIOD_MS);  // Check every 100 ms
  }
  if (isConnected) {
    xTaskCreate(connectionTask, "connectionTask", 4096, NULL, 5, &connectionTaskHandle);
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


  if (!playingBluetooth && pressingTime <= 2000 && pressingTime > 100) {  // Kiểm tra nếu nút nhấn được bấm
    detectAlcohol();
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
  MaxAlcohol = 0;
  postDelay(5000, 1000, showAlcoholDetail);
  digitalWrite(LED_BLUE, LOW);
  digitalWrite(LED_RED, LOW);
  resetLCD();
}
void postDelay(int time, int delayTime, void (*func)()) {
  unsigned long lastTime = millis();
  unsigned long passingTime = 0;
  while (passingTime <= time) {
    unsigned long currentTime = millis();

    if ((passingTime == 0 || passingTime > delayTime) && func != NULL) {
      Serial.print("\n");
      Serial.print(passingTime);
      Serial.print("\n");
      func();
      if (passingTime > delayTime)
        delayTime += delayTime;
    }



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
  float mgL = getAlcohol();
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
    // tone(BUZZER_PIN, 1000);
  } else {
    // Serial.print(mq3_value);
    Serial.print("\n");
    Serial.print(mgL, 2);
    lcd.setCursor(0, 1);
    lcd.print(mgL, 2);
    digitalWrite(LED_BLUE, LOW);
    digitalWrite(LED_RED, HIGH);
    // tone(BUZZER_PIN, 1000);
  }
  Serial.print("\n\n");
  // delay(1000);
}
