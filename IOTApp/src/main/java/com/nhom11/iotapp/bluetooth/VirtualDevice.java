/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.bluetooth;

import com.nhom11.iotapp.callback.Invokelater;
import com.nhom11.iotapp.event.PublicEvent;
import com.nhom11.iotapp.form.MesuringForm;
import com.nhom11.iotapp.form.ResultForm;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
    BufferedReader reader;
    BufferedWriter writer;

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
    HashMap<String, ArrayList<Invokelater>> callBackList;
    static HashMap<String,Invokelater> eventListenerHashMap;
    boolean Disconnect;

    public static void initEvent(){
        if(eventListenerHashMap == null){
            eventListenerHashMap = new HashMap<>();
            eventListenerHashMap.put("StartMesuring", new Invokelater() {
                @Override
                public void call(Object... obj) {
                    PublicEvent.getInstance().getEventMenuForm().changeForm(new MesuringForm());
                }
            });
            eventListenerHashMap.put("GetAlcohol", new Invokelater() {
                @Override
                public void call(Object... obj) {
                    AlcoholValue = Float.parseFloat((String)obj[0]);
                    System.out.println(AlcoholValue);
                    PublicEvent.getInstance().getEventMenuForm().changeForm(new ResultForm());
                }
            });
        }
    }
    public VirtualDevice(String id, String name, StreamConnection streamConnection) {
        this.Name = name;
        callBackList = new HashMap<>();
        
        this.Id = id;
        this.connection = streamConnection;
        try {

            reader = new BufferedReader(new InputStreamReader(streamConnection.openInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(streamConnection.openOutputStream()));
            RecievedThread = new Thread(this, "Recieved Thread");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void startCommunicate() {
        RecievedThread.start();
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
        sendEvent("GetAlcohol", null,null);
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
        writer.write(msg);
        writer.flush();
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

    public void disconnect() throws IOException {
        sendEvent("Disconnect", null, new Invokelater() {
            @Override
            public void call(Object... obj) {
                try {
                    writer.close();
                    reader.close();
                    connection.close();
                    BluetoothManager.getInstance().setVirtualDevice(null);
                    BluetoothManager.getInstance().setConnected(false);
                    Disconnect = true;
                    synchronized (SyncObject) {
                        SyncObject.notifyAll();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(VirtualDevice.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        synchronized (SyncObject) {
            try {
                SyncObject.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(VirtualDevice.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
