package com.example.capstoneproject.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.capstoneproject.SharedPrefManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneproject.AboutUsActivity;
import com.example.capstoneproject.CameraActivity;
import com.example.capstoneproject.R;
import com.example.capstoneproject.UserProfileActivity;
import com.example.capstoneproject.api.ApiClient;
import com.example.capstoneproject.api.DashboardApi;
import com.example.capstoneproject.dashboard.models.Model;
import com.example.capstoneproject.dashboard.models.ModelActuator;
import com.example.capstoneproject.dashboard.models.ModelSensor;
import com.example.capstoneproject.dashboard.models.PlantRequirement;
import com.example.capstoneproject.dashboard.models.Reading;
import com.example.capstoneproject.dashboard.models.SensorReading;
import com.example.capstoneproject.dashboard.models.WaterLevel;
import com.example.capstoneproject.motors.MotorControlsActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.res.ColorStateList;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import androidx.core.app.NotificationCompat;
import java.util.HashMap;
import java.util.Map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";
    private static final int REFRESH_INTERVAL = 10000;

    private LinearLayout sensorCardContainer;
    private Spinner modelSpinner;
    private LineChart envLineChart;
    private DashboardApi api;

    private PieChart waterLevelChart;
    private TextView tvWaterLevelValue;

    private final List<Model> models = new ArrayList<>();
    private int selectedModelId = -1;

    private final Map<String, ImageView> sensorIcons = new HashMap<>();
    private final Map<String, TextView[]> sensorViews = new HashMap<>();
    private final Handler handler = new Handler();

    private TextView tvUserRole;
    private SharedPrefManager sharedPrefManager;

    private com.google.android.material.chip.ChipGroup cgSensors, cgActuators;

    private final Map<String, Long> lastAlertTime = new HashMap<>();
    private static final long ALERT_COOLDOWN = 2 * 60 * 1000; // 2 minutes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        sensorCardContainer = findViewById(R.id.sensorCardContainer);
        modelSpinner = findViewById(R.id.spinnerModels);
        envLineChart = findViewById(R.id.envLineChart);

        api = ApiClient.getClient(this).create(DashboardApi.class);

        setupLineChart();
        setupBottomNavigation();
        setupSpinner();

        waterLevelChart = findViewById(R.id.waterLevelDonutChart);
        tvWaterLevelValue = findViewById(R.id.tvWaterLevelValue);

        sharedPrefManager = SharedPrefManager.getInstance(this);
        tvUserRole = findViewById(R.id.tvUserRole);
        String role = sharedPrefManager.getUserRole();
        if (role != null && !role.isEmpty()) {
            tvUserRole.setText(role);
        } else {
            tvUserRole.setText("Unknown Role");
        }

        cgSensors = findViewById(R.id.cgSensors);
        cgActuators = findViewById(R.id.cgActuators);

        ImageButton btnUserProfile = findViewById(R.id.btnUserProfile);
        btnUserProfile.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        setupWaterLevelChart();

        requestNotificationPermissionIfNeeded();

        // Start auto-refresh loop
        startAutoRefresh();
    }

    private void requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1001
                );
            }
        }
    }


    private PlantRequirement getActiveRequirement() {
        Model model = models.get(modelSpinner.getSelectedItemPosition());
        return model.getActiveRequirement();
    }

