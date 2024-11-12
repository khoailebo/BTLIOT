/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.swing;

import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author DELL
 */
public class MenuList<E extends Object> extends JList<E>{
    DefaultListModel model;
    public MenuList(){
        model = new DefaultListModel();
        setModel(model);
    }
}
