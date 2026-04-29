//package com.example.capstoneproject.motors.model;
//
//import java.util.List;
//
//public class ActuatorItem {
//
//    private final Actuator actuator;
//    private final ModelAssignment modelAssignment;
//
//    public ActuatorItem(Actuator actuator, ModelAssignment modelAssignment) {
//        this.actuator = actuator;
//        this.modelAssignment = modelAssignment;
//    }
//
//    public Actuator getActuator() {
//        return actuator;
//    }
//
//    public ModelAssignment getModelAssignment() {
//        return modelAssignment;
//    }
//
//    public boolean isGlobal() {
//        return modelAssignment == null;
//    }
//
//    /**
//     * Returns true if the actuator/control is set to Auto mode
//     * This will be used to disable the toggle switch.
//     */
//    public boolean isAuto() {
//        // 1. Check Model-Specific Assignment first
//        if (modelAssignment != null && modelAssignment.getControlType() != null) {
//            return modelAssignment.getControlType().equalsIgnoreCase("Auto");
//        }
//
//        // 2. Fallback to Global Status if no model assignment exists
//        return actuator.getActuatorStatus() != null &&
//                actuator.getActuatorStatus().equalsIgnoreCase("Auto");
//    }
//
//    /**
//     * Returns the latest command value (0 or 1)
//     * for both Global and Model actuators
//     */
//    public int getLatestCommandValue() {
//        if (isGlobal()) {
//            List<GlobalCommand> cmds = actuator.getGlobalCommands();
//            if (cmds != null && !cmds.isEmpty()) {
//                return cmds.get(cmds.size() - 1).getCommand_value();
//            }
//        } else if (modelAssignment != null) {
//            List<Command> cmds = modelAssignment.getCommands();
//            if (cmds != null && !cmds.isEmpty()) {
//                return cmds.get(cmds.size() - 1).getCommandValue();
//            }
//        }
//        return 0; // default OFF
//    }
//
//    /**
//     * Helper: returns true if the actuator is currently ON
//     */
//    public boolean isOn() {
//        return getLatestCommandValue() == 1;
//    }
//}


package com.example.capstoneproject.motors.model;

import java.util.List;

public class ActuatorItem {
    private final Actuator actuator;
    private final ModelAssignment modelAssignment;

    public ActuatorItem(Actuator actuator, ModelAssignment modelAssignment) {
        this.actuator = actuator;
        this.modelAssignment = modelAssignment;
    }

    public Actuator getActuator() { return actuator; }
    public ModelAssignment getModelAssignment() { return modelAssignment; }
    public boolean isGlobal() { return modelAssignment == null; }

    public boolean isAuto() {
        // Priority 1: Model Specific Assignment
        if (modelAssignment != null) {
            String mode = modelAssignment.getControlType();
            return mode != null && mode.equalsIgnoreCase("Auto");
        }
        // Priority 2: Global Scope
        if (actuator != null && "Global".equalsIgnoreCase(actuator.getScope())) {
            String status = actuator.getActuatorStatus();
            return status != null && status.equalsIgnoreCase("Auto");
        }
        return false;
    }

    // FIX: The Toggle Switch now follows the Auto/Manual mode
    public boolean isOn() {
        return isAuto();
    }

    public int getLatestCommandValue() {
        if (isGlobal()) {
            List<GlobalCommand> cmds = actuator.getGlobalCommands();
            if (cmds != null && !cmds.isEmpty()) return cmds.get(cmds.size() - 1).getCommand_value();
        } else if (modelAssignment != null) {
            List<Command> cmds = modelAssignment.getCommands();
            if (cmds != null && !cmds.isEmpty()) return cmds.get(cmds.size() - 1).getCommandValue();
        }
        return 0;
    }
}