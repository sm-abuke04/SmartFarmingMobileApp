package com.example.capstoneproject.api;

import android.content.Context;
import android.util.Log;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

import com.example.capstoneproject.motors.model.Actuator;
import com.example.capstoneproject.dashboard.models.Model;
import com.example.capstoneproject.dashboard.models.DashboardResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private ApiService apiService;
    private OkHttpClient okHttpClient;
    private CookieManager cookieManager;

    // Your specific backend URL
    public static final String BASE_URL = "https://uep-smartfarming-backend.onrender.com/";

    private RetrofitClient(Context context) {
        // Initialize CookieManager to handle cookies
        cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        // Create logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        //loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
// Use HEADERS to see cookies

        // Create OkHttp client with longer timeouts for Render.com
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static synchronized RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context.getApplicationContext());
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }

    // Method to clear cookies (for logout)
    public void clearCookies() {
        try {
            if (cookieManager != null && cookieManager.getCookieStore() != null) {
                cookieManager.getCookieStore().removeAll();
                Log.d("RetrofitClient", "Cookies cleared successfully");
            } else {
                Log.e("RetrofitClient", "CookieManager or CookieStore is null");
            }
        } catch (Exception e) {
            Log.e("RetrofitClient", "Error clearing cookies: " + e.getMessage());
        }
    }

public interface DashboardApi {

    // Get all models for spinner
    @GET("/models")
    Call<List<Model>> getModels();

    // Get latest readings for a model
    @GET("/models/{id}/latest")
    Call<DashboardResponse> getLatestReadings(@Path("id") int modelId);

    // Get actuators for a model (global or selected)
    @GET("/actuators")
    Call<List<Actuator>> getActuators(
            @Query("modelId") int modelId,
            @Query("global") boolean isGlobal
    );

    // Update actuator status
    @PUT("/actuators/{id}")
    Call<Void> updateActuatorStatus(
            @Path("id") int id,
            @Query("active") boolean active
    );
}
}