////package com.example.capstoneproject.dashboard.models;
////
////import com.google.gson.annotations.SerializedName;
////import java.util.List;
////
////public class ModelSensor {
////
////    @SerializedName("model_sensor_id")
////    private int modelSensorId;
////
////    @SerializedName("status")
////    private String status;
////
////    @SerializedName("HardwareInfo")
////    private HardwareInfo hardwareInfo;
////
////    @SerializedName("Readings")
////    private List<SensorReading> readings;
////
////    private double latestValue;
////
////    public int getModelSensorId() {
////        return modelSensorId;
////    }
////
////    public String getStatus() {
////        return status;
////    }
////
////    public HardwareInfo getHardwareInfo() {
////        return hardwareInfo;
////    }
////
////    public List<SensorReading> getReadings() {
////        return readings;
////    }
////
////    // JS equivalent: getLatestReading()
////    public void computeLatestValue() {
////        if (readings != null && !readings.isEmpty()) {
////            latestValue = readings.get(0).getReadingValue();
////        }
////    }
////
////    public double getLatestValue() {
////        return latestValue;
////    }
////}
//
//
//
//
//
//package com.example.capstoneproject.dashboard.models;
//
//import com.google.gson.annotations.SerializedName;
//import java.util.List;
//
//public class ModelSensor {
//
//    @SerializedName("model_sensor_id")
//    private int modelSensorId;
//
//    @SerializedName("hydromodel_id")
//    private int hydromodelId;
//
//    @SerializedName("sensor_id")
//    private int sensorId;
//
//    @SerializedName("status")
//    private String status;
//
//    @SerializedName("HardwareInfo")
//    private HardwareInfo hardwareInfo;
//
//    @SerializedName("Readings")
//    private List<Reading> readings;
//
//    public int getModelSensorId() {
//        return modelSensorId;
//    }
//
//    public int getHydromodelId() {
//        return hydromodelId;
//    }
//
//    public int getSensorId() {
//        return sensorId;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public List<Reading> getReadings() {
//        return readings;
//    }
//
//    // ✅ New getter to get sensor name directly
//    public String getSensorName() {
//        return hardwareInfo != null ? hardwareInfo.getSensorName() : "Unknown";
//    }
//
//    // Nested class for HardwareInfo
//    public static class HardwareInfo {
//        @SerializedName("sensor_id")
//        private int sensorId;
//
//        @SerializedName("sensor_name")
//        private String sensorName;
//
//        @SerializedName("sensor_desc")
//        private String sensorDesc;
//
//        public int getSensorId() {
//            return sensorId;
//        }
//
//        public String getSensorName() {
//            return sensorName;
//        }
//
//        public String getSensorDesc() {
//            return sensorDesc;
//        }
//    }
//}








package com.example.capstoneproject.dashboard.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ModelSensor {

    @SerializedName("model_sensor_id")
    private int modelSensorId;

    @SerializedName("hydromodel_id")
    private int hydromodelId;

    @SerializedName("sensor_id")
    private int sensorId;

    @SerializedName("status")
    private String status;

    @SerializedName("HardwareInfo")
    private Sensor hardwareInfo;

    @SerializedName("Readings")
    private List<Reading> readings;

    public int getModelSensorId() { return modelSensorId; }
    public int getHydromodelId() { return hydromodelId; }
    public int getSensorId() { return sensorId; }
    public String getStatus() { return status; }

    // ✅ Accessor to sensor name via HardwareInfo
    public String getSensorName() {
        return hardwareInfo != null ? hardwareInfo.getSensorName() : "Unknown";
    }

    public List<Reading> getReadings() { return readings; }
}
