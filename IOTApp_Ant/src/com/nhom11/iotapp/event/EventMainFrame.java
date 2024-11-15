/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nhom11.iotapp.event;

import java.awt.event.MouseEvent;

/**
 *
 * @author DELL
 */
public interface EventMainFrame {
    public void dragingFrame(int x,int y, MouseEvent e);
    public void tabBarFrame();
    public void closeFrame();
}
