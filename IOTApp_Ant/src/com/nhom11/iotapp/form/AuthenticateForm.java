/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.nhom11.iotapp.form;

import com.nhom11.iotapp.event.PublicEvent;
import com.nhom11.iotapp.swing.EventLogin;

/**
 *
 * @author DELL
 */
public class AuthenticateForm extends javax.swing.JPanel {

    /**
     * Creates new form AuthenticateForm
     */
    public AuthenticateForm() {
        initComponents();
        loginAndRegister.setEventLogin(new EventLogin(){
            @Override
            public void loginDone() {
//                System.out.println("log in");
                PublicEvent.getInstance().getEventMainFrame().changeForm(new MenuForm());
            }

            @Override
            public void logOut() {
                
            }
            
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loginAndRegister = new com.nhom11.iotapp.components.LoginAndRegister();

        setOpaque(false);
        setLayout(new java.awt.CardLayout());
        add(loginAndRegister, "card2");
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.nhom11.iotapp.components.LoginAndRegister loginAndRegister;
    // End of variables declaration//GEN-END:variables
}
