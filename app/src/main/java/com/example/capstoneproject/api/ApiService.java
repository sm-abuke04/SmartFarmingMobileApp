    package com.example.capstoneproject.api;

    import com.example.capstoneproject.api.models.LoginRequest;
    import com.example.capstoneproject.api.models.LoginResponse;
    import com.example.capstoneproject.api.models.Motor;
    import com.example.capstoneproject.api.models.MotorStatusRequest;
    import com.example.capstoneproject.api.models.MotorHistory;
    import com.example.capstoneproject.api.models.AutomationRequest;

    import java.util.List;
    import java.util.Map;

    import retrofit2.Call;
    import retrofit2.http.Body;
    import retrofit2.http.GET;
    import retrofit2.http.POST;
    import retrofit2.http.Path;

    public interface ApiService {

        // Login
        @POST("api/auth/signin")
        Call<LoginResponse> login(@Body LoginRequest loginRequest);

        // Get all motors
        @GET("api/motor") // Adjust to real endpoint
        Call<List<Motor>> getMotors();

        // Get specific motor info
        @GET("api/motor/{id}")
        Call<Motor> getMotor(@Path("id") int motorId);

        // Update motor status (manual or auto)
        @POST("api/motor/{id}/status")
        Call<Void> updateMotorStatus(@Path("id") int motorId, @Body MotorStatusRequest request);

        // Set automation schedule
        @POST("api/motor/{id}/automation")
        Call<Void> setAutomation(@Path("id") int motorId, @Body AutomationRequest request);

        @POST("automation")
        Call<Void> setAutomation(@Body AutomationRequest request);




        // Get motor history
        @GET("api/motor/{id}/history")
        Call<List<MotorHistory>> getMotorHistory(@Path("id") int motorId);
    }
