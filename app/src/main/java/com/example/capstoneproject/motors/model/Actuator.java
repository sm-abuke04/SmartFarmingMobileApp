package com.example.capstoneproject.motors.model;

public class Actuator {

    private int id;
    private String name;
    private boolean isOn;

    public Actuator(int id, String name, boolean isOn) {
        this.id = id;
        this.name = name;
        this.isOn = isOn;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }
}
