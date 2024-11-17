/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.nhom11.iotapp.enums;

/**
 *
 * @author DELL
 */
public enum AlcoholStatus {
    LOW,HIGH;
    public static AlcoholStatus fromString(String alString){
        try{
            return AlcoholStatus.valueOf(alString.toUpperCase());
        }
        catch(Exception e){
            return null;
        }
    }
}