//    private boolean isOutOfRange(String key, double value) {
//        PlantRequirement req = getActiveRequirement();
//        if (req == null) return false;
//
//        switch (key) {
//            case "temperature":
//                return value < req.getMinTemp() || value > req.getMaxTemp();
//            case "humidity":
//                return value < req.getMinHumidity() || value > req.getMaxHumidity();
//            case "ph":
//                return value < req.getMinPh() || value > req.getMaxPh();
//            case "tds":
//                return value < req.getMinPpm() || value > req.getMaxPpm();
//            case "light":
//                return value < req.getMinLux() || value > req.getMaxLux();
//        }
//        return false;
//    }

    private boolean isOutOfRange(String key, double value) {
        PlantRequirement req = getActiveRequirement(); // This uses the spinner selection
        if (req == null) return false;

        switch (key) {
            case "temperature": return value < req.getMinTemp() || value > req.getMaxTemp();
            case "humidity":    return value < req.getMinHumidity() || value > req.getMaxHumidity();
            case "ph":          return value < req.getMinPh() || value > req.getMaxPh();
            case "tds":         return value < req.getMinPpm() || value > req.getMaxPpm();
            case "light":       return value < req.getMinLux() || value > req.getMaxLux();
            default:            return false;
        }
    }

    private void sendLocalAlert(String title, String message) {
        String channelId = "sensor_alerts";
        NotificationManager nm =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Sensor Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );
            nm.createNotificationChannel(channel);
        }

        Notification notification =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_warning)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .build();

        nm.notify((int) System.currentTimeMillis(), notification);
    }

    // ---------------------- LINE CHART ----------------------
    private void setupLineChart() {
        envLineChart.getDescription().setEnabled(false);
        envLineChart.setTouchEnabled(true);
        envLineChart.setDrawGridBackground(false);
        envLineChart.setNoDataText("No data available");
        envLineChart.setNoDataTextColor(Color.parseColor("#757575"));

        XAxis xAxis = envLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        YAxis leftAxis = envLineChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        envLineChart.getAxisRight().setEnabled(false);
    }

    // ---------------------- SPINNER ----------------------
    private void setupSpinner() {
        ArrayAdapter<Model> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, models);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(adapter);

        modelSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                Model selectedModel = models.get(position); // Get the full model object
                selectedModelId = selectedModel.getHydromodelId();

                fetchModelSensors(selectedModelId);
                fetchWaterLevel(selectedModelId);

                // Update the new Hardware Card
                updateHardwareConfiguration(selectedModel);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        loadModels(adapter);
    }

    private void updateHardwareConfiguration(Model model) {

        if (models == null || models.isEmpty()) return;

        cgSensors.removeAllViews();
        cgActuators.removeAllViews();

        // Add Sensors (Greenish theme)
        if (model.getModelSensors() != null) {
            for (ModelSensor sensor : model.getModelSensors()) {
                if (sensor.getHardwareInfo() != null) {
                    addChipToGroup(cgSensors, sensor.getHardwareInfo().getSensorName(), "#E8F5E9", "#2E7D32");
                }
            }
        }

        // Add Actuators (Blueish theme)
        if (model.getModelActuators() != null) {
            for (ModelActuator actuator : model.getModelActuators()) {
                if (actuator.getHardwareInfo() != null) {
                    addChipToGroup(cgActuators, actuator.getHardwareInfo().getActuatorName(), "#E3F2FD", "#1565C0");
                }
            }
        }
    }

