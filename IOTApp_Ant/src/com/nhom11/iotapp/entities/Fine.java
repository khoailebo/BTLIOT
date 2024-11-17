/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.entities;

/**
 *
 * @author DELL
 */
public class Fine extends  JsonFormat{
    String device_id;
    String subject_name;
    String subject_id;
    int subject_age;
    float alcohol_level;
    String location;

    public Fine(String device_id, String subject_name, String subject_id, int subject_age, float alcohol_level, String location) {
        this.device_id = device_id;
        this.subject_name = subject_name;
        this.subject_id = subject_id;
        this.subject_age = subject_age;
        this.alcohol_level = alcohol_level;
        this.location = location;
    }
    
    
}
