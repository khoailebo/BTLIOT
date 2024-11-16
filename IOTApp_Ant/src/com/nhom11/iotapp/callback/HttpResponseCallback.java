/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.callback;

/**
 *
 * @author DELL
 */
public interface HttpResponseCallback {
    public void onSuccess(Object ...os);
    public void onFailed(Object ...os);
}
