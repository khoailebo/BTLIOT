/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom11.iotapp.event;

/**
 *
 * @author DELL
 */
public class PublicEvent {
    public static PublicEvent instance;
    private PublicEvent(){}
    private EventMainFrame eventMainFrame;
    private EventMenuForm eventMenuForm;

    public EventMenuForm getEventMenuForm() {
        return eventMenuForm;
    }

    public void setEventMenuForm(EventMenuForm eventMenuForm) {
        this.eventMenuForm = eventMenuForm;
    }
    public static PublicEvent getInstance(){
        if(instance == null)
        {
            instance = new PublicEvent();
        }
        return instance;
    }

    /**
     * @return the eventMainFrame
     */
    public EventMainFrame getEventMainFrame() {
        return eventMainFrame;
    }

    /**
     * @param eventMainFrame the eventMainFrame to set
     */
    public void setEventMainFrame(EventMainFrame eventMainFrame) {
        this.eventMainFrame = eventMainFrame;
    }
}
