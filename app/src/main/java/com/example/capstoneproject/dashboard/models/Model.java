//package com.example.capstoneproject.dashboard.models;
//
//import com.google.gson.annotations.SerializedName;
//import java.util.List;
//
//public class Model {
//
//    @SerializedName("hydromodel_id")
//    private int hydroModelId;
//
//    @SerializedName("hydromodel_name")
//    private String hydroModelName;
//
//    @SerializedName("ModelSensors")
//    private List<ModelSensor> modelSensors;
//
//    @SerializedName("ModelActuators")
//    private List<ModelActuator> modelActuators;
//
//    @SerializedName("PlantProfiles")
//    private PlantProfile plantProfile;
//
//    /**
//     * Returns the active PlantRequirement.
//     * If none exists, returns a default requirement to prevent nulls.
//     */
//    public PlantRequirement getActiveRequirement() {
//        if (plantProfile != null &&
//                plantProfile.getRequirements() != null &&
//                !plantProfile.getRequirements().isEmpty()) {
//
//            return plantProfile.getRequirements().get(0);
//        }
//
//        // Return default safe requirement
//        return new PlantRequirement(); // Uses default values
//    }
//
//    public List<ModelActuator> getModelActuators() {
//        return modelActuators;
//    }
//
//    public List<ModelSensor> getModelSensors() {
//        return modelSensors;
//    }
//
//    public int getHydromodelId() {
//        return hydroModelId;
//    }
//
//    @Override
//    public String toString() {
//        return hydroModelName;
//    }
//}
package com.example.capstoneproject.dashboard.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Model {
        @SerializedName("hydromodel_id")
        private int hydroModelId;

        @SerializedName("hydromodel_name")
        private String hydroModelName;

        // Matches the "PlantProfiles": { ... } object in your JSON
        @SerializedName("PlantProfiles")
        private PlantProfile plantProfile;

        public PlantRequirement getActiveRequirement() {
            // Ensure plantProfile exists AND has the inner Requirements list
            if (plantProfile != null &&
                    plantProfile.getRequirements() != null &&
                    !plantProfile.getRequirements().isEmpty()) {

                // Your JSON shows Requirements is an array [ { ... } ]
                return plantProfile.getRequirements().get(0);
            }
            return new PlantRequirement(); // Returns the safe 17-32 defaults
        }

    @SerializedName("ModelSensors")
    private List<ModelSensor> modelSensors;

    @SerializedName("ModelActuators")
    private List<ModelActuator> modelActuators;

    public List<ModelActuator> getModelActuators() { return modelActuators; }
    public List<ModelSensor> getModelSensors() { return modelSensors; }
    public int getHydromodelId() { return hydroModelId; }

    @Override
    public String toString() { return hydroModelName; }
}