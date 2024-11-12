package com.example;

import javax.bluetooth.*;
import java.util.ArrayList;
import java.util.List;

public class BluetoothDeviceDiscovery {
    private static List<RemoteDevice> iotDevices = new ArrayList<>();

    public static void main(String[] args) {
        try {
            // Khởi tạo LocalDevice và DiscoveryAgent
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent discoveryAgent = localDevice.getDiscoveryAgent();
            
            System.out.println("Đang tìm kiếm thiết bị Bluetooth IoT...");

            // Bắt đầu tìm kiếm thiết bị Bluetooth
            discoveryAgent.startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener() {
                public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                    try {
                        String deviceName = btDevice.getFriendlyName(false);
                        iotDevices.add(btDevice);
                        System.out.println("Thiết bị IoT phát hiện: " + deviceName);
                        // if (deviceName != null && deviceName.contains("ESP32-IoT")) {
                        // }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void inquiryCompleted(int discType) {
                    System.out.println("Tìm kiếm hoàn tất.");
                    displayDevices();
                }

                public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
                    // Không cần thiết trong trường hợp tìm kiếm thiết bị
                }

                public void serviceSearchCompleted(int transID, int respCode) {
                    // Không cần thiết trong trường hợp tìm kiếm thiết bị
                }
            });

            // Chờ tìm kiếm hoàn tất
            Thread.sleep(12000); // Chờ 10 giây, có thể điều chỉnh

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayDevices() {
        if (iotDevices.isEmpty()) {
            System.out.println("Không tìm thấy thiết bị IoT nào.");
            return;
        }
        System.out.println("Danh sách thiết bị IoT phát hiện:");
        for (int i = 0; i < iotDevices.size(); i++) {
            try {
                System.out.println((i + 1) + ": " + iotDevices.get(i).getFriendlyName(false));
            } catch (Exception e) {
                System.out.println((i + 1) + ": " + iotDevices.get(i).getBluetoothAddress());
            }
        }
    }
}
