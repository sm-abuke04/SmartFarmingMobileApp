//package com.example.capstoneproject.motors;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.capstoneproject.AboutUsActivity;
//import com.example.capstoneproject.CameraActivity;
//import com.example.capstoneproject.R;
//import com.example.capstoneproject.api.ActuatorApi;
//import com.example.capstoneproject.api.ApiClient;
//import com.example.capstoneproject.dashboard.DashboardActivity;
//import com.example.capstoneproject.motors.adapter.ActuatorAdapter;
//import com.example.capstoneproject.motors.model.Actuator;
//import com.example.capstoneproject.motors.model.CommandRequest;
//import com.example.capstoneproject.motors.model.LatestCommand;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import java.util.ArrayList;
//import java.util.List;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class MotorControlsActivity extends AppCompatActivity {
//
//    private TextView btnGlobal, btnSelected;
//    private RecyclerView rvActuators;
//    private LinearLayout emptyState;
//    private ActuatorAdapter adapter;
//    private final List<Actuator> globalActuators = new ArrayList<>();
//    private final List<Actuator> selectedActuators = new ArrayList<>();
//    private List<Actuator> currentDisplayed = new ArrayList<>();
//    private ActuatorApi api;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_motor_controls);
//
//        btnGlobal = findViewById(R.id.btnGlobal);
//        btnSelected = findViewById(R.id.btnSelected);
//        rvActuators = findViewById(R.id.rvActuators);
//        emptyState = findViewById(R.id.emptyState);
//
//        setupBottomNavigation();
//        rvActuators.setLayoutManager(new LinearLayoutManager(this));
//        api = ApiClient.getClient(this).create(ActuatorApi.class);
//
//        adapter = new ActuatorAdapter((actuator, isOn, isGlobal) -> {
//            boolean isModel = !actuator.getModelAssignments().isEmpty();
//            int modelActId = isModel ? actuator.getModelAssignments().get(0).getModelActuatorId() : 0;
//
//            CommandRequest body = isModel ?
//                    new CommandRequest(null, modelActId, isOn ? 1 : 0, "Manual") :
//                    new CommandRequest(actuator.getActuatorId(), null, isOn ? 1 : 0, "Manual");
//
//            api.sendActuatorCommand(body).enqueue(new Callback<LatestCommand>() {
//                @Override
//                public void onResponse(Call<LatestCommand> call, Response<LatestCommand> response) {
//                    if (!response.isSuccessful()) {
//                        Toast.makeText(MotorControlsActivity.this, "Sync failed", Toast.LENGTH_SHORT).show();
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//                @Override
//                public void onFailure(Call<LatestCommand> call, Throwable t) {
//                    Toast.makeText(MotorControlsActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
//                }
//            });
//        });
//
//        rvActuators.setAdapter(adapter);
//        btnGlobal.setOnClickListener(v -> showGlobal());
//        btnSelected.setOnClickListener(v -> showSelected());
//        fetchActuators();
//    }
//
//    private void showGlobal() {
//        btnGlobal.setBackgroundResource(R.drawable.bg_pill_selected);
//        btnGlobal.setTextColor(Color.BLACK);
//        btnSelected.setBackground(null);
//        btnSelected.setTextColor(Color.parseColor("#000000"));
//
//        currentDisplayed = globalActuators;
//        adapter.setData(globalActuators);
//        updateEmptyState(globalActuators);
//    }
//
//    private void showSelected() {
//        btnSelected.setBackgroundResource(R.drawable.bg_pill_selected);
//        btnSelected.setTextColor(Color.BLACK);
//        btnGlobal.setBackground(null);
//        btnGlobal.setTextColor(Color.parseColor("#000000"));
//
//        currentDisplayed = selectedActuators;
//        adapter.setData(selectedActuators);
//        updateEmptyState(selectedActuators);
//    }
//
//    private void fetchActuators() {
//        api.getActuators().enqueue(new Callback<List<Actuator>>() {
//            @Override
//            public void onResponse(Call<List<Actuator>> call, Response<List<Actuator>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    globalActuators.clear();
//                    selectedActuators.clear();
//                    for (Actuator a : response.body()) {
//                        if ("Global".equalsIgnoreCase(a.getScope())) globalActuators.add(a);
//                        else selectedActuators.add(a);
//                    }
//                    showGlobal();
//                }
//            }
//            @Override public void onFailure(Call<List<Actuator>> call, Throwable t) {}
//        });
//    }
//
//
//    private void updateEmptyState(List<Actuator> list) {
//        emptyState.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
//        rvActuators.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
//    }
//
//    private void setupBottomNavigation() {
//        BottomNavigationView nav = findViewById(R.id.bottomNav);
//
//        // Set 'Home' or 'Settings' as the current active icon based on your preference
//        nav.setSelectedItemId(R.id.nav_settings);
//
//        nav.setOnItemSelectedListener(item -> {
//            int id = item.getItemId();
//            if (id == R.id.nav_camera) {
//                startActivity(new Intent(this, CameraActivity.class));
//                return true;
//            } else if (id == R.id.nav_home) {
//                startActivity(new Intent(this, DashboardActivity.class));
//                return true;
//            }
//            return false;
//        });
//
//        // This ID must exist in activity_motor_controls.xml
//        findViewById(R.id.btnSettings).setOnClickListener(v ->
//                startActivity(new Intent(this, AboutUsActivity.class)));
//    }
//}



