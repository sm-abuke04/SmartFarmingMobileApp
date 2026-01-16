package com.example.capstoneproject.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneproject.AboutUsActivity;
import com.example.capstoneproject.CameraActivity;
import com.example.capstoneproject.R;
import com.example.capstoneproject.api.ApiClient;
import com.example.capstoneproject.api.DashboardApi;
import com.example.capstoneproject.dashboard.models.Model;
import com.example.capstoneproject.dashboard.models.ModelSensor;
import com.example.capstoneproject.dashboard.models.Reading;
import com.example.capstoneproject.motors.MotorControlsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";
    private Spinner modelSpinner;
    private final List<Model> models = new ArrayList<>();
    private int selectedModelId = -1;

    // Map: Key (sensor type) -> TextView array [ValueView, TitleView]
    private final Map<String, TextView[]> sensorCards = new HashMap<>();

    private final Handler handler = new Handler();
    private static final int REFRESH_INTERVAL = 10000; // 10 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        // 1. Initialize UI components
        modelSpinner = findViewById(R.id.spinnerModels);
        bindAllSensorCards();
        setupNavigation();

        // 2. Setup Spinner Adapter
        ArrayAdapter<Model> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, models);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(adapter);

        // 3. Spinner Selection Logic
        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedModelId = models.get(position).getHydromodelId();
                resetAllCards();
                fetchModelSensors(selectedModelId);
                startAutoRefresh();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 4. Initial Data Load
        loadModels(adapter);
    }

