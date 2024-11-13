/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.swing;

import com.nhom11.iotapp.components.MenuItem;
import com.nhom11.iotapp.event.PublicEvent;
import com.nhom11.iotapp.form.DeviceDetailForm;
import com.nhom11.iotapp.form.DeviceSelectionForm;
import com.nhom11.iotapp.model.ModelMenu;
import com.nhom11.iotapp.enums.MenuType;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import java.awt.event.MouseMotionAdapter;

/**
 *
 * @author HP
 */
public class MenuList<E extends Object> extends JList<E> {

    private final DefaultListModel model;
    private int selectedIndex = -1;
    private int overedIndex = -1;

    
    private Component getForm(ModelMenu data){
        Component comp;
        switch (data.getMenuName()) {
            case "Devices":
                comp = new DeviceSelectionForm();
                break;
            default:
                comp = null;
        }
        return comp;
    }
    public MenuList() {
        model = new DefaultListModel();
        setModel(model);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    int index = locationToIndex(e.getPoint());
                    ModelMenu data = (ModelMenu) model.getElementAt(index);
                    if (data.getType() == MenuType.MENU_ITEM) {
                        selectedIndex = index;
                        PublicEvent.getInstance().getEventMenuForm().changeForm(getForm(data));
                        repaint();
                    }
                    
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                overedIndex = -1;
                repaint();
            }

        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index = locationToIndex(e.getPoint());
                if (model.getElementAt(index) instanceof ModelMenu) {
                    ModelMenu data = (ModelMenu) model.getElementAt(index);
                    if (data.getType() == MenuType.MENU_ITEM) {
                        overedIndex = index;
                        repaint();
                    } else {
                        overedIndex = -1;
                        repaint();
                    }
                }
            }

        });

    }

    @Override
    public ListCellRenderer<? super E> getCellRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                ModelMenu data;
                if (value instanceof ModelMenu) {
                    data = (ModelMenu) value;
                } else {
                    data = new ModelMenu("Devices", "icon_device", MenuType.MENU_ITEM);
                }
                MenuItem item = new MenuItem(data);
                item.setSelected(selectedIndex == index);
                item.setOvered(overedIndex == index);
                return item;

// Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
            }

        };
    }

    public void addItem(ModelMenu data) {
        model.addElement(data);
    }
}
