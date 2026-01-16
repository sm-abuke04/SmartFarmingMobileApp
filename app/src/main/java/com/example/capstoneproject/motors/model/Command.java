package com.example.capstoneproject.motors.model;

public class Command {
    private int command_id;
    private int command_value;
    private String source;
    private String command_date;
    private int model_actuator_id;
    private Integer actuator_id; // nullable

    // Default constructor
    public Command() {}

    // Constructor to create command manually
    public Command(int command_value) {
        this.command_value = command_value;
    }

    public int getCommandValue() { return command_value; }
    public String getSource() { return source; }
    public int getCommandId() { return command_id; }
}
