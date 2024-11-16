/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.entities;

import com.nhom11.iotapp.enums.UserRole;

/**
 *
 * @author DELL
 */
public class ModelSignUp extends JsonFormat{
    String username;

    public ModelSignUp(String username, String password, String email, String full_name, UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.full_name = full_name;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
    String password;
    String email;
    String full_name;
    UserRole role;
}