//    private void addChipToGroup(com.google.android.material.chip.ChipGroup group, String text, String bgColor, String textColor) {
//        com.google.android.material.chip.Chip chip = new com.google.android.material.chip.Chip(this);
//        chip.setText(text);
//        chip.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(Color.parseColor(bgColor)));
//        chip.setTextColor(Color.parseColor(textColor));
//        chip.setChipStrokeWidth(0f); // Clean, flat look
//        group.addView(chip);
//    }

    private void addChipToGroup(ChipGroup group, String text, String bgColor, String textColor) {

        Chip chip = new Chip(this);

        chip.setText(text);
        chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(bgColor)));
        chip.setTextColor(Color.parseColor(textColor));
        chip.setCheckable(false);
        chip.setClickable(false);
        chip.setChipStrokeWidth(0f);

        group.addView(chip);
    }

    private void loadModels(ArrayAdapter<Model> adapter) {
        api.getModels().enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    models.clear();
                    models.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    // If a model is already selected, make sure to refresh data
                    if (selectedModelId != -1) {
                        fetchModelSensors(selectedModelId);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Log.e("DashboardActivity", "Model load failed", t);

                runOnUiThread(() -> {
                    Toast.makeText(DashboardActivity.this,
                            "Server is unreachable. Using cached values.",
                            Toast.LENGTH_LONG).show();
                });
            }

        });
    }


    // ---------------------- SENSOR CARDS ----------------------
    private void createSensorCards(List<ModelSensor> sensors) {
        sensorCardContainer.removeAllViews();
        sensorViews.clear();
        sensorIcons.clear();

        for (ModelSensor ms : sensors) {
            String name = ms.getSensorName().toLowerCase();

            if (name.contains("dht")) {
                addCard("temperature", "Temperature", R.drawable.ic_temp);
                addCard("humidity", "Humidity", R.drawable.ic_humidity);
            } else if (name.contains("ph")) {
                addCard("ph", "pH Level", R.drawable.ic_ph);
            } else if (name.contains("tds")) {
                addCard("tds", "Water TDS", R.drawable.ic_tds);
            } else if (name.contains("light") || name.contains("lux") || name.contains("ldr") || name.contains("photo")) {
                addCard("light", "Light Intensity", R.drawable.ic_light);
            }
        }
    }

    private void addCard(String key, String title, int iconRes) {
        View card = getLayoutInflater().inflate(R.layout.card_sensor_grid, sensorCardContainer, false);
        TextView tvValue = card.findViewById(R.id.tvSensorValue);
        TextView tvTitle = card.findViewById(R.id.tvSensorTitle);
        TextView tvOptimal = card.findViewById(R.id.tvOptimalValue);
        ImageView ivIcon = card.findViewById(R.id.ivSensorIcon);

        tvValue.setText("--");
        tvTitle.setText(title.toUpperCase());
        tvOptimal.setText("--");
        ivIcon.setImageResource(iconRes);

        sensorViews.put(key, new TextView[]{tvValue, tvTitle, tvOptimal});
        sensorIcons.put(key, ivIcon);

        sensorCardContainer.addView(card);
    }

    private void fetchModelSensors(int modelId) {
        api.getModelSensors(modelId).enqueue(new Callback<List<ModelSensor>>() {
            @Override
            public void onResponse(Call<List<ModelSensor>> call, Response<List<ModelSensor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ModelSensor> sensors = response.body();
                    createSensorCards(sensors);

                    if (sensors.isEmpty()) {
                        // No sensors, show no data
                        updateChartUI(new ArrayList<>(), new ArrayList<>());
                        return;
                    }

                    // Fetch latest readings for each sensor
                    for (ModelSensor ms : sensors) fetchLatestReading(ms);

                    // Load chart data
                    loadAllSensorCharts(sensors);
                } else {
                    updateChartUI(new ArrayList<>(), new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<ModelSensor>> call, Throwable t) {
                Log.e(TAG, "Sensor load failed", t);
                updateChartUI(new ArrayList<>(), new ArrayList<>());
            }
        });
    }

//    private void fetchLatestReading(ModelSensor ms) {
//        int pos = modelSpinner.getSelectedItemPosition();
//        if (pos == -1) return;
//
//        Model currentModel = models.get(pos);
//        PlantRequirement req = currentModel.getActiveRequirement(); // guaranteed non-null
//
//        api.getReadingsByModelSensor(ms.getModelSensorId())
//                .enqueue(new Callback<List<Reading>>() {
//                    @Override
//                    public void onResponse(Call<List<Reading>> call, Response<List<Reading>> response) {
//                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
//                            List<Reading> readings = response.body();
//                            String name = ms.getSensorName().toLowerCase();
//
//                            switch (name) {
//                                case "dht":
//                                case "temp":
//                                    updateCard("temperature", readings.get(0).getReadingValue(), "Temperature", "°C",
//                                            String.format("%.0f-%.0f", req.getMinTemp(), req.getMaxTemp()));
//
//                                    if (readings.size() > 1) {
//                                        updateCard("humidity", readings.get(1).getReadingValue(), "Humidity", "%",
//                                                String.format("%.0f-%.0f", req.getMinHumidity(), req.getMaxHumidity()));
//                                    }
//                                    break;
//
//                                case "ph":
//                                    updateCard("ph", readings.get(0).getReadingValue(), "pH Level", "",
//                                            String.format("%.1f-%.1f", req.getMinPh(), req.getMaxPh()));
//                                    break;
//
//                                case "tds":
//                                    updateCard("tds", readings.get(0).getReadingValue(), "Water TDS", " ppm",
//                                            String.format("%.0f-%.0f", req.getMinPpm(), req.getMaxPpm()));
//                                    break;
//
//                                case "lux":
//                                case "light":
//                                case "ldr":
//                                case "photo":
//                                    updateCard("light", readings.get(0).getReadingValue(), "Light Intensity", " lx",
//                                            String.format("%.0f-%.0f", req.getMinLux(), req.getMaxLux()));
//                                    break;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Reading>> call, Throwable t) {
//                        Log.e(TAG, "Fetch failed for sensor " + ms.getSensorName(), t);
//                    }
//                });
//    }


//    private void fetchLatestReading(ModelSensor ms) {
//        int pos = modelSpinner.getSelectedItemPosition();
//        if (pos == -1) return;
//        Model currentModel = models.get(pos);
//        PlantRequirement req = currentModel.getActiveRequirement();
//
//        api.getReadingsByModelSensor(ms.getModelSensorId())
//                .enqueue(new Callback<List<Reading>>() {
//                    @Override
//                    public void onResponse(Call<List<Reading>> call, Response<List<Reading>> response) {
//                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
//                            List<Reading> readings = response.body();
//                            String name = ms.getSensorName().toLowerCase();
//
//                            double value = readings.get(0).getReadingValue(); // default first reading
//                            String unit = "";
//                            String optimalRange = "N/A";
//
//                            if (name.contains("dht") || name.contains("temp")) {
//                                // Temperature
//                                unit = "°C";
//                                if (req != null)
//                                    optimalRange = String.format("%.0f-%.0f °C", req.getMinTemp(), req.getMaxTemp());
//                                updateCard("temperature", value, "Temperature", unit, optimalRange);
//
//                                // Humidity
//                                if (readings.size() >= 2) {
//                                    double humVal = readings.get(1).getReadingValue();
//                                    unit = "%";
//                                    if (req != null)
//                                        optimalRange = String.format("%.0f-%.0f %%", req.getMinHumidity(), req.getMaxHumidity());
//                                    updateCard("humidity", humVal, "Humidity", unit, optimalRange);
//                                }
//
//                            } else if (name.contains("ph")) {
//                                unit = "";
//                                if (req != null)
//                                    optimalRange = String.format("%.1f-%.1f", req.getMinPh(), req.getMaxPh());
//                                updateCard("ph", value, "pH Level", unit, optimalRange);
//
//                            } else if (name.contains("tds")) {
//                                unit = " ppm";
//                                if (req != null)
//                                    optimalRange = String.format("%.0f-%.0f ppm", req.getMinPpm(), req.getMaxPpm());
//                                updateCard("tds", value, "Water TDS", unit, optimalRange);
//
//                            } else if (name.contains("lux") || name.contains("light") || name.contains("ldr") || name.contains("photo")) {
//                                unit = " lx";
//                                if (req != null)
//                                    optimalRange = String.format("%.0f-%.0f lx", req.getMinLux(), req.getMaxLux());
//                                updateCard("light", value, "Light Intensity", unit, optimalRange);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Reading>> call, Throwable t) {
//                        Log.e(TAG, "Network failure", t);
//                    }
//                });
//    }
// Replace the existing fetchLatestReading method with this:
private void fetchLatestReading(ModelSensor ms) {
    int pos = modelSpinner.getSelectedItemPosition();
    if (pos == -1) return;

    // 1. Get the dynamic requirements for the selected model
    Model currentModel = models.get(pos);
    PlantRequirement req = currentModel.getActiveRequirement();

    api.getReadingsByModelSensor(ms.getModelSensorId())
            .enqueue(new Callback<List<Reading>>() {
                @Override
                public void onResponse(Call<List<Reading>> call, Response<List<Reading>> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        List<Reading> readings = response.body();
                        String name = ms.getSensorName().toLowerCase();
                        double value = readings.get(0).getReadingValue();

                        String unit = "";
                        String optimalRange = "N/A";

                        // 2. Map dynamic values to the UI cards
                        if (name.contains("dht") || name.contains("temp")) {
                            unit = "°C";
                            optimalRange = String.format("%.0f-%.0f°C", req.getMinTemp(), req.getMaxTemp());
                            updateCard("temperature", value, "Temperature", unit, optimalRange);

                            if (readings.size() >= 2) {
                                double humVal = readings.get(1).getReadingValue();
                                String humOpt = String.format("%.0f-%.0f%%", req.getMinHumidity(), req.getMaxHumidity());
                                updateCard("humidity", humVal, "Humidity", "%", humOpt);
                            }
                        } else if (name.contains("ph")) {
                            optimalRange = String.format("%.1f-%.1f", req.getMinPh(), req.getMaxPh());
                            updateCard("ph", value, "pH Level", "", optimalRange);
                        } else if (name.contains("tds")) {
                            optimalRange = String.format("%.0f-%.0f ppm", req.getMinPpm(), req.getMaxPpm());
                            updateCard("tds", value, "Water TDS", " ppm", optimalRange);
                        } else if (name.contains("lux") || name.contains("light")) {
                            optimalRange = String.format("%.0f-%.0f lx", req.getMinLux(), req.getMaxLux());
                            updateCard("light", value, "Light Intensity", " lx", optimalRange);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Reading>> call, Throwable t) {
                    Log.e(TAG, "Failed to fetch readings", t);
                }
            });
}

//    private void updateCard(String key, double value, String title, String unit, String optimalRange) {
//        TextView[] views = sensorViews.get(key);
//        if (views != null && views.length >= 3) {
//            // Current Value
//            views[0].setText(String.format("%.1f%s", value, unit));
//            // Card Title
//            views[1].setText(title.toUpperCase());
//            // Optimal Range from Backend
//            views[2].setText(optimalRange);
//
//            // Visual alert logic
//            checkRangeAlert(views[0], value, key);
//        }
//
//        ImageView iconView = sensorIcons.get(key);
//        if (iconView != null) iconView.setImageResource(getSensorIcon(key));
//    }

    private void updateCard(String key, double value, String title, String unit, String optimalRange) {
        TextView[] views = sensorViews.get(key);
        if (views != null && views.length >= 3) {
            // Update displayed values
            views[0].setText(String.format("%.1f%s", value, unit));
            views[1].setText(title.toUpperCase());
            views[2].setText(optimalRange);

            // Check and apply alert color
            checkRangeAlert(views[0], value, key);
        }

        if (isOutOfRange(key, value)) {
            long now = System.currentTimeMillis();

            if (!lastAlertTime.containsKey(key) ||
                    now - lastAlertTime.get(key) > ALERT_COOLDOWN) {

                sendLocalAlert(
                        title + " Alert",
                        title + " is out of range: " + value + unit +
                                "\nOptimal: " + optimalRange
                );

                lastAlertTime.put(key, now);
            }
        }


        // Update sensor icon
        ImageView iconView = sensorIcons.get(key);
        if (iconView != null) iconView.setImageResource(getSensorIcon(key));
    }

//    private void checkRangeAlert(TextView tvValue, double value, String key) {
//
//        if (models == null || models.isEmpty()) return;
//
//        int pos = modelSpinner.getSelectedItemPosition();
//        if (pos == -1) return;
//
//        PlantRequirement req = models.get(pos).getActiveRequirement(); // guaranteed non-null
//        if (req == null) return;
//
//        boolean isOutOfRange = false;
//
//        switch (key) {
//            case "temperature":
//                isOutOfRange = (value < req.getMinTemp() || value > req.getMaxTemp());
//                break;
//            case "humidity":
//                isOutOfRange = (value < req.getMinHumidity() || value > req.getMaxHumidity());
//                break;
//            case "ph":
//                isOutOfRange = (value < req.getMinPh() || value > req.getMaxPh());
//                break;
//            case "tds":
//                isOutOfRange = (value < req.getMinPpm() || value > req.getMaxPpm());
//                break;
//            case "light":
//                isOutOfRange = (value < req.getMinLux() || value > req.getMaxLux());
//                break;
//        }
//
//        tvValue.setTextColor(isOutOfRange ? Color.parseColor("#D32F2F") : Color.parseColor("#0A3327"));
//    }

    // Replace the existing checkRangeAlert method with this:
    private void checkRangeAlert(TextView tvValue, double value, String key) {
        int pos = modelSpinner.getSelectedItemPosition();
        if (pos == -1 || models.isEmpty()) return;

        // Use the dynamic requirement to determine text color
        PlantRequirement req = models.get(pos).getActiveRequirement();
        boolean isOutOfRange = false;

        switch (key) {
            case "temperature": isOutOfRange = (value < req.getMinTemp() || value > req.getMaxTemp()); break;
            case "humidity":    isOutOfRange = (value < req.getMinHumidity() || value > req.getMaxHumidity()); break;
            case "ph":          isOutOfRange = (value < req.getMinPh() || value > req.getMaxPh()); break;
            case "tds":         isOutOfRange = (value < req.getMinPpm() || value > req.getMaxPpm()); break;
            case "light":       isOutOfRange = (value < req.getMinLux() || value > req.getMaxLux()); break;
        }

        // Apply "Danger" Red or "Healthy" Dark Green
        tvValue.setTextColor(isOutOfRange ? Color.parseColor("#D32F2F") : Color.parseColor("#0A3327"));
    }

//    private void checkRangeAlert(TextView tvValue, double value, String key) {
//        int pos = modelSpinner.getSelectedItemPosition();
//        if (pos == -1) return;
//
//        Model currentModel = models.get(pos);
//        PlantRequirement req = currentModel.getActiveRequirement();
//
//        if (req == null) return; // Cannot check if requirements aren't loaded
//
//        boolean isOutOfRange = false;
//
//        // Logic based on backend keys: min_temperature, max_ph, etc.
//        switch (key) {
//            case "temperature":
//                isOutOfRange = (value < req.getMinTemp() || value > req.getMaxTemp());
//                break;
//            case "ph":
//                isOutOfRange = (value < req.getMinPh() || value > req.getMaxPh());
//                break;
//            case "tds":
//                isOutOfRange = (value < req.getMinPpm() || value > req.getMaxPpm());
//                break;
//            case "light":
//                isOutOfRange = (value < req.getMinLux() || value > req.getMaxLux());
//                break;
//            case "humidity":
//                isOutOfRange = (value < req.getMinHumidity() || value > req.getMaxHumidity());
//                break;
//        }
//
//        // Set text to Red if it's outside the optimal range, otherwise keep it Deep Green
//        if (isOutOfRange) {
//            tvValue.setTextColor(Color.parseColor("#D32F2F"));
//        } else {
//            tvValue.setTextColor(Color.parseColor("#0A3327"));
//        }
//    }

    private int getSensorIcon(String key) {
        switch (key.toLowerCase()) {
            case "temperature": return R.drawable.ic_temp;
            case "humidity": return R.drawable.ic_humidity;
            case "ph": return R.drawable.ic_ph;
            case "tds": return R.drawable.ic_tds;
            case "light": return R.drawable.ic_light;
            default: return R.drawable.ic_default_motor;
        }
    }

    // ---------------------- LINE CHART DATA ----------------------
    private void loadAllSensorCharts(List<ModelSensor> sensors) {
        List<LineDataSet> allDataSets = Collections.synchronizedList(new ArrayList<>());
        List<String> timestamps = new ArrayList<>();

        if (sensors.isEmpty()) {
            updateChartUI(new ArrayList<>(), new ArrayList<>());
            return;
        }

        for (ModelSensor ms : sensors) {
            String name = ms.getSensorName().toLowerCase();
            int limit = name.contains("dht") ? 10 : 5;

            api.getSensorReadings(ms.getModelSensorId(), limit)
                    .enqueue(new Callback<List<SensorReading>>() {
                        @Override
                        public void onResponse(Call<List<SensorReading>> call, Response<List<SensorReading>> response) {
                            List<SensorReading> readings = (response.isSuccessful() && response.body() != null)
                                    ? response.body() : new ArrayList<>();

                            readings.sort((r1,r2) -> r1.getRecordDate().compareTo(r2.getRecordDate()));

                            List<Entry> entries = new ArrayList<>();
                            for (int i = 0; i < readings.size() && i < 5; i++) {
                                entries.add(new Entry(i, (float) readings.get(i).getReadingValue()));
                                if (readings.get(i).getRecordDate() != null && readings.get(i).getRecordDate().length() >= 16) {
                                    String time = readings.get(i).getRecordDate().substring(11,16);
                                    if (timestamps.size() <= i) timestamps.add(time);
                                }
                            }

                            if (!entries.isEmpty()) {
                                allDataSets.add(createSet(entries, ms.getSensorName(), getSensorColor(name)));
                            }

                            // Only update chart after last sensor
                            if (allDataSets.size() == sensors.size() || allDataSets.size() > 0) {
                                updateChartUI(allDataSets, timestamps);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SensorReading>> call, Throwable t) {
                            Log.e(TAG, "Chart fetch failed", t);
                        }
                    });
        }
    }

    private LineDataSet createSet(List<Entry> entries, String label, String colorHex) {
        LineDataSet set = new LineDataSet(entries,label);
        int color = Color.parseColor(colorHex);
        set.setColor(color);
        set.setCircleColor(color);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setDrawValues(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        return set;
    }

    private String getSensorColor(String sensorName) {
        sensorName = sensorName.toLowerCase();
        if (sensorName.contains("ph")) return "#2E7D32";
        if (sensorName.contains("temp")) return "#D32F2F";
        if (sensorName.contains("humid")) return "#1976D2";
        if (sensorName.contains("tds")) return "#FBC02D";
        if (sensorName.contains("light") || sensorName.contains("lux")) return "#F57C00";
        return "#757575";
    }

    private void updateChartUI(List<LineDataSet> dataSets, List<String> timestamps) {
        if (dataSets.isEmpty()) {
            envLineChart.clear();
            envLineChart.setNoDataText("No data available");
            envLineChart.invalidate();
            return;
        }

        LineData lineData = new LineData();
        for (LineDataSet ds : dataSets) lineData.addDataSet(ds);

        envLineChart.setData(lineData);
        envLineChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int)value;
                return (index >=0 && index < timestamps.size()) ? timestamps.get(index) : "";
            }
        });
        envLineChart.invalidate();
    }

    // ---------------------- WATER LEVEL ----------------------
    private void setupWaterLevelChart() {
        waterLevelChart.getDescription().setEnabled(false);
        waterLevelChart.setDrawEntryLabels(false);
        waterLevelChart.setUsePercentValues(true);
        waterLevelChart.setTouchEnabled(false);
        waterLevelChart.setDrawHoleEnabled(true);
        waterLevelChart.setHoleRadius(80f);
        waterLevelChart.setHoleColor(Color.TRANSPARENT);
        waterLevelChart.getLegend().setEnabled(false);
    }

    private void fetchWaterLevel(int modelId) {
        api.getWaterLevelByModel(modelId).enqueue(new Callback<WaterLevel>() {
            @Override
            public void onResponse(Call<WaterLevel> call, Response<WaterLevel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    double waterLevel = response.body().getValue();
                    updateWaterLevelCard(waterLevel);
                }
            }

            @Override
            public void onFailure(Call<WaterLevel> call, Throwable t) {
                Log.e(TAG, "Water level fetch failed", t);
            }
        });
    }

