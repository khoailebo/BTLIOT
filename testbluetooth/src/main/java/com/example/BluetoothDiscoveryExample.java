package com.example;

import javax.bluetooth.*;

public class BluetoothDiscoveryExample {
    public static void main(String[] args) {
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();
            
            System.out.println("Starting Bluetooth inquiry...");
            
            agent.startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener() {
                public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
                    try {
                        System.out.println("Found device: " + btDevice.getFriendlyName(false));
                    } catch (Exception e) {
                        System.out.println("Device discovered: " + btDevice.getBluetoothAddress());
                    }
                }
                
                public void inquiryCompleted(int discType) {
                    System.out.println("Inquiry completed.");
                    // Any other required cleanup or shutdown should go here
                }
                
                public void servicesDiscovered(int transID, ServiceRecord[] servRecord) { }
                public void serviceSearchCompleted(int transID, int respCode) { }
            });

            // Ensure main thread doesn't exit prematurely
            Thread.sleep(12000);  // Adjust as needed for inquiry time

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
