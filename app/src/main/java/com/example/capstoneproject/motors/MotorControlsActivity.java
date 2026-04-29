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
import com.example.capstoneproject.motors.model.ActuatorItem;
import com.example.capstoneproject.motors.model.CommandRequest;
import com.example.capstoneproject.motors.model.LatestCommand;
import com.example.capstoneproject.motors.model.ModeRequest;
import com.example.capstoneproject.motors.model.ModelAssignment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MotorControlsActivity extends AppCompatActivity {

    private static final String TAG = "MotorControlsActivity";

    private TextView btnGlobal, btnSelected;
    private RecyclerView rvActuators;
    private LinearLayout emptyState;
    private ActuatorAdapter adapter;

    private final List<ActuatorItem> globalItems = new ArrayList<>();
    private final List<ActuatorItem> modelItems = new ArrayList<>();

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
        adapter = new ActuatorAdapter(new ActuatorAdapter.OnToggleListener() {
//            @Override
//            public void onToggle(ActuatorItem item, boolean isOn) {
//                // 1. Get the ID for the Mode change request
//                int id = item.isGlobal() ?
//                        item.getActuator().getActuatorId() :
//                        item.getModelAssignment().getModelActuatorId();
//                String scope = item.isGlobal() ? "Global" : "Model";
//
//                // 2. If it's in Auto, flip it to Manual first so sensors don't fight the user
//                if (item.isAuto()) {
//                    ModeRequest modeReq = new ModeRequest(id, "Manual", scope);
//                    api.updateActuatorMode(modeReq).enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, Response<Void> response) {
//                            // Now that it's in Manual, send the actual ON/OFF command
//                            sendCommand(item, isOn);
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//                            Log.e("Toggle", "Mode override failed");
//                            fetchActuators();
//                        }
//                    });
//                } else {
//                    // 3. If already in Manual, just send the command
//                    sendCommand(item, isOn);
//                }
//            }

            @Override
            public void onToggle(ActuatorItem item, boolean isOn) {
                String targetMode = isOn ? "Auto" : "Manual";

                // TEMPORARY UI FIX: Update the object immediately so the text changes
                if (item.getModelAssignment() != null) {
                    item.getModelAssignment().setControlType(targetMode);
                }
                adapter.notifyDataSetChanged();

                // Now send to API
                int id = item.isGlobal() ? item.getActuator().getActuatorId() : item.getModelAssignment().getModelActuatorId();
                String scope = item.isGlobal() ? "Global" : "Model";

                api.updateActuatorMode(new ModeRequest(id, targetMode, scope)).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            executeToggleCommand(item, isOn, "Manual Toggle");
                        } else {
                            // If it failed, refresh to revert the "Fake" UI update we did above
                            fetchActuators();
                            Toast.makeText(getApplicationContext(), "Failed to save mode", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        fetchActuators();
                    }
                });
            }

            @Override
            public void onModeChange(ActuatorItem item, String nextMode) {
                // Logic for tapping the text label directly
                int id = item.isGlobal() ? item.getActuator().getActuatorId() : item.getModelAssignment().getModelActuatorId();
                String scope = item.isGlobal() ? "Global" : "Model";
                api.updateActuatorMode(new ModeRequest(id, nextMode, scope)).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) { fetchActuators(); }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) { }
                });
            }
        });
        rvActuators.setAdapter(adapter);
    }

    private void executeToggleCommand(ActuatorItem item, boolean isOn, String source) {
        Integer actuatorId = item.isGlobal() ? item.getActuator().getActuatorId() : null;
        Integer modelActuatorId = !item.isGlobal() ? item.getModelAssignment().getModelActuatorId() : null;
        int val = isOn ? 1 : 0;

        CommandRequest cmdReq = new CommandRequest(actuatorId, modelActuatorId, val, source);
        api.sendActuatorCommand(cmdReq).enqueue(new Callback<LatestCommand>() {
            @Override
            public void onResponse(Call<LatestCommand> call, Response<LatestCommand> response) { fetchActuators(); }
            @Override
            public void onFailure(Call<LatestCommand> call, Throwable t) { fetchActuators(); }
        });
    }

            // Helper method to keep your code clean
