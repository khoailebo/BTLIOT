/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.entities;

/**
 *
 * @author DELL
 */
public class ModelDevice extends JsonFormat{
    String device_id;

    public ModelDevice(String device_id, String name, String model) {
        this.device_id = device_id;
        this.name = name;
        this.model = model;
    }
    String name;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    String model;
}
