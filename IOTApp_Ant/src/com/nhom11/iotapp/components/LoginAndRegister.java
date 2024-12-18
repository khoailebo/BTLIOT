package com.nhom11.iotapp.components;

import com.nhom11.iotapp.event.RegisterEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import com.nhom11.iotapp.swing.EventLogin;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class LoginAndRegister extends javax.swing.JPanel {

    private MigLayout layout;
    private Register register;
    private Login login;
    private Animator animator;
    private boolean isLogin;
    public static Color mainColor = new Color(51,182,252);

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(mainColor);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        super.paintComponent(g); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
    // public void setAnimate(int animate) {
    //     layout.setComponentConstraints(register, "pos (50%)-290px-" + animate + " 0.5al n n");
    //     layout.setComponentConstraints(login, "pos (50%)-10px+" + animate + " 0.5al n n");
    //     if (animate == 30) {
    //         if (isLogin) {
    //             setComponentZOrder(login, 0);
    //         } else {
    //             setComponentZOrder(register, 0);
    //         }
    //     }
    //     revalidate();
    // }

    public void setAnimate(int animate)
    {
        // layout.setComponentConstraints(register, "pos (50%)-290px-" + animate + " 0.5al n n");
        // layout.setComponentConstraints(login,"pos (50%)-10px+" + animate + " 0.5al n n");
        // if(animate == 30)
        // {
        //     if(isLogin)
        //     {
        //         setComponentZOrder(login, 0);
        //     } else {
        //         setComponentZOrder(register, 0);
        //     }
        // }
        // revalidate();
        layout.setComponentConstraints(register, "pos (50%)-290px-"+ animate + " 0.5al n n");
        layout.setComponentConstraints(login, "pos (50%)-10px+"+animate + " 0.5al n n");
        if(animate == 30)
        {
            if(isLogin)
            {
                setComponentZOrder(login, 0);
            } else 
            {
                setComponentZOrder(register,0);
            }
        }
        revalidate();
    }
    public LoginAndRegister() {
        initComponents();
        // init();
        init_Selfwork();
        initAnimator_Selfwork();
    }
    public void applyEventSelfWork(JComponent comp,boolean show)
    {
        for(Component com:comp.getComponents())
        {
            com.addMouseListener(new MouseAdapter(){
                @Override
                public void mousePressed(MouseEvent e) {
                    if(SwingUtilities.isLeftMouseButton(e))
                    {
                        showLogin(show);
                    }
                }
            });
        }
    }
    private void initAnimator_Selfwork(){
        animator = new Animator(1000,new TimingTargetAdapter(){
            @Override
            public void timingEvent(float fraction) {
                if(isLogin)
                {
                    //fraction tăng dần
                    login.setAlpha(1f - fraction);
                    register.setAlpha(fraction);
                }
                else {
                    login.setAlpha(fraction);
                    register.setAlpha(1f - fraction);
                }
                // System.out.println(fraction);
            }
        });
        animator.addTarget(new PropertySetter(this, "animate", 0,30,0));
        animator.setResolution(0);
    }
    private void init_Selfwork(){
        layout = new MigLayout("fill,insets 12 14 0 0 ","fill","fill");
        setBackground(mainColor);
        setLayout(layout);
        register = new Register();
        login = new Login();
        ControlPanel controlPanel = new ControlPanel();
        applyEventSelfWork(login, true);
        applyEvent(register, false);
        register.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e))
                {
                    showLogin(false);
                }
            }
        });
        login.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e))
                {
                    showLoginSelfwork(true);
                }
            }
        });
        add(controlPanel);
        add(register,"pos (50%)-290px 0.5al n n");
        add(login,"pos (50%)-10px 0.5al n n");

    }
    private void showLoginSelfwork(boolean show){
        if(show != isLogin)
        {
            if(!animator.isRunning())
            {
                isLogin = show;
                animator.start();
            }
        }
    }
    private void initAnimator() {
        animator = new Animator(1000, new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (isLogin) {
                    register.setAlpha(fraction);
                    login.setAlpha(1f - fraction);
                } else {
                    register.setAlpha(1f - fraction);
                    login.setAlpha(fraction);
                }
            }
        });
        animator.addTarget(new PropertySetter(this, "animate", 0, 30, 0));
        animator.setResolution(0);
    }

    private void init() {
        setBackground(mainColor);
        layout = new MigLayout("fill", "fill", "fill");
        setLayout(layout);
        register = new Register();
        login = new Login();
        applyEvent(register, false);
        applyEvent(login, true);
        add(register, "pos (50%)-290px 0.5al n n");
        add(login, "pos (50%)-10px 0.5al n n");
        register.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isLeftMouseButton(me)) {
                    showLogin(false);
                }
            }
        });
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isLeftMouseButton(me)) {
                    showLogin(true);
                }
            }
        });
    }

    public void showLogin(boolean show) {
        if (show != isLogin) {
            if (!animator.isRunning()) {
                isLogin = show;
                animator.start();
            }
        }
    }

    private void applyEvent(JComponent panel, boolean login) {
        for (Component com : panel.getComponents()) {
            com.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent me) {
                    showLogin(login);
                }
            });
        }
    }

    public void setEventLogin(EventLogin event) {
        login.setEventLogin(event);
    }
    public void setEventRegister(RegisterEvent event){
        register.setEvent(event);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 698, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 452, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
