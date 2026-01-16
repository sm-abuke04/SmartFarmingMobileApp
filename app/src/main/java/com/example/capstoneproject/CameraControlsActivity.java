////package com.example.capstoneproject;
////
////import android.content.Intent;
////import android.os.Bundle;
////import android.widget.Button;
////import android.widget.ImageButton;
////import android.widget.ImageView;
////import android.widget.TextView;
////import android.widget.Toast;
////
////import androidx.activity.EdgeToEdge;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.core.graphics.Insets;
////import androidx.core.view.ViewCompat;
////import androidx.core.view.WindowInsetsCompat;
////
////public class CameraControlsActivity extends AppCompatActivity {
////
////    // Top controls
////    ImageButton btnBack;
////    Button prevButton, nextButton;
////    TextView tvCamLabel;
////
////    // Camera preview placeholder
////    ImageView cameraPreview;
////
////    // Thumbnails
////    ImageView camThumb1, camThumb2, camThumb3, camThumb4;
////
////    int currentCam = 1;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        EdgeToEdge.enable(this);
////        setContentView(R.layout.activity_camera_controls);
////
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
////            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
////            return insets;
////        });
////
////        // Bind views
////        btnBack = findViewById(R.id.btnBack);
////        prevButton = findViewById(R.id.prevButton);
////        nextButton = findViewById(R.id.nextButton);
////        tvCamLabel = findViewById(R.id.tvPageTitle2);
////
////        cameraPreview = findViewById(R.id.imageView12);
////
////        camThumb1 = findViewById(R.id.imageView4);
////        camThumb2 = findViewById(R.id.imageView9);
////        camThumb3 = findViewById(R.id.imageView10);
////        camThumb4 = findViewById(R.id.imageView11);
////
////        // Click listeners
////        btnBack.setOnClickListener(v -> goBack());
////
////        prevButton.setOnClickListener(v -> previousCamera());
////
////        nextButton.setOnClickListener(v -> nextCamera());
////
////        camThumb1.setOnClickListener(v -> selectCamera(1));
////        camThumb2.setOnClickListener(v -> selectCamera(2));
////        camThumb3.setOnClickListener(v -> selectCamera(3));
////        camThumb4.setOnClickListener(v -> selectCamera(4));
////    }
////
////    // ================= FUNCTIONS (PLACEHOLDERS) =================
////
////    private void goBack() {
////        startActivity(new Intent(this, CameraActivity.class));
////        finish();
////    }
////
////    private void previousCamera() {
////        if (currentCam > 1) {
////            currentCam--;
////            updateCameraUI();
////        }
////    }
////
////    private void nextCamera() {
////        if (currentCam < 4) {
////            currentCam++;
////            updateCameraUI();
////        }
////    }
////
////    private void selectCamera(int camNumber) {
////        currentCam = camNumber;
////        updateCameraUI();
////    }
////
////    private void updateCameraUI() {
////        tvCamLabel.setText("CAM " + currentCam);
////
////        // Placeholder feedback only
////        Toast.makeText(
////                this,
////                "Selected Camera " + currentCam,
////                Toast.LENGTH_SHORT
////        ).show();
////
////        // TODO:
////        // - Send command to Arduino
////        // - Load camera stream/image
////        // - Update field data
////    }
////}
//
//
//
//
//
//
//package com.example.capstoneproject;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//public class CameraControlsActivity extends AppCompatActivity {
//
//    private ImageButton btnBack, btnPrev, btnNext;
//    private TextView tvCurrentCam;
//    private LinearLayout btnFieldA, btnFieldB;
//
//    // Tracking current camera state
//    private int currentCamIndex = 1;
//    private final String[] cameraNames = {"FRONT GATE", "GREENHOUSE", "FIELD SECTOR A", "FIELD SECTOR B"};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_camera_controls);
//
//        // Modern Edge-to-Edge Padding
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        initializeViews();
//        setupClickListeners();
//        updateCameraUI();
//    }
//
//    private void initializeViews() {
//        btnBack = findViewById(R.id.btnBack);
//        btnPrev = findViewById(R.id.prevButton);
//        btnNext = findViewById(R.id.nextButton);
//        tvCurrentCam = findViewById(R.id.tvCurrentCam);
//
//        // Quick Field Buttons
//        btnFieldA = findViewById(R.id.btnFieldA);
//        btnFieldB = findViewById(R.id.btnFieldB);
//    }
//
//    private void setupClickListeners() {
//        // Back Navigation
//        btnBack.setOnClickListener(v -> finish());
//
//        // Paging Logic
//        btnPrev.setOnClickListener(v -> {
//            if (currentCamIndex > 1) {
//                currentCamIndex--;
//                updateCameraUI();
//            }
//        });
//
//        btnNext.setOnClickListener(v -> {
//            if (currentCamIndex < 4) {
//                currentCamIndex++;
//                updateCameraUI();
//            }
//        });
//
//        // Quick View Shortcuts
//        btnFieldA.setOnClickListener(v -> {
//            currentCamIndex = 3; // Assuming Field A is Cam 3
//            updateCameraUI();
//            showFeedback("Snapping to Field A View");
//        });
//
//        btnFieldB.setOnClickListener(v -> {
//            currentCamIndex = 4; // Assuming Field B is Cam 4
//            updateCameraUI();
//            showFeedback("Snapping to Field B View");
//        });
//    }
//
//    private void updateCameraUI() {
//        // Update the Pill Label text based on index
//        tvCurrentCam.setText(cameraNames[currentCamIndex - 1]);
//
//        // Disable arrows at boundaries for better UX
//        btnPrev.setAlpha(currentCamIndex == 1 ? 0.3f : 1.0f);
//        btnPrev.setEnabled(currentCamIndex != 1);
//
//        btnNext.setAlpha(currentCamIndex == 4 ? 0.3f : 1.0f);
//        btnNext.setEnabled(currentCamIndex != 4);
//
//        // TODO: Trigger actual hardware switch (Retrofit or MQTT)
//    }
//
//    private void showFeedback(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//}
//
//package com.example.capstoneproject;
//
//import android.os.Bundle;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//public class CameraControlsActivity extends AppCompatActivity {
//
//    private ImageButton btnBack, btnPrev, btnNext, btnSettings;
//    private TextView tvCurrentCam;
//    private LinearLayout btnFieldA, btnFieldB;
//
//    private int currentCamIndex = 1;
//    private final String[] cameraNames = {"FRONT GATE", "GREENHOUSE", "FIELD SECTOR A", "FIELD SECTOR B"};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_camera_controls);
//
//        // Modern Edge-to-Edge Padding
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        initializeViews();
//        setupClickListeners();
//        updateCameraUI();
//    }
//
//    private void initializeViews() {
//        btnBack = findViewById(R.id.btnBack);
//        btnPrev = findViewById(R.id.prevButton);
//        btnNext = findViewById(R.id.nextButton);
//        tvCurrentCam = findViewById(R.id.tvCurrentCam);
//        btnFieldA = findViewById(R.id.btnFieldA);
//        btnFieldB = findViewById(R.id.btnFieldB);
//        btnSettings = findViewById(R.id.btnSettings); // Added from new design
//    }
//
//    private void setupClickListeners() {
//        btnBack.setOnClickListener(v -> finish());
//
//        btnPrev.setOnClickListener(v -> {
//            if (currentCamIndex > 1) {
//                currentCamIndex--;
//                updateCameraUI();
//            }
//        });
//
//        btnNext.setOnClickListener(v -> {
//            if (currentCamIndex < 4) {
//                currentCamIndex++;
//                updateCameraUI();
//            }
//        });
//
//        btnFieldA.setOnClickListener(v -> {
//            currentCamIndex = 3;
//            updateCameraUI();
//            showFeedback("Switching to Field A");
//        });
//
//        btnFieldB.setOnClickListener(v -> {
//            currentCamIndex = 4;
//            updateCameraUI();
//            showFeedback("Switching to Field B");
//        });
//    }
//
//    private void updateCameraUI() {
//        tvCurrentCam.setText(cameraNames[currentCamIndex - 1]);
//
//        // Boundary Feedback (Visual dimming)
//        btnPrev.setAlpha(currentCamIndex == 1 ? 0.3f : 1.0f);
//        btnPrev.setEnabled(currentCamIndex != 1);
//
//        btnNext.setAlpha(currentCamIndex == 4 ? 0.3f : 1.0f);
//        btnNext.setEnabled(currentCamIndex != 4);
//    }
//
//    private void showFeedback(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//}
package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstoneproject.dashboard.DashboardActivity;
import com.example.capstoneproject.motors.MotorControlsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CameraControlsActivity extends AppCompatActivity {

    private ImageButton btnBack, btnPrev, btnNext, btnSettings;
    private TextView tvCurrentCam;
    private LinearLayout btnFieldA, btnFieldB;
    private BottomNavigationView bottomNav;

    private int currentCamIndex = 1;
    private final String[] cameraNames = {"FRONT GATE", "GREENHOUSE", "FIELD SECTOR A", "FIELD SECTOR B"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_camera_controls);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Add padding only to top to avoid bottom nav overlap issues with system bars
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        initializeViews();
        setupClickListeners();
//        setupBottomNavigation();
        updateCameraUI();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnPrev = findViewById(R.id.prevButton);
        btnNext = findViewById(R.id.nextButton);
        tvCurrentCam = findViewById(R.id.tvCurrentCam);
        btnFieldA = findViewById(R.id.btnFieldA);
        btnFieldB = findViewById(R.id.btnFieldB);
        btnSettings = findViewById(R.id.btnSettings);
        bottomNav = findViewById(R.id.bottomNav);
    }

