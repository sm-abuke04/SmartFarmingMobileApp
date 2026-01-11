package com.example.capstoneproject.api;

import com.example.capstoneproject.motors.model.Actuator;
import com.example.capstoneproject.dashboard.models.Model;
import com.example.capstoneproject.dashboard.models.SensorReading;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DashboardApi {

//    // Actuators
//    @GET("/actuators/global")
//    Call<List<Actuator>> getGlobalActuators();
//
//    @GET("/actuators/selected")
//    Call<List<Actuator>> getSelectedActuators();

    @GET("/actuators/model/1")
    Call<List<Actuator>> getGlobalActuators();

    @GET("/actuators/model/1")
    Call<List<Actuator>> getSelectedActuators();

    // Models for Dashboard
    @GET("/models")
    Call<List<Model>> getModels();

    // Latest sensor reading by model
    @GET("/models/{id}/latest")
    Call<SensorReading> getLatestReadings(@Path("id") int modelId);
    @GET("actuators")
    Call<List<Actuator>> getActuators(@Query("global") boolean isGlobal);

    @PUT("actuator/{id}/update")
    Call<Void> updateActuatorStatus(@Path("id") int id, @Query("active") boolean status);

}
