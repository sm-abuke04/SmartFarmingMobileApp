//package com.example.capstoneproject.dashboard.models;
//
//import com.google.gson.annotations.SerializedName;
//
//import java.util.List;
//
//public class PlantProfile {
//
//    @SerializedName("Requirements")
//    private List<PlantRequirement> requirements;
//
//    public List<PlantRequirement> getRequirements() {
//        return requirements;
//    }
//}

package com.example.capstoneproject.dashboard.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PlantProfile {

    @SerializedName("plant_name")
    private String plantName;

    @SerializedName("Requirements")
    private List<PlantRequirement> requirements;

    public List<PlantRequirement> getRequirements() {
        return requirements;
    }
}