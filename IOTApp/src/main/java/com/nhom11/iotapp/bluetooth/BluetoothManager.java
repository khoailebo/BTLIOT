/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.bluetooth;

import com.nhom11.iotapp.callback.Invokelater;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author DELL
 */
public class BluetoothManager {

    ArrayList<RemoteDevice> foundDevices;
    private VirtualDevice virtualDevice;
    boolean Connecting;

    public boolean isConnecting() {
        return Connecting;
    }

    public void setConnecting(boolean Connecting) {
        this.Connecting = Connecting;
    }

    public void setVirtualDevice(VirtualDevice virtualDevice) {
        this.virtualDevice = virtualDevice;
    }
    boolean Connected;

    public boolean isConnected() {
        return Connected;
    }

    public void setConnected(boolean Connected) {
        this.Connected = Connected;
    }

    public VirtualDevice getVirtualDevice() {
        return virtualDevice;
    }
    static final Object SyncObj = new Object();

    public ArrayList getDevicesList() throws BluetoothStateException {
        foundDevices = new ArrayList<>();
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        DiscoveryAgent discoveryAgent = localDevice.getDiscoveryAgent();
        System.out.println("Looking for devices....");
        setConnecting(true);
        discoveryAgent.startInquiry(DiscoveryAgent.GIAC, new DiscoveryListener() {
            @Override
            public void deviceDiscovered(RemoteDevice btDevice, DeviceClass dc) {
//                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                foundDevices.add(btDevice);
                try {
                    System.out.println("Found device: " + btDevice.getFriendlyName(false) + " (" + btDevice.getBluetoothAddress() + ")");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void servicesDiscovered(int i, ServiceRecord[] srs) {
//                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void serviceSearchCompleted(int i, int i1) {
//                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

            @Override
            public void inquiryCompleted(int i) {
//                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                synchronized (BluetoothManager.SyncObj) {
                    BluetoothManager.SyncObj.notifyAll();
                }
            }
        });
        synchronized (SyncObj) {
            try {
                SyncObj.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(BluetoothManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        setConnecting(false);
        return foundDevices;
    }

    public boolean connectToDevice(int index) {

        return true;
    }

    private BluetoothManager() {
    }
    public static BluetoothManager instanceBluetoothManager;

    public static BluetoothManager getInstance() {
        if (instanceBluetoothManager == null) {
            instanceBluetoothManager = new BluetoothManager();
            VirtualDevice.initEvent();
        }
        return instanceBluetoothManager;
    }

    public void connectToDevice(int deviceIndex, Invokelater callback) {
        new Thread(
                new Runnable() {
            @Override
            public void run() {
//                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                try {
                    RemoteDevice device = foundDevices.get(deviceIndex);
                    String url = "btspp://" + device.getBluetoothAddress() + ":1;authenticate=false;encrypt=false;master=false";
                    StreamConnection connection = (StreamConnection) Connector.open(url);
                    Connected = true;
                    System.out.println("Connected to: " + device.getFriendlyName(false));
                    virtualDevice = new VirtualDevice(device.getBluetoothAddress(),device.getFriendlyName(false),connection);
                    virtualDevice.startCommunicate();
                    
                } catch (IOException ex) {
                    Connected = false;
                    Logger.getLogger(BluetoothManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                finally{
                    callback.call(isConnected());
                }
            }
        }
        ).start();
    }
}
