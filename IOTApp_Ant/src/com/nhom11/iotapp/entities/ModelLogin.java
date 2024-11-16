/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.entities;

/**
 *
 * @author DELL
 */
public class ModelLogin extends JsonFormat{
    String username;
    String password;

    public ModelLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
