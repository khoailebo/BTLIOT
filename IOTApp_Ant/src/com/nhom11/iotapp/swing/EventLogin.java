package com.nhom11.iotapp.swing;

public interface EventLogin {

    public void startLogin();
    public void loginDone();
    public void loginFailed(String msg);
    public void logOut();
}
