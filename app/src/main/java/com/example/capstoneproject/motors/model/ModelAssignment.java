////package com.example.capstoneproject.motors.model;
////
////import java.util.ArrayList;
////import java.util.List;
////import com.google.gson.annotations.SerializedName;
////
////public class ModelAssignment {
////
////    private int model_actuator_id;
////    private int actuator_id;
////    private String status;
////    private int hydromodel_id;
////
////    private HydroModel HydroModel;
////    private Config Config;
////    private List<Command> Commands = new ArrayList<>();
////    private Schedule Schedule;
////
////    private String controlType;
////    public String getControlType() {
////        return controlType;
////
////    }
////
////
////    public int getModelActuatorId() { return model_actuator_id; }
////    public List<Command> getCommands() { return Commands; }
////    public HydroModel getHydroModel() { return HydroModel; }
////
////    public List<Command> getCommands() {
////        return Commands;
////    }
////
////    public void setCommands(List<Command> commands) {
////        this.Commands = commands;
////    }
////
////}
//package com.example.capstoneproject.motors.model;
//
//import com.google.gson.annotations.SerializedName;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ModelAssignment {
//
//    private int model_actuator_id;
//    private int actuator_id;
//    private String status;
//    private int hydromodel_id;
//
//    // Use @SerializedName to match the underscore naming in your JSON API
//    @SerializedName("control_type")
//    private String controlType;
//
//    @SerializedName("HydroModel")
//    private HydroModel HydroModel;
//
//    @SerializedName("Config")
//    private Config Config;
//
//    @SerializedName("Commands")
//    private List<Command> Commands = new ArrayList<>();
//
//    @SerializedName("Schedule")
//    private Schedule Schedule;
//
//    // --- GETTERS AND SETTERS ---
//
//    public String getControlType() {
//        return controlType;
//    }
//
//    public void setControlType(String controlType) {
//        this.controlType = controlType;
//    }
//
//    public int getModelActuatorId() {
//        return model_actuator_id;
//    }
//
//    public void setModelActuatorId(int model_actuator_id) {
//        this.model_actuator_id = model_actuator_id;
//    }
//
//    public int getActuatorId() {
//        return actuator_id;
//    }
//
//    public void setActuatorId(int actuator_id) {
//        this.actuator_id = actuator_id;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public int getHydromodelId() {
//        return hydromodel_id;
//    }
//
//    public void setHydromodelId(int hydromodel_id) {
//        this.hydromodel_id = hydromodel_id;
//    }
//
//    public HydroModel getHydroModel() {
//        return HydroModel;
//    }
//
//    public void setHydroModel(HydroModel hydroModel) {
//        this.HydroModel = hydroModel;
//    }
//
//    public List<Command> getCommands() {
//        return Commands;
//    }
//
//    public void setCommands(List<Command> commands) {
//        this.Commands = commands;
//    }
//
//    public Config getConfig() {
//        return Config;
//    }
//
//    public void setConfig(Config config) {
//        this.Config = config;
//    }
//
//    public Schedule getSchedule() {
//        return Schedule;
//    }
//
//    public void setSchedule(Schedule schedule) {
//        this.Schedule = schedule;
//    }
//}

package com.example.capstoneproject.motors.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class ModelAssignment {

    private int model_actuator_id;
    private int actuator_id;
    private String status;
    private int hydromodel_id;

    @SerializedName("control_type")
    private String controlType;

    @SerializedName("HydroModel")
    private HydroModel HydroModel;

    @SerializedName("Config")
    private Config Config;

    @SerializedName("Commands")
    private List<Command> Commands = new ArrayList<>();

    @SerializedName("Schedule")
    private Schedule Schedule;

    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    public int getModelActuatorId() { return model_actuator_id; }
    public void setModelActuatorId(int id) { this.model_actuator_id = id; }

    public HydroModel getHydroModel() { return HydroModel; }
    public void setHydroModel(HydroModel hydroModel) { this.HydroModel = hydroModel; }

    public List<Command> getCommands() { return Commands; }
    public void setCommands(List<Command> commands) { this.Commands = commands; }

    public Config getConfig() { return Config; }
    public void setConfig(Config config) { this.Config = config; }

    public Schedule getSchedule() { return Schedule; }
    public void setSchedule(Schedule schedule) { this.Schedule = schedule; }
}