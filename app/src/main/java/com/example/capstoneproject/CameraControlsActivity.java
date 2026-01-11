package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CameraControlsActivity extends AppCompatActivity {

    // Top controls
    ImageButton btnBack;
    Button prevButton, nextButton;
    TextView tvCamLabel;

    // Camera preview placeholder
    ImageView cameraPreview;

    // Thumbnails
    ImageView camThumb1, camThumb2, camThumb3, camThumb4;

    int currentCam = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_camera_controls);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bind views
        btnBack = findViewById(R.id.btnBack);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        tvCamLabel = findViewById(R.id.tvPageTitle2);

        cameraPreview = findViewById(R.id.imageView12);

        camThumb1 = findViewById(R.id.imageView4);
        camThumb2 = findViewById(R.id.imageView9);
        camThumb3 = findViewById(R.id.imageView10);
        camThumb4 = findViewById(R.id.imageView11);

        // Click listeners
        btnBack.setOnClickListener(v -> goBack());

        prevButton.setOnClickListener(v -> previousCamera());

        nextButton.setOnClickListener(v -> nextCamera());

        camThumb1.setOnClickListener(v -> selectCamera(1));
        camThumb2.setOnClickListener(v -> selectCamera(2));
        camThumb3.setOnClickListener(v -> selectCamera(3));
        camThumb4.setOnClickListener(v -> selectCamera(4));
    }

    // ================= FUNCTIONS (PLACEHOLDERS) =================

    private void goBack() {
        startActivity(new Intent(this, CameraActivity.class));
        finish();
    }

    private void previousCamera() {
        if (currentCam > 1) {
            currentCam--;
            updateCameraUI();
        }
    }

    private void nextCamera() {
        if (currentCam < 4) {
            currentCam++;
            updateCameraUI();
        }
    }

    private void selectCamera(int camNumber) {
        currentCam = camNumber;
        updateCameraUI();
    }

    private void updateCameraUI() {
        tvCamLabel.setText("CAM " + currentCam);

        // Placeholder feedback only
        Toast.makeText(
                this,
                "Selected Camera " + currentCam,
                Toast.LENGTH_SHORT
        ).show();

        // TODO:
        // - Send command to Arduino
        // - Load camera stream/image
        // - Update field data
    }
}