//            private void sendCommand(ActuatorItem item, boolean isOn) {
//                Integer actuatorId = null;
//                Integer modelActuatorId = null;
//                int commandValue = isOn ? 1 : 0;
//                String source = "Manual"; // Since the user is physically toggling it
//
//                if (item.isGlobal()) {
//                    // It's a Global Actuator
//                    actuatorId = item.getActuator().getActuatorId();
//                } else {
//                    // It's a Model-specific Actuator
//                    modelActuatorId = item.getModelAssignment().getModelActuatorId();
//                }
//
//                // Now the arguments match: (Integer, Integer, int, String)
//                CommandRequest cmdReq = new CommandRequest(actuatorId, modelActuatorId, commandValue, source);
//
//                api.sendActuatorCommand(cmdReq).enqueue(new Callback<LatestCommand>() {
//                    @Override
//                    public void onResponse(Call<LatestCommand> call, Response<LatestCommand> response) {
//                        if (response.isSuccessful()) {
//                            fetchActuators(); // Refresh to show latest state
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();
//                            fetchActuators(); // Revert toggle
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<LatestCommand> call, Throwable t) {
//                        Log.e("API", "Command failed", t);
//                        fetchActuators(); // Revert toggle
//                    }
//                });
//            }
//            // Inside your OnToggleListener implementation in the Activity
//            @Override
//            public void onModeChange(ActuatorItem item, String nextMode) {
//                // Determine the ID and Scope (Global vs Model)
//                int id;
//                String scope;
//
//                if (item.isGlobal()) {
//                    id = item.getActuator().getActuatorId();
//                    scope = "Global";
//                } else {
//                    id = item.getModelAssignment().getModelActuatorId();
//                    scope = "Model";
//                }
//
//                // Pass these to your ModeRequest
//                ModeRequest request = new ModeRequest(id, nextMode, scope);
//
//                api.updateActuatorMode(request).enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        if (response.isSuccessful()) {
//                            fetchActuators(); // Refresh UI to reflect the new mode
//                            Toast.makeText(getApplicationContext(), "Mode: " + nextMode, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Log.e("API", "Failed to change mode", t);
//                    }
//                });
//            }
//        });
//
//        rvActuators.setAdapter(adapter);
//    }

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
//        adapter.setData(globalActuators);
//        updateEmptyState(globalActuators);

        adapter.setData(globalItems);
        updateEmptyState(globalItems);
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
//        adapter.setData(selectedActuators);
//        updateEmptyState(selectedActuators);
        adapter.setData(modelItems);
        updateEmptyState(modelItems);
    }

    /* ================= API DATA FETCHING ================= */



    private void fetchActuators() {
        api.getActuators().enqueue(new Callback<List<Actuator>>() {
            @Override
            public void onResponse(Call<List<Actuator>> call, Response<List<Actuator>> response) {
                if (!response.isSuccessful() || response.body() == null) return;

                globalItems.clear();
                modelItems.clear();

                for (Actuator actuator : response.body()) {


                    if ("Global".equalsIgnoreCase(actuator.getScope())) {
                        // Truly global actuator
                        globalItems.add(new ActuatorItem(actuator, null));
                    }
// MODEL-SPECIFIC (even if assignments are empty)
                    else if ("Model".equalsIgnoreCase(actuator.getScope())) {
                        if (actuator.getModelAssignments() != null && !actuator.getModelAssignments().isEmpty()) {
                            // One row per model assignment
                            for (ModelAssignment ma : actuator.getModelAssignments()) {
                                modelItems.add(new ActuatorItem(actuator, ma));
                            }
                        }
//                        else {
//                            // No assignments yet → still show as model actuator (optional row or empty placeholder)
//                            modelItems.add(new ActuatorItem(actuator, null));
//                        }
                    }

                }

                showGlobal();
            }

            @Override
            public void onFailure(Call<List<Actuator>> call, Throwable t) {
                Toast.makeText(MotorControlsActivity.this,
                        "Failed to load actuators", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void updateEmptyState(List<ActuatorItem> list) {
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
            } else if (id == R.id.nav_config) {
                startActivity(new Intent(this, AboutUsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }
}