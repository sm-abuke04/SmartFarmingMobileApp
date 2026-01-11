package com.example.capstoneproject.motors;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.capstoneproject.motors.adapter.ActuatorAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.capstoneproject.motors.model.Actuator;
import com.example.capstoneproject.R;

import java.util.ArrayList;
import java.util.List;

public class MotorControlsActivity extends AppCompatActivity {

    private TextView btnGlobal, btnSelected;
    private RecyclerView rvActuators;
    private LinearLayout emptyState;

    private ActuatorAdapter adapter;
    private final List<Actuator> globalActuators = new ArrayList<>();
    private final List<Actuator> selectedActuators = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor_controls);

        btnGlobal = findViewById(R.id.btnGlobal);
        btnSelected = findViewById(R.id.btnSelected);
        rvActuators = findViewById(R.id.rvActuators);
        emptyState = findViewById(R.id.emptyState);

        rvActuators.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ActuatorAdapter();
        rvActuators.setAdapter(adapter);

        seedTemporaryData(); // SAFE MOCK DATA

        showGlobal();

        btnGlobal.setOnClickListener(v -> showGlobal());
        btnSelected.setOnClickListener(v -> showSelected());
    }

    private void showGlobal() {
        btnGlobal.setTextColor(getColor(R.color.white));
        btnSelected.setTextColor(getColor(R.color.gray));

        adapter.setData(globalActuators);
        updateEmptyState(globalActuators);
    }

    private void showSelected() {
        btnSelected.setTextColor(getColor(R.color.white));
        btnGlobal.setTextColor(getColor(R.color.gray));

        adapter.setData(selectedActuators);
        updateEmptyState(selectedActuators);
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

    // TEMP DATA — REMOVED LATER WHEN API IS READY
    private void seedTemporaryData() {
        globalActuators.add(new Actuator(1, "Greenhouse Fan", false));
        globalActuators.add(new Actuator(2, "Main Lights", true));

        selectedActuators.add(new Actuator(3, "Water Pump Motor", false));
    }
}
