package com.example.capstoneproject.motors.model;

public class MotorHistory {

    private int motorId;
    private String motorName;
    private String status;
    private String dateTime;

    // Constructor
    public MotorHistory(int motorId, String motorName, String status, String dateTime) {
        this.motorId = motorId;
        this.motorName = motorName;
        this.status = status;
        this.dateTime = dateTime;
    }

    // Getters
    public int getMotorId() {
        return motorId;
    }

    public String getMotorName() {
        return motorName;
    }

    public String getStatus() {
        return status;
    }

    public String getDateTime() {
        return dateTime;
    }
}
