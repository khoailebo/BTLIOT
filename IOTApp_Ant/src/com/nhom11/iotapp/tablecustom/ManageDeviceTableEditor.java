/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.tablecustom;

import com.nhom11.iotapp.callback.Invokelater;
import com.nhom11.iotapp.components.PanelActionManageDevice;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 *
 * @author DELL
 */
public class ManageDeviceTableEditor extends DefaultCellEditor{
    Invokelater callback;
    public ManageDeviceTableEditor(Invokelater callback){
        super(new JCheckBox());
        this.callback = callback;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        PanelActionManageDevice panel = new PanelActionManageDevice();
        panel.initEvent(callback,row);
        panel.setBackground(table.getSelectionBackground());
        return panel;
        
    }
    
}
