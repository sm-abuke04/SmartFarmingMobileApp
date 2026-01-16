//package com.example.capstoneproject;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.LinearLayout;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.example.capstoneproject.dashboard.DashboardActivity;
//import com.example.capstoneproject.motors.MotorControlsActivity;
//
//public class CameraActivity extends AppCompatActivity {
//
//    LinearLayout btnDashboard, btnMotor;
//    Button btnAdjustCamera;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_camera);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        btnDashboard = findViewById(R.id.navDashboard);
//        btnMotor = findViewById(R.id.navControls);
//        btnAdjustCamera = findViewById(R.id.btnCameraControl);
//
//        btnDashboard.setOnClickListener(v -> {
//            startActivity(new Intent(this, DashboardActivity.class));
//        });
//
//        btnMotor.setOnClickListener(v -> {
//            startActivity(new Intent(this, MotorControlsActivity.class));
//        });
//
//        btnAdjustCamera.setOnClickListener(view -> {
//            startActivity(new Intent(this, CameraControlsActivity.class));
//        });
//    }
//}

//package com.example.capstoneproject;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.capstoneproject.dashboard.DashboardActivity;
//import com.example.capstoneproject.motors.MotorControlsActivity;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//public class CameraActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera);
//
//        // This is the Java part you need to make the button DO something
//        Button btnSwitch = findViewById(R.id.btnCameraControl);
//        btnSwitch.setOnClickListener(v -> {
//            Intent intent = new Intent(CameraActivity.this, CameraControlsActivity.class);
//            startActivity(intent);
//        });
//
//        setupNavigation();
//    }
//
//    private void setupNavigation() {
//        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
//
//        // Highlight the 'Camera' tab
//        bottomNav.setSelectedItemId(R.id.nav_camera);
//
//        bottomNav.setOnItemSelectedListener(item -> {
//            int id = item.getItemId();
//
//            if (id == R.id.nav_home) {
//                startActivity(new Intent(this, DashboardActivity.class));
//                finish();
//                return true;
//            } else if (id == R.id.nav_settings) {
//                // Control Center
//                startActivity(new Intent(this, MotorControlsActivity.class));
//                return true;
//            } else if (id == R.id.nav_config) {
//                // Settings
//                startActivity(new Intent(this, AboutUsActivity.class));
//                return true;
//            }
//            return false;
//        });
//    }
//}
//package com.example.capstoneproject;
//
//import android.content.Intent;
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.capstoneproject.dashboard.DashboardActivity;
//import com.example.capstoneproject.motors.MotorControlsActivity;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//public class CameraActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera);
//
//        setupNavigation();
//    }
//
//    private void setupNavigation() {
//        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
//
//        // Highlight the Camera icon as the active page
//        bottomNav.setSelectedItemId(R.id.nav_camera);
//
//        bottomNav.setOnItemSelectedListener(item -> {
//            int id = item.getItemId();
//
//            if (id == R.id.nav_home) {
//                // Go to Dashboard
//                startActivity(new Intent(this, DashboardActivity.class));
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                finish(); // Finish current to keep stack clean
//                return true;
//            }
//            else if (id == R.id.nav_camera) {
//                // Already here
//                return true;
//            }
//            else if (id == R.id.nav_settings) {
//                // This is the "Control Center"
//                startActivity(new Intent(this, MotorControlsActivity.class));
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                return true;
//            }
//            else if (id == R.id.nav_config) {
//                // This is the "Settings" / About Us
//                startActivity(new Intent(this, AboutUsActivity.class));
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                return true;
//            }
//            return false;
//        });
//    }
//}
package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button; // Needed for the button
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneproject.dashboard.DashboardActivity;
import com.example.capstoneproject.motors.MotorControlsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // --- THE MISSING JAVA FIX ---
        // 1. Find the button by the ID you set in XML
        Button btnSwitch = findViewById(R.id.btnCameraControl);

        // 2. Set the click listener so it actually DOES something
        btnSwitch.setOnClickListener(v -> {
            Intent intent = new Intent(CameraActivity.this, CameraControlsActivity.class);
            startActivity(intent);
        });
        // -----------------------------

        setupNavigation();
    }

    private void setupNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_camera);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, DashboardActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                return true;
            }
            else if (id == R.id.nav_camera) {
                return true;
            }
            else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, MotorControlsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
            else if (id == R.id.nav_config) {
                startActivity(new Intent(this, AboutUsActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }
            return false;
        });
    }
}