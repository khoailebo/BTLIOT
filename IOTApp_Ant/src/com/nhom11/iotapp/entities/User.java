/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.entities;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nhom11.iotapp.enums.UserRole;

/**
 *
 * @author DELL
 */
public class User extends JsonFormat{
    String access_token;
    UserRole user_roleRole;
    int user_id;

    public User(String access_token, UserRole user_roleRole, int user_id) {
        this.access_token = access_token;
        this.user_roleRole = user_roleRole;
        this.user_id = user_id;
    }
    public User(JsonObject data){
        access_token = data.get("access_token").getAsString();
        user_roleRole = UserRole.fromString(data.get("user_role").getAsString());
        user_id = data.get("user_id").getAsInt();
    }
    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public UserRole getUser_roleRole() {
        return user_roleRole;
    }

    public void setUser_roleRole(UserRole user_roleRole) {
        this.user_roleRole = user_roleRole;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
