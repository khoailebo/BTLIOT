/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.model;

import com.nhom11.iotapp.model.enums.MenuType;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author DELL
 */
public class ModelMenu {
    String MenuName;

    public String getMenuName() {
        return MenuName;
    }

    public void setMenuName(String MenuName) {
        this.MenuName = MenuName;
    }

    public String getMenuIcon() {
        return MenuIcon;
    }

    public void setMenuIcon(String MenuIcon) {
        this.MenuIcon = MenuIcon;
    }

    public MenuType getType() {
        return Type;
    }

    public void setType(MenuType Type) {
        this.Type = Type;
    }
    String MenuIcon;
    MenuType Type;
    public ModelMenu(String name,String icon,MenuType type){
        this.MenuName = name;
        this.MenuIcon = icon;
        this.Type = type;
    }
    public Icon toImage(){
        return new ImageIcon(getClass().getResource("/icon/" + MenuIcon + ".png"));
    }
}
