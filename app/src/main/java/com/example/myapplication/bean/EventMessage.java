package com.example.myapplication.bean;

public class EventMessage {
    private String message;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EventMessage() {
    }

    public EventMessage(String message) {
        this.message = message;
    }

    public EventMessage(int code) {
        this.code = code;
    }
}
