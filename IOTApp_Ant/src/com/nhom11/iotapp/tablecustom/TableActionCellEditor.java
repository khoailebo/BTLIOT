/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.tablecustom;

import com.nhom11.iotapp.components.PanelAction;
import com.nhom11.iotapp.event.TableSelectedEvent;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 *
 * @author Administrator
 */
public class TableActionCellEditor extends DefaultCellEditor{
    TableSelectedEvent event;
    public TableActionCellEditor(TableSelectedEvent event){
        super(new JCheckBox());
        this.event = event;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        PanelAction action = new PanelAction();
        action.setBackground(table.getSelectionBackground());
        action.initEvent(event, row);
        return action;
    }
    
}
