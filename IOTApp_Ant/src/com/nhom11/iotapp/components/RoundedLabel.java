/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author DELL
 */
public class RoundedLabel extends JLabel{
    public RoundedLabel(){
        setOpaque(false);
        setBorder(new EmptyBorder(5,10,5,10));
        setBackground(new Color(51,182,252));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 15, 15);
        super.paintComponent(g); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
}
