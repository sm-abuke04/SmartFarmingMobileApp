package com.example.capstoneproject.motors.model;

public class LatestCommand {
    private int command_id;
    private int command_value;
    private String source;
    private int model_actuator_id;
    private String command_date;

    // Default constructor (needed for Retrofit)
    public LatestCommand() {}

    // Constructor to create manually
    public LatestCommand(int value) {
        this.command_value = value;
    }

    public int getCommand_id() { return command_id; }
    public int getCommand_value() { return command_value; }
    public String getSource() { return source; }
    public int getModel_actuator_id() { return model_actuator_id; }
    public String getCommand_date() { return command_date; }

    public boolean isOn() { return command_value != 0; }
}
