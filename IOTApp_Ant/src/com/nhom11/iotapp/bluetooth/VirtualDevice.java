/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.bluetooth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nhom11.iotapp.callback.Invokelater;
import com.nhom11.iotapp.enums.AlcoholStatus;
import com.nhom11.iotapp.event.PublicEvent;
import com.nhom11.iotapp.form.DeviceSelectionForm;
import com.nhom11.iotapp.form.MeasuringForm;
import com.nhom11.iotapp.form.ResultForm;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author Administrator
 */
public class VirtualDevice implements Runnable {

    static final Object SyncObject = new Object();
    String Name;
    String Id;
    boolean SystemDevice;

    public boolean isSystemDevice() {
        return SystemDevice;
    }

    public void setSystemDevice(boolean SystemDevice) {
        this.SystemDevice = SystemDevice;
    }
    BufferedReader reader;
    BufferedWriter writer;
    boolean Identify = false;

    public boolean isIdentify() {
        return Identify;
    }

    public void setIdentify(boolean Identify) {
        this.Identify = Identify;
    }
    OutputStream output;

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }
    StreamConnection connection;
    Thread RecievedThread;
    Thread CheckConnectionThread;
    HashMap<String, ArrayList<Invokelater>> callBackList;
    static HashMap<String, Invokelater> eventListenerHashMap;
    boolean Disconnect;
    static AlcoholStatus AlcoholStatus;

    public AlcoholStatus getAlcoholStatus() {
        return AlcoholStatus;
    }

    public static void initEvent() {
        if (eventListenerHashMap == null) {
            eventListenerHashMap = new HashMap<>();
            eventListenerHashMap.put("StartMesuring", new Invokelater() {
                @Override
                public void call(Object... obj) {
                    PublicEvent.getInstance().getEventMenuForm().changeForm(new MeasuringForm());
                }
            });
            eventListenerHashMap.put("GetAlcohol", new Invokelater() {
                @Override
                public void call(Object... obj) {
                    JsonObject jsonResponse = JsonParser.parseString((String) obj[0]).getAsJsonObject();
                    AlcoholValue = jsonResponse.get("alcohol_level").getAsFloat();
                    AlcoholStatus = AlcoholStatus.fromString(jsonResponse.get("status").getAsString());
                    System.out.println((String) obj[0]);
                    PublicEvent.getInstance().getEventMenuForm().changeForm(new ResultForm());
                }
            });
            eventListenerHashMap.put("DeviceCheckConnection", new Invokelater() {
                @Override
                public void call(Object... obj) {
                    try {
                        BluetoothManager.getInstance().getVirtualDevice().sendEvent("DeviceCheckConnection", null, null);
                    } catch (IOException ex) {
                        Logger.getLogger(VirtualDevice.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            eventListenerHashMap.put("Confirm Device", new Invokelater() {
                @Override
                public void call(Object... obj) {
//                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                    BluetoothManager.getInstance().getVirtualDevice().setSystemDevice(true);
                }
            }
            );
        }
    }

    public VirtualDevice(String id, String name, StreamConnection streamConnection) {
        this.Name = name;
        callBackList = new HashMap<>();

        this.Id = id;
        this.connection = streamConnection;
        try {

            output = streamConnection.openOutputStream();
            reader = new BufferedReader(new InputStreamReader(streamConnection.openInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(output));
            RecievedThread = new Thread(this, "Recieved Thread");
            RecievedThread.setPriority(Thread.MAX_PRIORITY);
            CheckConnectionThread = new Thread(new CheckConnectionRunable(), "Check Connection Thread");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void startCommunicate() {
        RecievedThread.start();
        CheckConnectionThread.start();
    }

    static float AlcoholValue = -1;

    public float getAlcoholValue() {
        return AlcoholValue;
    }

    public void setAlcoholValue(float AlcoholValue) {
        this.AlcoholValue = AlcoholValue;
    }

    public void mesure() throws IOException {
        AlcoholValue = -1;
        sendEvent("GetAlcohol", null, null);
//        return value;
    }

    public void sendEvent(String eventName, String data, Invokelater callback) throws IOException {
        if (callback != null) {
            if (callBackList.containsKey(eventName)) {
                callBackList.get(eventName).add(callback);
            } else {
                ArrayList<Invokelater> arrCallback = new ArrayList<>();
                arrCallback.add(callback);
                callBackList.put(eventName, arrCallback);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(eventName);
        sb.append("|");
        sb.append(data);
        sendMsg(sb.toString());
    }

    public void sendMsg(String msg) throws IOException {
//        writer.write(msg);
//        writer.flush();
        output.write(msg.getBytes());
        output.flush();
    }

    public void performCallBack(String msg) {
        String buffers[] = msg.split("\\|");
        String eventName = buffers[0];
        if (callBackList.containsKey(eventName)) {
            ArrayList<Invokelater> arrCallBack = callBackList.get(eventName);
            Invokelater callback = arrCallBack.getFirst();
            arrCallBack.remove(callback);
            callback.call((buffers.length > 1 ? buffers[1] : null));
            if (arrCallBack.size() == 0) {
                callBackList.remove(eventName);
            }
        }
    }

    public void performEventListener(String msg) {
        String buffers[] = msg.split("\\|");
        String eventName = buffers[0];
        if (eventListenerHashMap.containsKey(eventName)) {
            Invokelater eventListener = eventListenerHashMap.get(eventName);
            eventListener.call((buffers.length > 1 ? buffers[1] : null));
        }
    }

    @Override
    public void run() {
        try {
            String recievedMsg = "";
            while ((recievedMsg = reader.readLine()) != null) {
                System.out.println("Read: " + recievedMsg);
                performCallBack(recievedMsg);
                performEventListener(recievedMsg);
                if (Disconnect) {
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void disconnectProtocol() {
        new Thread(() -> {
            try {
                RecievedThread.interrupt();
                CheckConnectionThread.interrupt();
                writer.close();
                reader.close();
                connection.close();
                BluetoothManager.getInstance().setVirtualDevice(null);
                BluetoothManager.getInstance().setConnected(false);
                Disconnect = true;
                PublicEvent.getInstance().getEventMenuForm().changeForm(new DeviceSelectionForm());
                System.out.println("dis");
            } catch (IOException ex) {
                Logger.getLogger(VirtualDevice.class.getName()).log(Level.SEVERE, null, ex);
            }
        }, "Disconnect Thread").start();
    }

    public void disconnectProtocolWithOutComeBack() {
        new Thread(() -> {
            try {
                RecievedThread.interrupt();
                CheckConnectionThread.interrupt();
                writer.close();
                reader.close();
                connection.close();
                BluetoothManager.getInstance().setVirtualDevice(null);
                BluetoothManager.getInstance().setConnected(false);
                Disconnect = true;
                System.out.println("dis");
            } catch (IOException ex) {
                Logger.getLogger(VirtualDevice.class.getName()).log(Level.SEVERE, null, ex);
            }
        }, "Disconnect Thread").start();
    }

    public void disconnect(boolean comeback) throws IOException {
        sendEvent("Disconnect", null, new Invokelater() {
            @Override
            public void call(Object... obj) {
                if (comeback) {
                    disconnectProtocol();
                }
                else {
                    disconnectProtocolWithOutComeBack();
                }
            }
        });

    }

    class CheckConnectionRunable implements Runnable {

        long timeStartChecking = 0;
        boolean checking = false;
        boolean send = true;
        boolean revievedMsg = false;

        @Override
        public void run() {
            while (!Disconnect) {
                if (send) {
                    try {
                        System.out.println("checkCon");
                        send = false;
                        checking = true;
                        revievedMsg = false;
                        timeStartChecking = System.currentTimeMillis();
                        sendEvent("CheckConnection", null, new Invokelater() {
                            @Override
                            public void call(Object... obj) {
                                checking = false;
                                revievedMsg = true;
//                                timeStartChecking = System.currentTimeMillis();
                            }
                        });
                    } catch (IOException ex) {
                        disconnectProtocol();
                        Logger.getLogger(VirtualDevice.class.getName()).log(Level.SEVERE, null, ex);
                        break;
                    }
                }
                long currentTime = System.currentTimeMillis();
                if (checking == true) {
                    long waitingTime = currentTime - timeStartChecking;
                    if (waitingTime > 5000 && !revievedMsg) {
                        disconnectProtocol();
                        break;
                    }
                }
                if (System.currentTimeMillis() - timeStartChecking > 5000) {
                    send = true;
                }
            }
        }

    }
}
