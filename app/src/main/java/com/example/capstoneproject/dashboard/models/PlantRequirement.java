//package com.example.capstoneproject.dashboard.models;
//
//import com.google.gson.annotations.SerializedName;
//
//public class PlantRequirement {
//
//    // Temperature (°C)
//    @SerializedName("min_temperature")
//    private final double minTemp;
//    @SerializedName("max_temperature")
//    private final double maxTemp;
//
//    // pH level
//    @SerializedName("min_ph")
//    private final double minPh;
//    @SerializedName("max_ph")
//    private final double maxPh;
//
//    // Light intensity (lux)
//    @SerializedName("min_lux")
//    private final double minLux;
//    @SerializedName("max_lux")
//    private final double maxLux;
//
//    // TDS / PPM
//    @SerializedName("min_ppm")
//    private final double minPpm;
//    @SerializedName("max_ppm")
//    private final double maxPpm;
//
//    // Humidity (%)
//    @SerializedName("min_humidity")
//    private final double minHumidity;
//    @SerializedName("max_humidity")
//    private final double maxHumidity;
//
//    // ---------------- CONSTRUCTORS ----------------
//
//    // Default constructor for Gson
//    public PlantRequirement() {
//        this.minTemp = 17;
//        this.maxTemp = 32;
//        this.minPh = 4.5;
//        this.maxPh = 6.5;
//        this.minLux = 200;
//        this.maxLux = 700;
//        this.minPpm = 0;
//        this.maxPpm = 800;
//        this.minHumidity = 30;
//        this.maxHumidity = 70;
//    }
//
//    // Full constructor for internal default or test use
//    public PlantRequirement(double minTemp, double maxTemp,
//                            double minPh, double maxPh,
//                            double minLux, double maxLux,
//                            double minPpm, double maxPpm,
//                            double minHumidity, double maxHumidity) {
//        this.minTemp = minTemp;
//        this.maxTemp = maxTemp;
//        this.minPh = minPh;
//        this.maxPh = maxPh;
//        this.minLux = minLux;
//        this.maxLux = maxLux;
//        this.minPpm = minPpm;
//        this.maxPpm = maxPpm;
//        this.minHumidity = minHumidity;
//        this.maxHumidity = maxHumidity;
//    }
//
//    // ---------------- GETTERS ----------------
//    public double getMinTemp() { return minTemp; }
//    public double getMaxTemp() { return maxTemp; }
//
//    public double getMinPh() { return minPh; }
//    public double getMaxPh() { return maxPh; }
//
//    public double getMinLux() { return minLux; }
//    public double getMaxLux() { return maxLux; }
//
//    public double getMinPpm() { return minPpm; }
//    public double getMaxPpm() { return maxPpm; }
//
//    public double getMinHumidity() { return minHumidity; }
//    public double getMaxHumidity() { return maxHumidity; }
//}

package com.example.capstoneproject.dashboard.models;

import com.google.gson.annotations.SerializedName;

public class PlantRequirement {
    @SerializedName("min_temperature")
    private double minTemp;
    @SerializedName("max_temperature")
    private double maxTemp;

    @SerializedName("min_ph")
    private double minPh;
    @SerializedName("max_ph")
    private double maxPh;

    @SerializedName("min_lux")
    private double minLux;
    @SerializedName("max_lux")
    private double maxLux;

    @SerializedName("min_ppm")
    private double minPpm;
    @SerializedName("max_ppm")
    private double maxPpm;

    @SerializedName("min_humidity")
    private double minHumidity;
    @SerializedName("max_humidity")
    private double maxHumidity;

    public PlantRequirement() {
        // Safe fallbacks
        this.minTemp = 17; this.maxTemp = 32;
        this.minPh = 4.5; this.maxPh = 6.5;
        this.minLux = 200; this.maxLux = 700;
        this.minPpm = 0; this.maxPpm = 800;
        this.minHumidity = 30; this.maxHumidity = 70;
    }

    public double getMinTemp() { return minTemp; }
    public double getMaxTemp() { return maxTemp; }
    public double getMinPh() { return minPh; }
    public double getMaxPh() { return maxPh; }
    public double getMinLux() { return minLux; }
    public double getMaxLux() { return maxLux; }
    public double getMinPpm() { return minPpm; }
    public double getMaxPpm() { return maxPpm; }
    public double getMinHumidity() { return minHumidity; }
    public double getMaxHumidity() { return maxHumidity; }
}