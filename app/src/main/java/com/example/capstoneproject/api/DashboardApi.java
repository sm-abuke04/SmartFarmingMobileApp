package com.example.capstoneproject.api;

import com.example.capstoneproject.dashboard.models.Model;
import com.example.capstoneproject.dashboard.models.ModelSensor;
import com.example.capstoneproject.dashboard.models.Reading;
import com.example.capstoneproject.dashboard.models.SensorReading;
import com.example.capstoneproject.dashboard.models.WaterLevel;
import com.example.capstoneproject.user_profile.UserProfileResponse;
import com.example.capstoneproject.user_profile.UserProfileResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DashboardApi {

    @GET("api/model")
    Call<List<Model>> getModels();

    @GET("api/model/{id}")
    Call<Model> getHydroModelId(@Path("id") int modelId);

    @GET("api/model-sensor/model/{id}")
    Call<List<ModelSensor>> getModelSensors(@Path("id") int modelId);

    @GET("api/readings/sensor/{modelSensorId}")
    Call<List<Reading>> getReadingsByModelSensor(
            @Path("modelSensorId") int modelSensorId
    );

    @GET("api/readings/sensor/{sensorId}")
    Call<List<SensorReading>> getSensorReadings(
            @Path("sensorId") int sensorId,
            @Query("limit") int limit
    );

    @GET("api/waterlevel/model/{modelId}") // Adjust endpoint according to your backend
    Call<WaterLevel> getWaterLevelByModel(@Path("modelId") int modelId);

    @GET("api/user/{id}")
    Call<com.example.capstoneproject.user_profile.UserProfileResponse> getUserProfile(@Path("id") String userId);
}