//    private void updateWaterLevelCard(double value) {
//        updateCard("water", value, "Water Level", " %");
//        updateWaterLevel(value);
//    }

    private void updateWaterLevelCard(double value) {
        // Added "20-100%" as a placeholder for optimal water level
        updateCard("water", value, "Water Level", " %", "20-100%");
        updateWaterLevel(value);
    }

    private void updateWaterLevel(double value) {
        double remaining = 100 - value;
        int color;

        if (value < 20) color = Color.RED;
        else if (value < 50) color = Color.parseColor("#F97316");
        else color = Color.parseColor("#2081D5");

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float)value, "Water"));
        entries.add(new PieEntry((float)remaining, "Empty"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(color, Color.parseColor("#E2E8F0"));
        dataSet.setDrawValues(false);
        dataSet.setSliceSpace(2f);

        PieData data = new PieData(dataSet);
        waterLevelChart.setData(data);
        waterLevelChart.invalidate();

        tvWaterLevelValue.setText(String.format("%.0f%%", value));
    }

    // ---------------------- AUTO REFRESH ----------------------
    private void startAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (selectedModelId != -1) fetchModelSensors(selectedModelId);
                handler.postDelayed(this, REFRESH_INTERVAL);
            }
        }, REFRESH_INTERVAL);
    }

    // ---------------------- NAVIGATION ----------------------
    private void setupBottomNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_home);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_camera) startActivity(new Intent(this, CameraActivity.class));
            else if (id == R.id.nav_settings) startActivity(new Intent(this, MotorControlsActivity.class));
            else if (id == R.id.nav_config) startActivity(new Intent(this, AboutUsActivity.class));
            return true;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("NOTIFY", "Notification permission granted");
            } else {
                Log.w("NOTIFY", "Notification permission denied");
            }
        }
    }


    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
