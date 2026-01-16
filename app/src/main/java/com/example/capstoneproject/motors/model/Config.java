package com.example.capstoneproject.motors.model;

public class Config {

    private int config_id;
    private String device_category;
    private int reference_id;
    private int gpio_pin;
    private String control_type;
    private String pin_mode;

    public int getGpioPin() {
        return gpio_pin;
    }

    public String getControlType() {
        return control_type;
    }
}
