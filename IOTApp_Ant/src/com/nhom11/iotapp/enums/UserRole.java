/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.nhom11.iotapp.enums;

/**
 *
 * @author DELL
 */
public enum UserRole {
    officer,admin;
    public static UserRole fromString(String role){
        try {
                return UserRole.valueOf(role.toLowerCase());
            } catch (IllegalArgumentException e) {
                return null; // or handle default case
            }
    }
}
