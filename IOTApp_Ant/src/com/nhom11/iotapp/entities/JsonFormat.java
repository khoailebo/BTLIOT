/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.entities;

import com.google.gson.Gson;

/**
 *
 * @author DELL
 */
public abstract class JsonFormat {
    public String toJsonObj(){
        return new Gson().toJson(this,this.getClass());
    }
}