//    private void setupNavigation() {
//        BottomNavigationView nav = findViewById(R.id.bottomNav);
//        nav.setSelectedItemId(R.id.nav_home);
//
//        nav.setOnItemSelectedListener(item -> {
//            int id = item.getItemId();
//            if (id == R.id.nav_camera) {
//                startActivity(new Intent(this, CameraActivity.class));
//                return true;
//            } else if (id == R.id.nav_settings) {
//                startActivity(new Intent(this, MotorControlsActivity.class));
//                return true;
//            }
//            return false;
//        });
//
//        findViewById(R.id.btnSettings).setOnClickListener(v ->
//                startActivity(new Intent(this, AboutUsActivity.class)));
//    }
private void setupNavigation() {
    BottomNavigationView nav = findViewById(R.id.bottomNav);

    // Set the correct active state visually
    nav.setSelectedItemId(R.id.nav_home);

    nav.setOnItemSelectedListener(item -> {
        int id = item.getItemId();

        // 1. Accessibility: Don't re-trigger if already on this page
        if (id == nav.getSelectedItemId()) {
            return false;
        }

        // 2. Navigation Logic
        if (id == R.id.nav_camera) {
            startActivity(new Intent(this, CameraActivity.class));
            return true;
        }
        else if (id == R.id.nav_home) {
            // Already here, but logic included for completeness
            return true;
        }
        else if (id == R.id.nav_settings) {
            // This is your "Control Center" (MotorControls)
            startActivity(new Intent(this, MotorControlsActivity.class));
            return true;
        }
        else if (id == R.id.nav_config) {
            // This is your new "Settings" (AboutUsActivity)
            startActivity(new Intent(this, AboutUsActivity.class));
            return true;
        }

        return false;
    });

    // Note: I removed the old findViewById(R.id.btnSettings) listener
    // because that action is now handled by the bottom nav (nav_config).
}

    /* ================= API LOGIC ================= */

    private void loadModels(ArrayAdapter<Model> adapter) {
        DashboardApi api = ApiClient.getClient(this).create(DashboardApi.class);
        api.getModels().enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    models.clear();
                    models.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    if (!models.isEmpty()) modelSpinner.setSelection(0);
                }
            }
            @Override public void onFailure(Call<List<Model>> call, Throwable t) { Log.e(TAG, "Fail", t); }
        });
    }

    private void fetchModelSensors(int modelId) {
        DashboardApi api = ApiClient.getClient(this).create(DashboardApi.class);
        api.getModelSensors(modelId).enqueue(new Callback<List<ModelSensor>>() {
            @Override
            public void onResponse(Call<List<ModelSensor>> call, Response<List<ModelSensor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (ModelSensor ms : response.body()) {
                        fetchLatestReading(ms);
                    }
                }
            }
            @Override public void onFailure(Call<List<ModelSensor>> call, Throwable t) { Log.e(TAG, "Fail", t); }
        });
    }

    private void fetchLatestReading(ModelSensor ms) {
        DashboardApi api = ApiClient.getClient(this).create(DashboardApi.class);
        api.getReadingsByModelSensor(ms.getModelSensorId())
                .enqueue(new Callback<List<Reading>>() {
                    @Override
                    public void onResponse(Call<List<Reading>> call, Response<List<Reading>> response) {
                        if (!response.isSuccessful() || response.body() == null || response.body().isEmpty()) return;

                        List<Reading> readings = response.body();
                        String name = ms.getSensorName().toLowerCase();

                        // logic to parse specific sensor types and apply custom icons
                        if (name.contains("dht")) {
                            if (readings.size() >= 2) {
                                updateCard("temperature", readings.get(0).getReadingValue(),
                                        "Temperature", "°C", R.drawable.ic_temp);
                                updateCard("humidity", readings.get(1).getReadingValue(),
                                        "Humidity", "%", R.drawable.ic_humidity);
                            }
                        } else if (name.contains("ph")) {
                            updateCard("ph", readings.get(0).getReadingValue(),
                                    "pH Level", "", R.drawable.ic_ph);
                        } else if (name.contains("tds")) {
                            updateCard("tds", readings.get(0).getReadingValue(),
                                    "Water TDS", " ppm", R.drawable.ic_tds);
                        } else if (name.contains("light") || name.contains("lux")) {
                            updateCard("light", readings.get(0).getReadingValue(),
                                    "Light Intensity", " lx", R.drawable.ic_light);
                        }
                    }

                    @Override public void onFailure(Call<List<Reading>> call, Throwable t) {
                        Log.e(TAG, "Failed to fetch readings for: " + ms.getSensorName());
                    }
                });
    }
    /* ================= UI UPDATES ================= */

    private void updateCard(String key, double value, String title, String unit, int iconRes) {
        if (!sensorCards.containsKey(key)) return;

        TextView[] views = sensorCards.get(key);

        // 1. Update the Value text (1 decimal place)
        views[0].setText(String.format("%.1f%s", value, unit));
        views[0].setTextColor(Color.parseColor("#0A3327"));

        // 2. Update the Title (Force Uppercase)
        views[1].setText(title.toUpperCase());

        // 3. Find the ImageView in the parent layout and update the icon
        View cardParent = (View) views[0].getParent().getParent(); // Ascend to the horizontal LinearLayout
        ImageView iconView = cardParent.findViewById(R.id.ivSensorIcon);
        if (iconView != null) {
            iconView.setImageResource(iconRes);
        }
    }

    private void resetAllCards() {
        for (TextView[] views : sensorCards.values()) {
            views[0].setText("--");
            views[0].setTextColor(Color.LTGRAY);
        }
    }

    private void bindAllSensorCards() {
        bindSensorCard(R.id.cardTemperature, "temperature");
        bindSensorCard(R.id.cardHumidity, "humidity");
        bindSensorCard(R.id.cardPh, "ph");
        bindSensorCard(R.id.cardTds, "tds");
        bindSensorCard(R.id.cardLight, "light");
    }

    private void bindSensorCard(int cardId, String key) {
        View card = findViewById(cardId);
        if (card != null) {
            TextView value = card.findViewById(R.id.tvSensorValue);
            TextView title = card.findViewById(R.id.tvSensorTitle);
            sensorCards.put(key, new TextView[]{value, title});
        }
    }

    private void startAutoRefresh() {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (selectedModelId != -1) fetchModelSensors(selectedModelId);
                handler.postDelayed(this, REFRESH_INTERVAL);
            }
        }, REFRESH_INTERVAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}

