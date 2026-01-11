package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstoneproject.dashboard.DashboardActivity;
import com.example.capstoneproject.motors.MotorControlsActivity;

public class CameraActivity extends AppCompatActivity {

    LinearLayout btnDashboard, btnMotor;
    Button btnAdjustCamera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_camera);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnDashboard = findViewById(R.id.navDashboard);
        btnMotor = findViewById(R.id.navControls);
        btnAdjustCamera = findViewById(R.id.btnCameraControl);

        btnDashboard.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
        });

        btnMotor.setOnClickListener(v -> {
            startActivity(new Intent(this, MotorControlsActivity.class));
        });

        btnAdjustCamera.setOnClickListener(view -> {
            startActivity(new Intent(this, CameraControlsActivity.class));
        });
    }
}