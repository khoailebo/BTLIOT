package com.nhom11.iotapp.components;

import com.nhom11.iotapp.callback.HttpResponseCallback;
import com.nhom11.iotapp.entities.ModelSignUp;
import com.nhom11.iotapp.enums.UserRole;
import com.nhom11.iotapp.event.RegisterEvent;
import com.nhom11.iotapp.https.HttpClientManager;
import com.nhom11.iotapp.mainframe.MainFrame;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaswingdev.Notification;
import org.apache.hc.core5.http.ParseException;

public class Register extends PanelCustom {

    RegisterEvent event;

    public Register() {
        initComponents();
    }

    public void setEvent(RegisterEvent event) {
        this.event = event;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tfName = new com.nhom11.iotapp.swing.TextField();
        tfEmail = new com.nhom11.iotapp.swing.TextField();
        tfUserName = new com.nhom11.iotapp.swing.TextField();
        tfPassWord = new com.nhom11.iotapp.swing.Password();
        button1 = new com.nhom11.iotapp.swing.Button();

        setBackground(new java.awt.Color(58, 58, 58));

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(242, 242, 242));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("SIGN UP");

        tfName.setForeground(new java.awt.Color(242, 242, 242));
        tfName.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        tfName.setHint("NAME");

        tfEmail.setForeground(new java.awt.Color(242, 242, 242));
        tfEmail.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        tfEmail.setHint("EMAIL");

        tfUserName.setForeground(new java.awt.Color(242, 242, 242));
        tfUserName.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        tfUserName.setHint("USER NAME");

        tfPassWord.setForeground(new java.awt.Color(242, 242, 242));
        tfPassWord.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        tfPassWord.setHint("PASSWORD");

        button1.setBackground(new java.awt.Color(86, 142, 255));
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setText("Sign Up");
        button1.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(button1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfPassWord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addComponent(tfEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tfUserName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel1)
                .addGap(40, 40, 40)
                .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tfUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tfPassWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        // TODO add your handling code here:
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ModelSignUp modelSignUp = new ModelSignUp(tfUserName.getText(), new String(tfPassWord.getPassword()),
                            tfEmail.getText(), tfName.getText(), UserRole.officer);
                    HttpClientManager.getInstance().signup(modelSignUp, new HttpResponseCallback() {
                        @Override
                        public void onSuccess(Object... os) {
                            event.RegisterSucces();
                        }

                        @Override
                        public void onFailed(Object... os) {
                            Notification panel = new Notification(MainFrame.CurrentInstance, Notification.Type.WARNING, Notification.Location.TOP_CENTER, (String) os[0]);
                            panel.showNotification();
                        }
                    });
                } catch (IOException ex) {
                    Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, "Signup Thread").start();
    }//GEN-LAST:event_button1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.nhom11.iotapp.swing.Button button1;
    private javax.swing.JLabel jLabel1;
    private com.nhom11.iotapp.swing.TextField tfEmail;
    private com.nhom11.iotapp.swing.TextField tfName;
    private com.nhom11.iotapp.swing.Password tfPassWord;
    private com.nhom11.iotapp.swing.TextField tfUserName;
    // End of variables declaration//GEN-END:variables
}
