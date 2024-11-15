/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author HP
 */
public class ModernScrollBarUI extends BasicScrollBarUI {

    private static final int SCROLL_BAR_ALPHA_ROLLOVER = 100;
    private static final int SCROLL_BAR_ALPHA = 50;
    private static final int THUMB_SIZE = 8;
    private static final Color THUMB_COLOR = Color.black;
    public ModernScrollBarUI(){
        
    }
    @Override
    protected JButton createDecreaseButton(int orientation) {
//        return super.createDecreaseButton(orientation); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        return new InvisibleButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
//        return super.createIncreaseButton(orientation); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
        return new InvisibleButton();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        
    }

    
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        int alpha;
        if (isThumbRollover()) {
            alpha = SCROLL_BAR_ALPHA_ROLLOVER;
        } else {
            alpha = SCROLL_BAR_ALPHA;
        }
        int x = thumbBounds.x, y = thumbBounds.y;
        int orientation = scrollbar.getOrientation();
        int width = orientation == JScrollBar.VERTICAL ? THUMB_SIZE : thumbBounds.width;
        width = Math.max(width, THUMB_SIZE);
        int height = orientation == JScrollBar.VERTICAL ? thumbBounds.height : THUMB_SIZE;
        height = Math.max(height, THUMB_SIZE);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(THUMB_COLOR.getRed(), THUMB_COLOR.getGreen(), THUMB_COLOR.getBlue(), alpha));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(x + 2, y, width - 4, height, 4, 4);
        g2.dispose();
    }

    static class InvisibleButton extends JButton {

        private InvisibleButton() {
            //for button, if you don't want to
            //draw the back 
//            setOpaque(false);
            setContentAreaFilled(false);
            setFocusable(false);

            setBorderPainted(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(0, 0, 0, 0));
        }
    }
}
