//package com.example.capstoneproject.api;
//
//import com.example.capstoneproject.Models.CommandRequest;
//import com.example.capstoneproject.Models.LatestCommand;
//import com.example.capstoneproject.api.models.Motor;
//import com.example.capstoneproject.api.models.MotorStatusRequest;
//import com.example.capstoneproject.motors.model.Actuator;
//
//import retrofit2.Call;
//import retrofit2.http.Body;
//import retrofit2.http.GET;
//import retrofit2.http.POST;
//import retrofit2.http.Path;
//
//import java.util.List;
//import retrofit2.Call;
//import retrofit2.http.*;
//
//public interface ActuatorApi {
//
//    @GET("/api/actuator")
//    Call<List<Actuator>> getActuators();
//
//    @POST("/api/actuator/command")
//    Call<LatestCommand> sendActuatorCommand(@Body CommandRequest body);
//}
//












package com.example.capstoneproject.api;

import com.example.capstoneproject.motors.model.Actuator;
import com.example.capstoneproject.motors.model.CommandRequest;
import com.example.capstoneproject.motors.model.LatestCommand;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ActuatorApi {

    @GET("/api/actuator")
    Call<List<Actuator>> getActuators();

    @POST("/api/actuator/command")
    Call<LatestCommand> sendActuatorCommand(
            @Body CommandRequest body
    );
}