//    private void setupBottomNavigation() {
//        // Accessibility: Highlight the Camera tab
//        bottomNav.setSelectedItemId(R.id.nav_camera);
//
//        bottomNav.setOnItemSelectedListener(item -> {
//            int id = item.getItemId();
//
//            if (id == R.id.nav_home) {
//                startActivity(new Intent(this, DashboardActivity.class));
//                finish(); // Return to dashboard
//                return true;
//            } else if (id == R.id.nav_settings) {
//                startActivity(new Intent(this, MotorControlsActivity.class));
//                return true;
//            } else if (id == R.id.nav_config) {
//                startActivity(new Intent(this, AboutUsActivity.class));
//                return true;
//            } else if (id == R.id.nav_camera) {
//                return true; // Already here
//            }
//            return false;
//        });
//    }
//private void setupBottomNavigation() {
//    bottomNav = findViewById(R.id.bottomNav);
//    bottomNav.setSelectedItemId(R.id.nav_camera);
//
//    bottomNav.setOnItemSelectedListener(item -> {
//        int id = item.getItemId();
//
//        if (id == R.id.nav_home) {
//            startActivity(new Intent(this, DashboardActivity.class));
//            finish();
//            return true;
//        }
//        else if (id == R.id.nav_settings) { // This is "Control Center"
//            Intent intent = new Intent(this, MotorControlsActivity.class);
//            startActivity(intent);
//            return true;
//        }
//        else if (id == R.id.nav_config) { // This is "Settings / About Us"
//            startActivity(new Intent(this, AboutUsActivity.class));
//            return true;
//        }
//
//        return false;
//    });
//}

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnPrev.setOnClickListener(v -> {
            if (currentCamIndex > 1) {
                currentCamIndex--;
                updateCameraUI();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentCamIndex < 4) {
                currentCamIndex++;
                updateCameraUI();
            }
        });

        btnFieldA.setOnClickListener(v -> {
            currentCamIndex = 3;
            updateCameraUI();
            showFeedback("Switching to Field A");
        });

        btnFieldB.setOnClickListener(v -> {
            currentCamIndex = 4;
            updateCameraUI();
            showFeedback("Switching to Field B");
        });

        btnSettings.setOnClickListener(v -> {
            // Optional: Logic for camera-specific settings
            showFeedback("Camera settings coming soon");
        });
    }

    private void updateCameraUI() {
        String name = cameraNames[currentCamIndex - 1];
        tvCurrentCam.setText(name);

        // Accessibility: Announce change to TalkBack
        tvCurrentCam.announceForAccessibility("Viewing " + name);

        // Boundary Feedback
        btnPrev.setAlpha(currentCamIndex == 1 ? 0.3f : 1.0f);
        btnPrev.setEnabled(currentCamIndex != 1);

        btnNext.setAlpha(currentCamIndex == 4 ? 0.3f : 1.0f);
        btnNext.setEnabled(currentCamIndex != 4);
    }

    private void showFeedback(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}