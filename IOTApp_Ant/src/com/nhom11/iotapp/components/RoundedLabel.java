/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.components;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author DELL
 */
public class RoundedLabel extends JLabel{
    public RoundedLabel(){
        setOpaque(false);
        setBorder(new EmptyBorder(5,10,5,10));
    }
    
}
