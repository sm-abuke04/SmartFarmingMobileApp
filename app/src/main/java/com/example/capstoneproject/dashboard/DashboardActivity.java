package com.example.capstoneproject.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstoneproject.AboutUsActivity;
import com.example.capstoneproject.CameraActivity;
import com.example.capstoneproject.R;
import com.example.capstoneproject.api.ApiClient;
import com.example.capstoneproject.api.DashboardApi;
import com.example.capstoneproject.dashboard.models.Model;
import com.example.capstoneproject.dashboard.models.SensorReading;
import com.example.capstoneproject.motors.MotorControlsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";

    // Navigation
    LinearLayout navCamera, navControls, btnSettings;

    // Sensor UI
    TextView tempValue, tempStatus;
    TextView humidityValue, humidityStatus;
    TextView phValue, phStatus;
    TextView tdsValue, tdsStatus;
    TextView lightValue, lightStatus;

    // Spinner for selecting model
    private Spinner modelSpinner;
    private List<Model> modelsList = new ArrayList<>();
    private int selectedModelId = 1; // default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Navigation buttons
        navCamera = findViewById(R.id.navCamera);
        navControls = findViewById(R.id.navControls);
        btnSettings = findViewById(R.id.btnSettings);

        navCamera.setOnClickListener(v ->
                startActivity(new Intent(this, CameraActivity.class)));

        navControls.setOnClickListener(v ->
                startActivity(new Intent(this, MotorControlsActivity.class)));

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, AboutUsActivity.class)));

        // Spinner
        modelSpinner = findViewById(R.id.spinnerModels);

        // Bind sensor cards
        bindSensorCard(R.id.cardTemperature, "Temperature");
        bindSensorCard(R.id.cardHumidity, "Humidity");
        bindSensorCard(R.id.cardPh, "pH Level");
        bindSensorCard(R.id.cardTds, "TDS");
        bindSensorCard(R.id.cardLight, "Light Intensity");

        // Setup spinner adapter
        ArrayAdapter<Model> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                modelsList
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(spinnerAdapter);

        // Spinner selection listener
        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!modelsList.isEmpty()) {
                    selectedModelId = modelsList.get(position).getId();
                    fetchSensorReadings(selectedModelId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Load models from API
        loadModels(spinnerAdapter);
    }

    private void bindSensorCard(int cardId, String title) {
        View card = findViewById(cardId);
        if (card == null) return;

        TextView tvTitle = card.findViewById(R.id.tvSensorTitle);
        TextView tvValue = card.findViewById(R.id.tvSensorValue);
        TextView tvStatus = card.findViewById(R.id.tvSensorStatus);

        tvTitle.setText(title);

        if (cardId == R.id.cardTemperature) {
            tempValue = tvValue;
            tempStatus = tvStatus;
        } else if (cardId == R.id.cardHumidity) {
            humidityValue = tvValue;
            humidityStatus = tvStatus;
        } else if (cardId == R.id.cardPh) {
            phValue = tvValue;
            phStatus = tvStatus;
        } else if (cardId == R.id.cardTds) {
            tdsValue = tvValue;
            tdsStatus = tvStatus;
        } else if (cardId == R.id.cardLight) {
            lightValue = tvValue;
            lightStatus = tvStatus;
        }
    }

    private void loadModels(ArrayAdapter<Model> spinnerAdapter) {
        DashboardApi api = ApiClient.getClient(this).create(DashboardApi.class);
        api.getModels().enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    modelsList.clear();
                    modelsList.addAll(response.body());
                    spinnerAdapter.notifyDataSetChanged();

                    // Select first model by default
                    if (!modelsList.isEmpty()) {
                        selectedModelId = modelsList.get(0).getId();
                        fetchSensorReadings(selectedModelId);
                    }
                } else {
                    Log.e(TAG, "Failed to load models");
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Log.e(TAG, "API Error loading models", t);
            }
        });
    }

    private void fetchSensorReadings(int modelId) {
        DashboardApi api = ApiClient.getClient(this).create(DashboardApi.class);

        api.getLatestReadings(modelId).enqueue(new Callback<SensorReading>() {
            @Override
            public void onResponse(Call<SensorReading> call, Response<SensorReading> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Log.e(TAG, "Failed to fetch sensor data");
                    return;
                }

                SensorReading data = response.body();

                updateCard(tempValue, tempStatus,
                        data.getTemperature() + "°C",
                        getStatus(data.getTemperature(), "temperature"));

                updateCard(humidityValue, humidityStatus,
                        data.getHumidity() + "%",
                        getStatus(data.getHumidity(), "humidity"));

                updateCard(phValue, phStatus,
                        String.valueOf(data.getPh()),
                        getStatus(data.getPh(), "ph"));

                updateCard(tdsValue, tdsStatus,
                        data.getTds() + " ppm",
                        getStatus(data.getTds(), "tds"));

                updateCard(lightValue, lightStatus,
                        data.getLight() + "%",
                        getStatus(data.getLight(), "light"));
            }

            @Override
            public void onFailure(Call<SensorReading> call, Throwable t) {
                Log.e(TAG, "API Error", t);
            }
        });
    }

    private void updateCard(TextView value, TextView status, String v, String s) {
        if (value != null) value.setText(v);
        if (status != null) status.setText(s);
    }

    private String getStatus(float value, String type) {
        switch (type) {
            case "temperature":
                return value >= 20 && value <= 28 ? "Optimal" : "Alert";
            case "humidity":
                return value >= 60 && value <= 75 ? "Good" : "Alert";
            case "ph":
                return value >= 5.5 && value <= 7.0 ? "Stable" : "Warning";
            case "tds":
                return value >= 400 && value <= 800 ? "Good" : "Alert";
            case "light":
                return value >= 70 ? "Optimal" : "Low";
            default:
                return "-";
        }
    }
}
