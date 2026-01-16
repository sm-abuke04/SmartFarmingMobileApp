//package com.example.capstoneproject.motors.model;
//
//import java.util.List;
//
//public class ModelAssignment {
//
//    private int model_actuator_id;
//    private int actuator_id;
//    private String status;
//    private int hydromodel_id;
//
//    private HydroModel HydroModel;
//    private Config Config;
//    private List<Command> Commands;
//    private Schedule Schedule;
//
//    public int getModelActuatorId() {
//        return model_actuator_id;
//    }
//
//    public List<Command> getCommands() {
//        return Commands;
//    }
//
//    public HydroModel getHydroModel() {
//        return HydroModel;
//    }
//}



package com.example.capstoneproject.motors.model;

import java.util.ArrayList;
import java.util.List;

public class ModelAssignment {

    private int model_actuator_id;
    private int actuator_id;
    private String status;
    private int hydromodel_id;

    private HydroModel HydroModel;
    private Config Config;
    private List<Command> Commands = new ArrayList<>();
    private Schedule Schedule;

    public int getModelActuatorId() { return model_actuator_id; }
//    public List<Command> getCommands() { return Commands; }
    public HydroModel getHydroModel() { return HydroModel; }

    public List<Command> getCommands() {
        return Commands;
    }

    public void setCommands(List<Command> commands) {
        this.Commands = commands;
    }

}
