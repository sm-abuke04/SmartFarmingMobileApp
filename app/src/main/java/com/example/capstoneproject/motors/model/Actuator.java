package com.example.capstoneproject.motors.model;

import java.util.ArrayList;
import java.util.List;

public class Actuator {

    private int actuator_id;
    private String actuator_name;
    private String actuator_desc;
    private String actuator_image;
    private String scope;
    private boolean is_deleted;

    private Config Config;
    private List<ModelAssignment> ModelAssignments = new ArrayList<>();
    private Schedule Schedule;
    private List<GlobalCommand> GlobalCommands; // For global commands
    private List<Command> Commands; // For model-specific or standalone commands

    // Getters
    public int getActuatorId() { return actuator_id; }
    public String getActuatorName() { return actuator_name; }
    public String getActuatorDesc() { return actuator_desc; }
    public String getActuatorImage() { return actuator_image; }
    public String getScope() { return scope; }
    public Config getConfig() { return Config; }
    public List<ModelAssignment> getModelAssignments() { return ModelAssignments; }
    public Schedule getSchedule() { return Schedule; }
    public List<Command> getCommands() { return Commands; }
    public List<GlobalCommand> getGlobalCommands() {
        if (GlobalCommands == null) GlobalCommands = new ArrayList<>();
        return GlobalCommands;
    }

    // Setters
    public void setCommands(List<Command> commands) { this.Commands = commands; }
    public void setGlobalCommands(List<GlobalCommand> globalCommands) { this.GlobalCommands = globalCommands; }

    // Get latest model/standalone command
    public LatestCommand getLatestCommand() {
        if (ModelAssignments != null && !ModelAssignments.isEmpty()) {
            List<Command> cmds = ModelAssignments.get(0).getCommands();
            if (cmds != null && !cmds.isEmpty()) {
                return new LatestCommand(cmds.get(0).getCommandValue());
            }
        }
        if (Commands != null && !Commands.isEmpty()) {
            return new LatestCommand(Commands.get(0).getCommandValue());
        }
        return new LatestCommand(0); // default OFF
    }

    // Set latest model/standalone command
    public void setLatestCommand(int value, boolean isModel) {
        if (isModel && ModelAssignments != null && !ModelAssignments.isEmpty()) {
            List<Command> cmds = ModelAssignments.get(0).getCommands();
            if (cmds == null) ModelAssignments.get(0).setCommands(new ArrayList<>());
            ModelAssignments.get(0).getCommands().clear();
            ModelAssignments.get(0).getCommands().add(new Command(value));
        } else {
            if (Commands == null) Commands = new ArrayList<>();
            Commands.clear();
            Commands.add(new Command(value));
        }
    }

    // ====== NEW: Global Command helpers ======
    public void setLatestGlobalCommand(int value) {
        if (GlobalCommands == null) GlobalCommands = new ArrayList<>();
        if (GlobalCommands.isEmpty()) {
            GlobalCommands.add(new GlobalCommand(value));
        } else {
            GlobalCommands.get(0).setCommand_value(value);
        }
    }

    public LatestCommand getLatestGlobalCommand() {
        if (GlobalCommands != null && !GlobalCommands.isEmpty()) {
            return new LatestCommand(GlobalCommands.get(0).getCommand_value());
        }
        return new LatestCommand(0);
    }
}