//
//
//package com.example.capstoneproject.dashboard;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.capstoneproject.AboutUsActivity;
//import com.example.capstoneproject.CameraActivity;
//import com.example.capstoneproject.R;
//import com.example.capstoneproject.api.ApiClient;
//import com.example.capstoneproject.api.DashboardApi;
//import com.example.capstoneproject.dashboard.models.Model;
//import com.example.capstoneproject.dashboard.models.ModelSensor;
//import com.example.capstoneproject.dashboard.models.Reading;
//import com.example.capstoneproject.motors.MotorControlsActivity;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class DashboardActivity extends AppCompatActivity {
//
//    private static final String TAG = "DashboardActivity";
//    private Spinner modelSpinner;
//    private final List<Model> models = new ArrayList<>();
//    private int selectedModelId = -1;
//
//    // Map to hold references to the Value and Title TextViews for each sensor type
//    private final Map<String, TextView[]> sensorCards = new HashMap<>();
//
//    private final Handler handler = new Handler();
//    private static final int REFRESH_INTERVAL = 10000; // 10 seconds refresh rate
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_dashboard);
//
//        // 1. Initialize UI and bind layout cards
//        modelSpinner = findViewById(R.id.spinnerModels);
//        bindAllSensorCards();
//        setupNavigation();
//
//        // 2. Setup Spinner for selecting different hydroponic models
//        ArrayAdapter<Model> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_item, models);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        modelSpinner.setAdapter(adapter);
//
//        // 3. Selection logic
//        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                selectedModelId = models.get(position).getHydromodelId();
//                resetAllCards(); // Clear old data visually
//                fetchModelSensors(selectedModelId); // Get sensor list for this model
//                startAutoRefresh(); // Start the 10-second timer
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {}
//        });
//
//        // 4. Load models from the API on start
//        loadModels(adapter);
//    }
//
//    /**
//     * Handles the Bottom Navigation and Top Settings buttons
//     */
//    private void setupNavigation() {
//        BottomNavigationView nav = findViewById(R.id.bottomNav);
//        nav.setSelectedItemId(R.id.nav_home);
//
//        nav.setOnItemSelectedListener(item -> {
//            int id = item.getItemId();
//            if (id == R.id.nav_camera) {
//                startActivity(new Intent(this, CameraActivity.class));
//                return true;
//            } else if (id == R.id.nav_settings) {
//                startActivity(new Intent(this, MotorControlsActivity.class));
//                return true;
//            }
//            return false;
//        });
//
//        findViewById(R.id.btnSettings).setOnClickListener(v ->
//                startActivity(new Intent(this, AboutUsActivity.class)));
//    }
//
//    /* ================= API LOGIC ================= */
//
//    private void loadModels(ArrayAdapter<Model> adapter) {
//        DashboardApi api = ApiClient.getClient(this).create(DashboardApi.class);
//        api.getModels().enqueue(new Callback<List<Model>>() {
//            @Override
//            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    models.clear();
//                    models.addAll(response.body());
//                    adapter.notifyDataSetChanged();
//                    if (!models.isEmpty()) modelSpinner.setSelection(0);
//                }
//            }
//            @Override public void onFailure(Call<List<Model>> call, Throwable t) {
//                Log.e(TAG, "Failed to load models", t);
//            }
//        });
//    }
//
//    private void fetchModelSensors(int modelId) {
//        DashboardApi api = ApiClient.getClient(this).create(DashboardApi.class);
//        api.getModelSensors(modelId).enqueue(new Callback<List<ModelSensor>>() {
//            @Override
//            public void onResponse(Call<List<ModelSensor>> call, Response<List<ModelSensor>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    for (ModelSensor ms : response.body()) {
//                        fetchLatestReading(ms);
//                    }
//                }
//            }
//            @Override public void onFailure(Call<List<ModelSensor>> call, Throwable t) {
//                Log.e(TAG, "Failed to fetch model sensors", t);
//            }
//        });
//    }
//
//    private void fetchLatestReading(ModelSensor ms) {
//        DashboardApi api = ApiClient.getClient(this).create(DashboardApi.class);
//        api.getReadingsByModelSensor(ms.getModelSensorId())
//                .enqueue(new Callback<List<Reading>>() {
//                    @Override
//                    public void onResponse(Call<List<Reading>> call, Response<List<Reading>> response) {
//                        if (!response.isSuccessful() || response.body() == null || response.body().isEmpty()) return;
//
//                        List<Reading> readings = response.body();
//                        String name = ms.getSensorName().toLowerCase();
//
//                        // logic to parse specific sensor types and apply custom icons
//                        if (name.contains("dht")) {
//                            if (readings.size() >= 2) {
//                                updateCard("temperature", readings.get(0).getReadingValue(),
//                                        "Temperature", "°C", R.drawable.ic_temp);
//                                updateCard("humidity", readings.get(1).getReadingValue(),
//                                        "Humidity", "%", R.drawable.ic_humidity);
//                            }
//                        } else if (name.contains("ph")) {
//                            updateCard("ph", readings.get(0).getReadingValue(),
//                                    "pH Level", "", R.drawable.ic_ph);
//                        } else if (name.contains("tds")) {
//                            updateCard("tds", readings.get(0).getReadingValue(),
//                                    "Water TDS", " ppm", R.drawable.ic_tds);
//                        } else if (name.contains("light") || name.contains("lux")) {
//                            updateCard("light", readings.get(0).getReadingValue(),
//                                    "Light Intensity", " lx", R.drawable.ic_light);
//                        }
//                    }
//
//                    @Override public void onFailure(Call<List<Reading>> call, Throwable t) {
//                        Log.e(TAG, "Failed to fetch readings for: " + ms.getSensorName());
//                    }
//                });
//    }
//
//    /* ================= UI UPDATES ================= */
//
//    /**
//     * Core UI method to update individual sensor cards dynamically
//     */
//    private void updateCard(String key, double value, String title, String unit, int iconRes) {
//        if (!sensorCards.containsKey(key)) return;
//
//        TextView[] views = sensorCards.get(key);
//
//        // 1. Update the Value text (1 decimal place)
//        views[0].setText(String.format("%.1f%s", value, unit));
//        views[0].setTextColor(Color.parseColor("#0A3327"));
//
//        // 2. Update the Title (Force Uppercase)
//        views[1].setText(title.toUpperCase());
//
//        // 3. Find the ImageView in the parent layout and update the icon
//        View cardParent = (View) views[0].getParent().getParent(); // Ascend to the horizontal LinearLayout
//        ImageView iconView = cardParent.findViewById(R.id.ivSensorIcon);
//        if (iconView != null) {
//            iconView.setImageResource(iconRes);
//        }
//    }
//
//    private void resetAllCards() {
//        for (TextView[] views : sensorCards.values()) {
//            views[0].setText("--");
//            views[0].setTextColor(Color.LTGRAY);
//        }
//    }
//
//    private void bindAllSensorCards() {
//        bindSensorCard(R.id.cardTemperature, "temperature");
//        bindSensorCard(R.id.cardHumidity, "humidity");
//        bindSensorCard(R.id.cardPh, "ph");
//        bindSensorCard(R.id.cardTds, "tds");
//        bindSensorCard(R.id.cardLight, "light");
//    }
//
//    private void bindSensorCard(int cardId, String key) {
//        View card = findViewById(cardId);
//        if (card != null) {
//            TextView value = card.findViewById(R.id.tvSensorValue);
//            TextView title = card.findViewById(R.id.tvSensorTitle);
//            sensorCards.put(key, new TextView[]{value, title});
//        }
//    }
//
//    private void startAutoRefresh() {
//        handler.removeCallbacksAndMessages(null);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (selectedModelId != -1) fetchModelSensors(selectedModelId);
//                handler.postDelayed(this, REFRESH_INTERVAL);
//            }
//        }, REFRESH_INTERVAL);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        handler.removeCallbacksAndMessages(null); // Prevent memory leaks
//    }
//}