package com.example.capstoneproject.motors;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstoneproject.AboutUsActivity;
import com.example.capstoneproject.CameraActivity;
import com.example.capstoneproject.R;
import com.example.capstoneproject.api.ActuatorApi;
import com.example.capstoneproject.api.ApiClient;
import com.example.capstoneproject.dashboard.DashboardActivity;
import com.example.capstoneproject.motors.adapter.ActuatorAdapter;
import com.example.capstoneproject.motors.model.Actuator;
import com.example.capstoneproject.motors.model.CommandRequest;
import com.example.capstoneproject.motors.model.LatestCommand;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MotorControlsActivity extends AppCompatActivity {

    private static final String TAG = "MotorControlsActivity";

    // UI Components
    private TextView btnGlobal, btnSelected;
    private RecyclerView rvActuators;
    private LinearLayout emptyState;
    private ActuatorAdapter adapter;

    // Data lists
    private final List<Actuator> globalActuators = new ArrayList<>();
    private final List<Actuator> selectedActuators = new ArrayList<>();
    private ActuatorApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor_controls);

        // 1. Initialize Views
        btnGlobal = findViewById(R.id.btnGlobal);
        btnSelected = findViewById(R.id.btnSelected);
        rvActuators = findViewById(R.id.rvActuators);
        emptyState = findViewById(R.id.emptyState);

        // 2. Setup RecyclerView
        rvActuators.setLayoutManager(new LinearLayoutManager(this));

        // 3. Initialize API
        api = ApiClient.getClient(this).create(ActuatorApi.class);

        // 4. Setup Adapter with Toggle Listener
        setupAdapter();

        // 5. Setup Navigation and Tab Listeners
        setupBottomNavigation();
        btnGlobal.setOnClickListener(v -> showGlobal());
        btnSelected.setOnClickListener(v -> showSelected());

        // 6. Initial Data Fetch
        fetchActuators();
    }

    /**
     * Handles the logic for toggling actuators via the API
     */
    private void setupAdapter() {
        adapter = new ActuatorAdapter((actuator, isOn, isGlobal) -> {
            // Determine if we are controlling a Model-specific actuator or a Global one
            boolean isModel = actuator.getModelAssignments() != null && !actuator.getModelAssignments().isEmpty();

            int value = isOn ? 1 : 0;
            CommandRequest body;

            if (isModel) {
                int modelActId = actuator.getModelAssignments().get(0).getModelActuatorId();
                body = new CommandRequest(null, modelActId, value, "Manual");
            } else {
                body = new CommandRequest(actuator.getActuatorId(), null, value, "Manual");
            }

            // Send command to Backend
            api.sendActuatorCommand(body).enqueue(new Callback<LatestCommand>() {
                @Override
                public void onResponse(Call<LatestCommand> call, Response<LatestCommand> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(MotorControlsActivity.this, "Failed to sync hardware", Toast.LENGTH_SHORT).show();
                        // Revert UI if needed or refresh data
                        fetchActuators();
                    }
                }

                @Override
                public void onFailure(Call<LatestCommand> call, Throwable t) {
                    Log.e(TAG, "Command Error", t);
                    Toast.makeText(MotorControlsActivity.this, "Network Error: Hardware unreachable", Toast.LENGTH_SHORT).show();
                }
            });
        });
        rvActuators.setAdapter(adapter);
    }

    /* ================= TAB LOGIC ================= */

    private void showGlobal() {
        // Update UI Tabs
        btnGlobal.setBackgroundResource(R.drawable.bg_pill_selected);
        btnGlobal.setTextColor(Color.BLACK);
        btnGlobal.setTypeface(null, Typeface.BOLD);

        btnSelected.setBackground(null);
        btnSelected.setTextColor(Color.parseColor("#80000000")); // Muted color
        btnSelected.setTypeface(null, Typeface.NORMAL);

        // Swap Data
        adapter.setData(globalActuators);
        updateEmptyState(globalActuators);
    }

    private void showSelected() {
        // Update UI Tabs
        btnSelected.setBackgroundResource(R.drawable.bg_pill_selected);
        btnSelected.setTextColor(Color.BLACK);
        btnSelected.setTypeface(null, Typeface.BOLD);

        btnGlobal.setBackground(null);
        btnGlobal.setTextColor(Color.parseColor("#80000000")); // Muted color
        btnGlobal.setTypeface(null, Typeface.NORMAL);

        // Swap Data
        adapter.setData(selectedActuators);
        updateEmptyState(selectedActuators);
    }

    /* ================= API DATA FETCHING ================= */

    private void fetchActuators() {
        api.getActuators().enqueue(new Callback<List<Actuator>>() {
            @Override
            public void onResponse(Call<List<Actuator>> call, Response<List<Actuator>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    globalActuators.clear();
                    selectedActuators.clear();

                    for (Actuator a : response.body()) {
                        if ("Global".equalsIgnoreCase(a.getScope())) {
                            globalActuators.add(a);
                        } else {
                            selectedActuators.add(a);
                        }
                    }

                    // Default to showing Global on first load
                    showGlobal();
                }
            }

            @Override
            public void onFailure(Call<List<Actuator>> call, Throwable t) {
                Log.e(TAG, "Fetch Failed", t);
                Toast.makeText(MotorControlsActivity.this, "Could not load actuators", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateEmptyState(List<Actuator> list) {
        if (list.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            rvActuators.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            rvActuators.setVisibility(View.VISIBLE);
        }
    }

    /* ================= NAVIGATION ================= */

    private void setupBottomNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(R.id.nav_settings);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_camera) {
                startActivity(new Intent(this, CameraActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_home) {
                startActivity(new Intent(this, DashboardActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

        // About Us Activity
        View btnSettings = findViewById(R.id.btnSettings);
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v ->
                    startActivity(new Intent(this, AboutUsActivity.class)));
        }
    }
}