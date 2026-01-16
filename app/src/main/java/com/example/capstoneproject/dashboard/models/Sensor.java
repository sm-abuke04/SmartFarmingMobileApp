//package com.example.capstoneproject.dashboard.models;
//
//import com.google.gson.annotations.SerializedName;
//import java.util.List;
//
//public class Sensor {
//
//    @SerializedName("sensor_id")
//    private int sensorId;
//
//    @SerializedName("sensor_name")
//    private String sensorName;
//
//    @SerializedName("sensor_status")
//    private String sensorStatus;
//
//    @SerializedName("Readings") // <-- this must match the JSON key exactly
//    private List<Reading> readings;
//
//    public int getSensorId() { return sensorId; }
//    public String getSensorName() { return sensorName; }
//    public String getSensorStatus() { return sensorStatus; }
//
//    public List<Reading> getReadings() { return readings; } // <-- this is what fetchSensorReadings() needs
//}






package com.example.capstoneproject.dashboard.models;

import com.google.gson.annotations.SerializedName;

public class Sensor {

    @SerializedName("sensor_id")
    private int sensorId;

    @SerializedName("sensor_name")
    private String sensorName;

    @SerializedName("sensor_desc")
    private String sensorDesc;

    public int getSensorId() { return sensorId; }
    public String getSensorName() { return sensorName; }
    public String getSensorDesc() { return sensorDesc; }
}
