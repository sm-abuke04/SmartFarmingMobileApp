package com.example.capstoneproject.motors.model;

public class CommandRequest {
    private Integer actuator_id;
    private Integer model_actuator_id;
    private int command_value;
    private String source; // e.g., "Manual"

    public CommandRequest(Integer actuator_id, Integer model_actuator_id, int command_value, String source) {
        this.actuator_id = actuator_id;
        this.model_actuator_id = model_actuator_id;
        this.command_value = command_value;
        this.source = source;
    }

    // Getters & setters if needed
    public int getActuator_id() { return actuator_id; }
    public int getModel_actuator_id() { return model_actuator_id; }
    public int getCommand_value() { return command_value; }
    public String getSource() { return source; }
}
