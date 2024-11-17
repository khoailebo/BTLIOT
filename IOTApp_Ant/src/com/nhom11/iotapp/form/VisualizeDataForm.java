/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.nhom11.iotapp.form;

import com.nhom11.iotapp.callback.HttpResponseCallback;
import com.nhom11.iotapp.chart.ModelChart;
import com.nhom11.iotapp.entities.Mesurement;
import com.nhom11.iotapp.https.HttpClientManager;
import com.nhom11.iotapp.mainframe.MainFrame;
import java.awt.Color;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaswingdev.Notification;
import org.apache.hc.core5.http.ParseException;

/**
 *
 * @author DELL
 */
public class VisualizeDataForm extends javax.swing.JPanel {

    /**
     * Creates new form VisualizeDataForm
     */
    public VisualizeDataForm() {
        initComponents();
        initChart();
        initData();
    }
    HashMap<String, Double> valueMap;

    public void showLoading(boolean show) {
        loadingPanel.setVisible(show);
        panelShadow.setVisible(!show);
    }

    public void setUpData(int start, int stop, int step, Object[] mesurementArray) {
        valueMap = new HashMap<>();
        for (int i = start; i <= stop; i += step) {
            double value = 0;
            int time = 0;
            for (int j = 0; j < mesurementArray.length; j++) {
                if (mesurementArray[j] instanceof Mesurement mesurement) {

                    if (mesurement.getSubject_age() <= i) {
                        time++;
                        value += mesurement.getAlcohol_level();
                    }
                }
            }
            double averageAlcoholLevel = value / time;
            valueMap.put(Integer.toString(i), averageAlcoholLevel);
        }
        showLineChart(start,stop,step);
    }

    public void showLineChart(int start, int stop,int step) {
        for (int i = start;i <= stop;i+=step) {
            curveLineChart.addData(new ModelChart(Integer.toString(i), new double[]{valueMap.get(Integer.toString(i))}));
        }
        curveLineChart.start();
    }

    public void initData() {
        if (HttpClientManager.getInstance().getUser() != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        showLoading(true);
                        HttpClientManager.getInstance().getAllMesurements(new HttpResponseCallback() {
                            @Override
                            public void onSuccess(Object... os) {
                                setUpData(25, 60, 5, os);
                                
                            }

                            @Override
                            public void onFailed(Object... os) {
//                                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                                Notification panel = new Notification(MainFrame.CurrentInstance, Notification.Type.INFO, Notification.Location.TOP_CENTER, (String) os[0]);
                                panel.showNotification();

                            }
                        });
//                    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(VisualizeDataForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(VisualizeDataForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(VisualizeDataForm.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
//                        showLoading(false);
                    }
                }
            }, "Init Chart Data Thread").start();
        }
    }

    public void initChart() {
        curveLineChart.setTitle("Average Alcohol Concentration by Age");
        curveLineChart.addLegend("Alcohol Level", Color.decode("#FF5F6D"), Color.decode("#FFC371"));
        curveLineChart.clear();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loadingPanel = new com.nhom11.iotapp.components.LoadingPanel();
        panelShadow = new com.nhom11.iotapp.panel.PanelShadow();
        curveLineChart = new com.nhom11.iotapp.chart.CurveLineChart();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.CardLayout());

        panelShadow.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        curveLineChart.setForeground(new java.awt.Color(255, 102, 51));
        curveLineChart.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        javax.swing.GroupLayout panelShadowLayout = new javax.swing.GroupLayout(panelShadow);
        panelShadow.setLayout(panelShadowLayout);
        panelShadowLayout.setHorizontalGroup(
            panelShadowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShadowLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(curveLineChart, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelShadowLayout.setVerticalGroup(
            panelShadowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShadowLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(curveLineChart, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(panelShadow);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.nhom11.iotapp.chart.CurveLineChart curveLineChart;
    private com.nhom11.iotapp.components.LoadingPanel loadingPanel;
    private com.nhom11.iotapp.panel.PanelShadow panelShadow;
    // End of variables declaration//GEN-END:variables
}