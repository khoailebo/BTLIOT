/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.tablecustom;

import com.nhom11.iotapp.components.PanelActionManageDevice;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author DELL
 */
public class ManageDeviceTableRenderer extends DefaultTableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component com = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        PanelActionManageDevice panel = new PanelActionManageDevice();
        if(!isSelected && row % 2 == 0){
            panel.setBackground(Color.white);
        }
        else {
            panel.setBackground(com.getBackground());
        }
        return panel;
    }
    
}
