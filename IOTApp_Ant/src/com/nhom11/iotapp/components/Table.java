package com.nhom11.iotapp.components;

import com.nhom11.iotapp.tablecustom.TableActionCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Table extends JTable{
    public Table(){
        setShowHorizontalLines(true);
        setShowVerticalLines(false);
        setRowHeight(40);
        setGridColor(new Color(230,230,230));
        //khong cho doi cho cac cot
        getTableHeader().setReorderingAllowed(false);
       
        //neu dung label mac dinh de ve header thi xau
        //thay doi bang label tu tao
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                        Table_Header header = new Table_Header(value + "");
                        if(column == 0)
                        {
                            header.setHorizontalAlignment(JLabel.LEADING);
                            header.setBorder(new EmptyBorder(5,20,5,0));
                        }
                        return header;
            }
        });
        
        setDefaultRenderer(Object.class,new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                // TODO Auto-generated method stub
                //do cot cuoi co cach hien thi dac biet
                // nen se chi hien thi 4 cot dau
                    Component com = (Component) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if(column != 3)
                {
//                    com.setBackground(Color.white);
                    setBorder(noFocusBorder);
                    if(hasFocus)
                    {
                        com.setForeground(new Color(13,113,182));
                    }
                    else {
                        com.setForeground(new Color(102,102,102));
                    }
                }
                    return com;
                
//                Status_Type type = (Status_Type)value;
//                Table_Status t = new Table_Status(type);
//                return t;

            }
        });
    }
    public void addRow(Object[]row)
    {
        DefaultTableModel model =(DefaultTableModel) getModel();
        model.addRow(row);
    }
}
