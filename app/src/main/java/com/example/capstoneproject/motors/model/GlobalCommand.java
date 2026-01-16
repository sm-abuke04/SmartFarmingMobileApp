package com.example.capstoneproject.motors.model;

public class GlobalCommand {

    private int command_id;
    private int command_value;
    private String source;
    private Integer actuator_id; // nullable
    private Integer model_actuator_id; // nullable

    // constructor for local use
    public GlobalCommand(int command_value) {
        this.command_value = command_value;
    }

    // Getters and setters
    public int getCommand_id() { return command_id; }
    public void setCommand_id(int command_id) { this.command_id = command_id; }

    public int getCommand_value() { return command_value; }
    public void setCommand_value(int command_value) { this.command_value = command_value; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public Integer getActuator_id() { return actuator_id; }
    public void setActuator_id(Integer actuator_id) { this.actuator_id = actuator_id; }

    public Integer getModel_actuator_id() { return model_actuator_id; }
    public void setModel_actuator_id(Integer model_actuator_id) { this.model_actuator_id = model_actuator_id; }
}
