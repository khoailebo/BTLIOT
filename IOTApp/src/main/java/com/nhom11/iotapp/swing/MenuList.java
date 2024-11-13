/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.swing;

import com.nhom11.iotapp.components.MenuItem;
import com.nhom11.iotapp.model.ModelMenu;
import com.nhom11.iotapp.model.enums.MenuType;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author DELL
 */
public class MenuList<E extends Object> extends JList<E>{
    private final DefaultListModel model;
    public MenuList(){
        model = new DefaultListModel();
        setModel(model);
    }

    @Override
    public ListCellRenderer<? super E> getCellRenderer() {
        return new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
                ModelMenu data;
                System.out.println(model.size());
                if(value instanceof ModelMenu){
                    data = (ModelMenu)value;
//                    System.out.println("Yes");
                }
                else {
//                    System.out.println("No");
                    data = new ModelMenu("Devices", "icon_device", MenuType.MENU_ITEM);
                }
                MenuItem com = new MenuItem(data);
                return com;
            }
            
        };
    }
    
    public void addItem(ModelMenu data){
        model.addElement(data);
        
    }
}
