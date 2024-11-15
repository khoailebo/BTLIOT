/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.components;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author DELL
 */
public class CustomTextField extends JTextField{
    public CustomTextField(){
        setOpaque(false);
        setBorder(new EmptyBorder(5,10,5,10));
//        setBackground(new Color(0,0,0,0));
    }
    
}
