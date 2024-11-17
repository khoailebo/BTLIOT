/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.nhom11.iotapp.form;

import com.nhom11.iotapp.bluetooth.BluetoothManager;
import com.nhom11.iotapp.bluetooth.VirtualDevice;
import com.nhom11.iotapp.callback.HttpResponseCallback;
import com.nhom11.iotapp.callback.Invokelater;
import com.nhom11.iotapp.components.LoadingPanel;
import com.nhom11.iotapp.components.PopupPanel;
import com.nhom11.iotapp.components.ScrollBar;
import com.nhom11.iotapp.components.Table_Header;
import com.nhom11.iotapp.entities.ModelDevice;
import com.nhom11.iotapp.enums.PopupType;
import com.nhom11.iotapp.event.PublicEvent;
import com.nhom11.iotapp.event.TableSelectedEvent;
import com.nhom11.iotapp.https.HttpClientManager;
import com.nhom11.iotapp.mainframe.MainFrame;
import com.nhom11.iotapp.tablecustom.TableActionCellEditor;
import com.nhom11.iotapp.tablecustom.TableActionCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaswingdev.Notification;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.RemoteDevice;
import javax.swing.JLabel;
import raven.glasspanepopup.GlassPanePopup;
import raven.glasspanepopup.*;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.hc.core5.http.ParseException;

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
        devicesTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Table_Header th = new Table_Header(value + "");
                th.setHorizontalAlignment(JLabel.LEADING);
                th.setBorder(new EmptyBorder(5, 20, 5, 0));
                if (isSelected || row % 2 != 0) {
                    th.setBackground(comp.getBackground());
                } else {
                    th.setBackground(Color.WHITE);
                }
                return th;
            }

        });
        devicesTable.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(
                new TableSelectedEvent() {
            @Override
            public void onClick(int row) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Row clicked: " + row);
                        PublicEvent.getInstance().getEventMenuForm().changeForm(new LoadingPanel());
                        BluetoothManager.getInstance().connectToDevice(row, new Invokelater() {
                            @Override
                            public void call(Object... obj) {
                                boolean connected = (boolean) obj[0];
                                if (connected) {
                                    PublicEvent.getInstance().getEventMenuForm().changeForm(new DeviceDetailForm());
                                    try {
                                        BluetoothManager.getInstance().getVirtualDevice().setIdentify(
                                                HttpClientManager.getInstance().checkDeviceIdentity(
                                                        BluetoothManager.getInstance().getVirtualDevice().getId()
                                                )
                                        );
                                        if (!BluetoothManager.getInstance().getVirtualDevice().isIdentify()) {
//                                            Notification panel = new Notification(MainFrame.CurrentInstance, Notification.Type.WARNING, Notification.Location.TOP_CENTER, "Device hasn't been identified!");
//                                            panel.showNotification();
//                                            BluetoothManager.getInstance().getVirtualDevice().disconnectProtocol();
                                            Option option = new DefaultOption() {
                                                @Override
                                                public boolean closeWhenClickOutside() {
                                                    return false; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
                                                }

                                            };
                                            GlassPanePopup.showPopup(new PopupPanel("Warning", "Device hasn't been identified", "Click OK to Identify", PopupType.WARNING,
                                                    new Invokelater() {
                                                @Override
                                                public void call(Object... obj) {
                                                    GlassPanePopup.closePopup("warning device");
                                                    BluetoothManager.getInstance().getVirtualDevice().disconnectProtocol();
                                                }
                                            }, new Invokelater() {
                                                @Override
                                                public void call(Object... obj) {
                                                    try {
                                                        System.out.println("Identify");
                                                        VirtualDevice device = BluetoothManager.getInstance().getVirtualDevice();
                                                        HttpClientManager.getInstance().idendifyDevice(new ModelDevice(
                                                                device.getId(), device.getName(), "Model demo"),
                                                                new HttpResponseCallback() {
                                                            @Override
                                                            public void onSuccess(Object... os) {
                                                                Notification panel = new Notification(MainFrame.CurrentInstance, Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, (String) os[0]);
                                                                panel.showNotification();
//                                                                BluetoothManager.getInstance().getVirtualDevice().disconnectProtocol();
                                                            }

                                                            @Override
                                                            public void onFailed(Object... os) {
                                                                Notification panel = new Notification(MainFrame.CurrentInstance, Notification.Type.WARNING, Notification.Location.TOP_CENTER, (String) os[0]);
                                                                panel.showNotification();
                                                                BluetoothManager.getInstance().getVirtualDevice().disconnectProtocol();
                                                            }

                                                        });
                                                        GlassPanePopup.closePopup("warning device");
                                                    } catch (IOException | ParseException ex) {
                                                        Logger.getLogger(DeviceSelectionForm.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                }

                                            }), option, "warning device");

                                        }
                                    } catch (URISyntaxException | IOException | ParseException ex) {
                                        Logger.getLogger(DeviceSelectionForm.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else {
                                    PublicEvent.getInstance().getEventMenuForm().changeForm(new DeviceSelectionForm());
                                }
                            }

                        });
                    }

                }, "Connect Thread").start();
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

    public void showLoading(boolean show) {
        loadingPanel.setVisible(show);
        jScrollPane.setVisible(!show);
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
        loadingPanel = new com.nhom11.iotapp.components.LoadingPanel();
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
        panelBox.add(loadingPanel, "card3");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
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
        if (!BluetoothManager.getInstance().isConnecting()) {
            try {
                new Thread(
                        new Runnable() {
                    @Override
                    public void run() {
//                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                        List<RemoteDevice> devicesList;
                        try {
//                        loadingPanel.setVisible(true);
                            showLoading(true);
                            devicesList = BluetoothManager.getInstance().getDevicesList();
                            setTableData(devicesList);
                        } catch (BluetoothStateException ex) {
                            Logger.getLogger(DeviceSelectionForm.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            showLoading(false);
                        }
                    }
                }
                ).start();

            } catch (Exception ex) {
                Logger.getLogger(DeviceSelectionForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_searchBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.nhom11.iotapp.components.Table devicesTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane;
    private com.nhom11.iotapp.components.LoadingPanel loadingPanel;
    private javax.swing.JPanel panelBox;
    private com.nhom11.iotapp.components.MyButton searchBtn;
    // End of variables declaration//GEN-END:variables
}
