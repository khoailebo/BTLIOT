/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.nhom11.iotapp.components;

import com.nhom11.iotapp.model.ModelMenu;
import com.nhom11.iotapp.enums.MenuType;
import com.nhom11.iotapp.enums.UserRole;
import com.nhom11.iotapp.https.HttpClientManager;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 *
 * @author Administrator
 */
public class MenuSideBar extends javax.swing.JPanel {

    /**
     * Creates new form MenuSideBar
     */
    public MenuSideBar() {
        initComponents();
        init();
    }

    public final void init() {
//        menuList.removeAll();
        menuList.removeAll();
        menuList.addItem(new ModelMenu("Control", "", MenuType.MENU_GROUP));
        menuList.addItem(new ModelMenu("Devices", "icon_device", MenuType.MENU_ITEM));
        if(HttpClientManager.getInstance().getUser().getUser_role() == UserRole.admin){
            menuList.addItem(new ModelMenu("Manage Device","icon_device",MenuType.MENU_ITEM));
        }
        
        menuList.addItem(new ModelMenu("User", "", MenuType.MENU_GROUP));
        menuList.addItem(new ModelMenu("Account", "icon_user", MenuType.MENU_ITEM));
        menuList.addItem(new ModelMenu("Sign out", "icon_logout", MenuType.MENU_ITEM));
        menuList.addItem(new ModelMenu("Sign out", "icon_logout", MenuType.MENU_SPACE));


        menuList.updateUI();
//        menuList.addItem(new ModelMenu("Devices","",MenuType.MENU_GROUP));
//        menuList.addItem(new ModelMenu("Devices","",MenuType.MENU_GROUP));
//        menuList.addItem(new ModelMenu("Devices","",MenuType.MENU_GROUP));
//        menuList.addItem(new ModelMenu("Devices","",MenuType.MENU_GROUP));
//        menuList.addItem(new ModelMenu("Devices","",MenuType.MENU_GROUP));
//        menuList.addItem(new ModelMenu("Devices","",MenuType.MENU_GROUP));

    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.fillRect(getWidth() - 20, 0, 20, getHeight());
        super.paintChildren(g); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    

    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        controlPanel = new com.nhom11.iotapp.components.ControlPanel();
        imagePanel = new com.nhom11.iotapp.components.ImagePanel();
        lbName = new javax.swing.JLabel();
        menuList = new com.nhom11.iotapp.swing.MenuList<>();

        setBackground(new java.awt.Color(51, 182, 252));
        setOpaque(false);

        imagePanel.setImage(new javax.swing.ImageIcon(getClass().getResource("/com/nhom11/iotapp/resources/icon/Vietnamese_Traffic_Police_Icon-removebg-preview.png"))); // NOI18N
        imagePanel.setPreferredSize(new java.awt.Dimension(60, 60));

        javax.swing.GroupLayout imagePanelLayout = new javax.swing.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        lbName.setBackground(new java.awt.Color(255, 255, 255));
        lbName.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lbName.setForeground(new java.awt.Color(255, 255, 255));
        lbName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbName.setText("CSGT");

        menuList.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        menuList.setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbName)
                .addGap(26, 26, 26))
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(menuList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbName, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuList, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                .addGap(19, 19, 19))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.nhom11.iotapp.components.ControlPanel controlPanel;
    private com.nhom11.iotapp.components.ImagePanel imagePanel;
    private javax.swing.JLabel lbName;
    private com.nhom11.iotapp.swing.MenuList<String> menuList;
    // End of variables declaration//GEN-END:variables
}
