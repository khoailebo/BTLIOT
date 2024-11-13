/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.nhom11.iotapp.form;

import com.nhom11.iotapp.bluetooth.BluetoothManager;
import com.nhom11.iotapp.components.ScrollBar;
import com.nhom11.iotapp.event.PublicEvent;
import com.nhom11.iotapp.event.TableSelectedEvent;
import com.nhom11.iotapp.tablecustom.TableActionCellEditor;
import com.nhom11.iotapp.tablecustom.TableActionCellRenderer;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.RemoteDevice;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class DeviceSelectionForm extends javax.swing.JPanel {

    /**
     * Creates new form DeviceSelectionForm
     */
    public DeviceSelectionForm() {
        initComponents();
        jScrollPane.setVerticalScrollBar(new ScrollBar());
//        Để setBackground của JScrollPane, phải getViewport sau đó ms setBackground
        jScrollPane.getViewport().setBackground(Color.white);

        jScrollPane.setBorder(null);
        JPanel p = new JPanel();
        jScrollPane.getVerticalScrollBar().setBackground(Color.white);
        p.setBackground(Color.white);
        jScrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);
        setTableData(null);
        devicesTable.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRenderer());
        devicesTable.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(
                new TableSelectedEvent() {
            @Override
            public void onClick(int row) {
                System.out.println("Row clicked: " + row);
                PublicEvent.getInstance().getEventMenuForm().changeForm(new DeviceDetailForm());
            }
        }
        ));
    }

    public void setTableData(List<RemoteDevice> devices) {
        DefaultTableModel model = (DefaultTableModel) devicesTable.getModel();
        model.setRowCount(0);
        if (devices != null && devices.size() > 0) {
            for (int i = 0; i < devices.size(); i++) {
                try {
                    RemoteDevice temp = devices.get(i);
                    devicesTable.addRow(new Object[]{i + 1, temp.getFriendlyName(false), temp.getBluetoothAddress()});
                } catch (IOException ex) {
                    Logger.getLogger(DeviceSelectionForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchBtn = new com.nhom11.iotapp.components.MyButton();
        panelBox = new javax.swing.JPanel();
        jScrollPane = new javax.swing.JScrollPane();
        devicesTable = new com.nhom11.iotapp.components.Table();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 153, 204));
        setOpaque(false);

        searchBtn.setBackground(new java.awt.Color(51, 204, 255));
        searchBtn.setForeground(new java.awt.Color(255, 255, 255));
        searchBtn.setText("Search Devices");
        searchBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        panelBox.setBackground(new java.awt.Color(255, 102, 0));
        panelBox.setOpaque(false);
        panelBox.setLayout(new java.awt.CardLayout());

        jScrollPane.setBackground(new java.awt.Color(255, 102, 102));
        jScrollPane.setBorder(null);
        jScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setOpaque(false);

        devicesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Num", "Device Name", "Device ID", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        devicesTable.setSelectionBackground(new java.awt.Color(204, 204, 204));
        jScrollPane.setViewportView(devicesTable);
        if (devicesTable.getColumnModel().getColumnCount() > 0) {
            devicesTable.getColumnModel().getColumn(0).setResizable(false);
            devicesTable.getColumnModel().getColumn(1).setResizable(false);
            devicesTable.getColumnModel().getColumn(2).setResizable(false);
            devicesTable.getColumnModel().getColumn(3).setResizable(false);
        }

        panelBox.add(jScrollPane, "card2");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        jLabel1.setText("Devices List");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panelBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 555, Short.MAX_VALUE)
                                .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(14, 14, 14))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(searchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBox, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        try {
            new Thread(
                    new Runnable() {
                @Override
                public void run() {
//                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                    List<RemoteDevice> devicesList;
                    try {
                        devicesList = BluetoothManager.getInstance().getDevicesList();
                        setTableData(devicesList);
                    } catch (BluetoothStateException ex) {
                        Logger.getLogger(DeviceSelectionForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            ).start();
            
        } catch (Exception ex) {
            Logger.getLogger(DeviceSelectionForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_searchBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.nhom11.iotapp.components.Table devicesTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JPanel panelBox;
    private com.nhom11.iotapp.components.MyButton searchBtn;
    // End of variables declaration//GEN-END:variables
}
