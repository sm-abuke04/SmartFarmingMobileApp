package com.example.capstoneproject.api;

import com.example.capstoneproject.dashboard.models.Model;
import com.example.capstoneproject.dashboard.models.ModelSensor;
import com.example.capstoneproject.dashboard.models.Reading;
import com.example.capstoneproject.dashboard.models.Sensor;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DashboardApi {
    // MODELS
    @GET("api/model")
    Call<List<Model>> getModels();

    // MODEL SENSORS (THIS IS THE IMPORTANT ONE)
    @GET("api/model-sensor/model/{id}")
    Call<List<ModelSensor>> getModelSensors(@Path("id") int modelId);

    // READINGS BY MODEL_SENSOR_ID
    @GET("api/readings/sensor/{modelSensorId}")
    Call<List<Reading>> getReadingsByModelSensor(
            @Path("modelSensorId") int modelSensorId
    );
}
