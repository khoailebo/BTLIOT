/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.nhom11.iotapp.form;

import com.nhom11.iotapp.bluetooth.BluetoothManager;
import com.nhom11.iotapp.callback.HttpResponseCallback;
import com.nhom11.iotapp.callback.Invokelater;
import com.nhom11.iotapp.components.PopupPanel;
import com.nhom11.iotapp.components.ScrollBar;
import com.nhom11.iotapp.components.Table_Header;
import com.nhom11.iotapp.entities.ModelDevice;
import com.nhom11.iotapp.enums.PopupType;
import com.nhom11.iotapp.https.HttpClientManager;
import com.nhom11.iotapp.mainframe.MainFrame;
import com.nhom11.iotapp.tablecustom.ManageDeviceTableEditor;
import com.nhom11.iotapp.tablecustom.ManageDeviceTableRenderer;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaswingdev.Notification;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.hc.core5.http.ParseException;
import raven.glasspanepopup.GlassPanePopup;

/**
 *
 * @author DELL
 */
public class ManageDeviceForm extends javax.swing.JPanel {

    /**
     * Creates new form ManageDeviceForm
     */
    public ManageDeviceForm() {
        initComponents();
        initScrollPane();
        initTableGUI();
        setTableData(null);
    }

    public void initTableGUI() {
        deviceTable.getColumnModel().getColumn(3).setCellRenderer(new ManageDeviceTableRenderer());
        deviceTable.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
                Table_Header th = new Table_Header(value.toString());
                th.setBorder(new EmptyBorder(5, 20, 5, 10));
                if (!isSelected && row % 2 == 0) {
                    th.setBackground(Color.WHITE);
                } else {
                    th.setBackground(comp.getBackground());
                }
                return th;
            }

        });
        deviceTable.getColumnModel().getColumn(3).setCellEditor(new ManageDeviceTableEditor(new Invokelater() {
            @Override
            public void call(Object... obj) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String deviceId = deviceTable.getModel().getValueAt(((int) obj[0]), 1).toString();
//                        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                        GlassPanePopup.showPopup(
                                new PopupPanel(
                                        "Be Carefull", "You want to delete " + deviceId, "Click OK to Confrim", PopupType.WARNING,
                                        new Invokelater() {
                                    @Override
                                    public void call(Object... obj) {
//                                        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                                        GlassPanePopup.closePopup("confirm delete");
                                    }
                                }, (Object... obj1) -> {
//                                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        //                                                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                                                        showLoading(true);
                                                        String deviceId = deviceTable.getModel().getValueAt(((int) obj[0]), 1).toString();
                                                        HttpClientManager.getInstance().deleteDevice(deviceId,
                                                                new HttpResponseCallback() {
                                                            @Override
                                                            public void onSuccess(Object... os) {
//                                                                        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                                                                Notification panel = new Notification(MainFrame.CurrentInstance, Notification.Type.SUCCESS, Notification.Location.TOP_CENTER, (String) os[0]);
                                                                panel.showNotification();
                                                                String deviceId = deviceTable.getModel().getValueAt(((int) obj[0]), 1).toString();
                                                                if(BluetoothManager.getInstance().isConnected()
                                                                        &&BluetoothManager.getInstance().getVirtualDevice().getId().equals(deviceId)){
                                                                    try {
                                                                        BluetoothManager.getInstance().getVirtualDevice().disconnect(false);
                                                                    } catch (IOException ex) {
                                                                        Logger.getLogger(ManageDeviceForm.class.getName()).log(Level.SEVERE, null, ex);
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailed(Object... os) {
//                                                                        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                                                                Notification panel = new Notification(MainFrame.CurrentInstance, Notification.Type.INFO, Notification.Location.TOP_CENTER, (String) os[0]);
                                                                panel.showNotification();
                                                            }
                                                        }
                                                        );
                                                    } catch (IOException ex) {
                                                        Logger.getLogger(ManageDeviceForm.class.getName()).log(Level.SEVERE, null, ex);
                                                    } catch (ParseException ex) {
                                                        Logger.getLogger(ManageDeviceForm.class.getName()).log(Level.SEVERE, null, ex);
                                                    } finally {
//                                                        showLoading(false);
                                                        GlassPanePopup.closePopup("confirm delete");
                                                        getDevicesBtnActionPerformed(null);
                                                    }
                                                }
                                            }, "Delete Device Thread").start();

                                        }), "confirm delete");
                    }
                }, "Confirm Delete Device Thread").start();
            }
        }));
    }

    public void initScrollPane() {
        jScrollPane.getViewport().setBackground(Color.white);
        jScrollPane.setVerticalScrollBar(new ScrollBar());
        jScrollPane.getVerticalScrollBar().setBackground(Color.white);
        JPanel p = new JPanel();
        p.setBackground(Color.white);
        jScrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);
    }

    public void showLoading(boolean show) {
        loadingPanel1.setVisible(show);
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

        jLabel1 = new javax.swing.JLabel();
        getDevicesBtn = new com.nhom11.iotapp.components.BtnClickAnimate();
        jLabel4 = new javax.swing.JLabel();
        panelBox = new javax.swing.JPanel();
        jScrollPane = new javax.swing.JScrollPane();
        deviceTable = new com.nhom11.iotapp.components.Table();
        loadingPanel1 = new com.nhom11.iotapp.components.LoadingPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Mange Devices");

        getDevicesBtn.setBackground(new java.awt.Color(51, 204, 255));
        getDevicesBtn.setForeground(new java.awt.Color(255, 255, 255));
        getDevicesBtn.setText("Get Device List");
        getDevicesBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        getDevicesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getDevicesBtnActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Devices List");

        panelBox.setBackground(new java.awt.Color(255, 255, 255));
        panelBox.setLayout(new java.awt.CardLayout());

        jScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jScrollPane.setOpaque(false);

        deviceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Num", "Device Id", "Device Name", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        deviceTable.setSelectionBackground(new java.awt.Color(204, 204, 204));
        jScrollPane.setViewportView(deviceTable);
        if (deviceTable.getColumnModel().getColumnCount() > 0) {
            deviceTable.getColumnModel().getColumn(0).setResizable(false);
            deviceTable.getColumnModel().getColumn(1).setResizable(false);
            deviceTable.getColumnModel().getColumn(2).setResizable(false);
            deviceTable.getColumnModel().getColumn(3).setResizable(false);
        }

        panelBox.add(jScrollPane, "card2");
        panelBox.add(loadingPanel1, "card3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(panelBox, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(getDevicesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(43, 43, 43))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(getDevicesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelBox, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void setTableData(Object[] devices) {
        DefaultTableModel model = (DefaultTableModel) deviceTable.getModel();
        model.setRowCount(0);
        if (devices != null && devices.length > 0) {
            for (int i = 0; i < devices.length; i++) {
                if (devices[i] instanceof ModelDevice device) {
                    deviceTable.addRow(new Object[]{i + 1, device.getDevice_id(), device.getName()});
                }
            }
        }
        deviceTable.repaint();
        deviceTable.revalidate();
    }
    private void getDevicesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getDevicesBtnActionPerformed
        // TODO add your handling code here:
        new Thread(() -> {
            try {
                setTableData(null);
                showLoading(true);
                //                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                HttpClientManager.getInstance().getDeviceList(new HttpResponseCallback() {
                    @Override
                    public void onSuccess(Object... os) {
                        System.out.println(os.length);
                        setTableData(os);
//                            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                    }

                    @Override
                    public void onFailed(Object... os) {
                        setTableData(null);
                        Notification panel = new Notification(MainFrame.CurrentInstance, Notification.Type.INFO, Notification.Location.TOP_CENTER, "Can not get device list");
                        panel.showNotification();
//                            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                    }
                });
            } catch (URISyntaxException | IOException | ParseException ex) {
                Logger.getLogger(ManageDeviceForm.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                showLoading(false);
            }
        }, "Get Device List Thread").start();
    }//GEN-LAST:event_getDevicesBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.nhom11.iotapp.components.Table deviceTable;
    private com.nhom11.iotapp.components.BtnClickAnimate getDevicesBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane;
    private com.nhom11.iotapp.components.LoadingPanel loadingPanel1;
    private javax.swing.JPanel panelBox;
    // End of variables declaration//GEN-END:variables
}
