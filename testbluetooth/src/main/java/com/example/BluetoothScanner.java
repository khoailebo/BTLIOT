package com.example;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BluetoothScanner {

    static List<RemoteDevice> foundDevices = new ArrayList<>();

    public static void main(String[] args) {
        try {
            // Thiết lập Bluetooth LocalDevice và DiscoveryAgent
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent discoveryAgent = localDevice.getDiscoveryAgent();

            // Bắt đầu quét các thiết bị Bluetooth xung quanh
            System.out.println("Đang quét các thiết bị Bluetooth khả dụng...");
            discoveryAgent.startInquiry(DiscoveryAgent.GIAC, new DeviceDiscoveryListener());

            // Đợi đến khi việc quét hoàn tất
            synchronized(DeviceDiscoveryListener.inquiryCompletedEvent) {
                DeviceDiscoveryListener.inquiryCompletedEvent.wait();
            }

            // Hiển thị danh sách các thiết bị tìm thấy
            if (foundDevices.isEmpty()) {
                System.out.println("Không tìm thấy thiết bị Bluetooth nào.");
                return;
            }

            System.out.println("Danh sách các thiết bị tìm thấy:");
            for (int i = 0; i < foundDevices.size(); i++) {
                RemoteDevice device = foundDevices.get(i);
                System.out.println((i + 1) + ". " + device.getFriendlyName(false) + " (" + device.getBluetoothAddress() + ")");
            }

            // Chọn thiết bị để kết nối
            Scanner scanner = new Scanner(System.in);
            System.out.print("Chọn số thứ tự của thiết bị để kết nối: ");
            int choice = scanner.nextInt();
            RemoteDevice selectedDevice = foundDevices.get(choice - 1);

            // Kết nối tới thiết bị đã chọn
            connectToDevice(selectedDevice);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm để kết nối tới thiết bị Bluetooth
    private static void connectToDevice(RemoteDevice device) {
        try {
            String url = "btspp://" + device.getBluetoothAddress() + ":1;authenticate=false;encrypt=false;master=false";
            StreamConnection connection = (StreamConnection) Connector.open(url);
            System.out.println("Đã kết nối với " + device.getFriendlyName(false));

            // Mở InputStream và OutputStream
            InputStream inputStream = connection.openInputStream();
            OutputStream outputStream = connection.openOutputStream();

            // Gửi lệnh để bật LED trên ESP32
            outputStream.write("GetAlcohol".getBytes());
            outputStream.flush();
            System.out.println("Đã gửi dữ liệu tới thiết bị.");

            // Đọc dữ liệu từ thiết bị
            int data = inputStream.read();
            while (data != -1) {
                System.out.print((char) data);
                data = inputStream.read();
            }

            // Đóng kết nối
            inputStream.close();
            outputStream.close();
            connection.close();
            System.out.println("Đã đóng kết nối.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Listener để phát hiện thiết bị Bluetooth
    private static class DeviceDiscoveryListener implements DiscoveryListener {
        public static final Object inquiryCompletedEvent = new Object();

        public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
            foundDevices.add(btDevice);
            try {
                System.out.println("Tìm thấy thiết bị: " + btDevice.getFriendlyName(false) + " (" + btDevice.getBluetoothAddress() + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void inquiryCompleted(int discType) {
            System.out.println("Quét thiết bị hoàn tất.");
            synchronized(inquiryCompletedEvent) {
                inquiryCompletedEvent.notifyAll();
            }
        }

        public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {}
        public void serviceSearchCompleted(int transID, int respCode) {}
    }
}
