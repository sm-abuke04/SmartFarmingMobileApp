package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstoneproject.api.RetrofitClient;

public class AboutUsActivity extends AppCompatActivity {

    private static final String TAG = "AboutUsActivity";
    ImageButton btnBack;
    LinearLayout btnLogout;
    CardView cardSM, cardBomel; // Team member cards

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about_us);

        // Apply window insets for proper padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d(TAG, "AboutUsActivity onCreate");

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        btnLogout = findViewById(R.id.btnLogout);

        // Initialize team member cards
        cardSM = findViewById(R.id.cardSM);
        cardBomel = findViewById(R.id.cardBomel);

        // Back button click
        btnBack.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            finish();
        });

        // Logout click
        btnLogout.setOnClickListener(v -> {
            Log.d(TAG, "Logout button clicked - performing logout");
            performLogout();
        });

        // Team member clicks
        cardSM.setOnClickListener(v -> {
        Intent intent = new Intent(AboutUsActivity.this, ProfileSMActivity.class);
         startActivity(intent);
      });

        cardBomel.setOnClickListener(v -> {
            Intent intent = new Intent(AboutUsActivity.this, ProfileBomelActivity.class);
            startActivity(intent);
        });
    }

    private void performLogout() {
        Log.d(TAG, "=== STARTING LOGOUT PROCESS ===");

        try {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            RetrofitClient.getInstance(getApplicationContext()).clearCookies();
            SharedPrefManager.getInstance(this).logout();

            Intent intent = new Intent(AboutUsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

            Log.d(TAG, "=== LOGOUT COMPLETED SUCCESSFULLY ===");
        } catch (Exception e) {
            Log.e(TAG, "Error during logout: " + e.getMessage(), e);
            Toast.makeText(this, "Logout error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
