package com.example.capstoneproject;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CameraControlsActivity extends AppCompatActivity {

    private ImageButton btnBack, btnPrev, btnNext, btnSettings;
    private TextView tvCurrentCam;
    private LinearLayout btnFieldA, btnFieldB;
    private ImageView imgMainStream;

    private int currentCamIndex = 1;
    private final String[] cameraNames = {
            "HYDROPONIC MODEL",
            "TOWER MODEL",
            "FIELD SECTOR A",
            "FIELD SECTOR B"
    };
    private final int[] cameraImages = {
            R.drawable.hydroponic, // HYDROPONIC MODEL
            R.drawable.tower       // TOWER MODEL
            // Field A/B images can be added later
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_camera_controls);

        // Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        initializeViews();
        setupClickListeners();
        updateCameraUI();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnPrev = findViewById(R.id.prevButton);
        btnNext = findViewById(R.id.nextButton);
        tvCurrentCam = findViewById(R.id.tvCurrentCam);
        btnFieldA = findViewById(R.id.btnFieldA);
        btnFieldB = findViewById(R.id.btnFieldB);
        imgMainStream = findViewById(R.id.imgMainStream);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnPrev.setOnClickListener(v -> {
            if (currentCamIndex > 1) {
                currentCamIndex--;
                updateCameraUI();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentCamIndex < cameraNames.length) {
                currentCamIndex++;
                updateCameraUI();
            }
        });

        btnFieldA.setOnClickListener(v -> {
            currentCamIndex = 3; // Field A
            updateCameraUI();
            showFeedback("Switched to Field A");
        });

        btnFieldB.setOnClickListener(v -> {
            currentCamIndex = 4; // Field B
            updateCameraUI();
            showFeedback("Switched to Field B");
        });
    }

    private void updateCameraUI() {
        String name = cameraNames[currentCamIndex - 1];
        tvCurrentCam.setText(name);
        tvCurrentCam.announceForAccessibility("Viewing " + name);

        // Only update images for first two cameras
        if (currentCamIndex <= 2) {
            imgMainStream.setImageResource(cameraImages[currentCamIndex - 1]);
        } else {
            imgMainStream.setImageResource(R.drawable.nocam); // generic placeholder for Field A/B
        }

        // Boundary feedback
        btnPrev.setAlpha(currentCamIndex == 1 ? 0.3f : 1.0f);
        btnPrev.setEnabled(currentCamIndex != 1);

        btnNext.setAlpha(currentCamIndex == cameraNames.length ? 0.3f : 1.0f);
        btnNext.setEnabled(currentCamIndex != cameraNames.length);
    }

    private void showFeedback(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
