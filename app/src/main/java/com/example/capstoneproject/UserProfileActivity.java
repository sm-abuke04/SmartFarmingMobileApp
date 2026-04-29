//package com.example.capstoneproject;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.example.capstoneproject.api.ApiClient;
//import com.example.capstoneproject.api.DashboardApi;
//import com.example.capstoneproject.api.RetrofitClient;
//import com.example.capstoneproject.user_profile.UserProfileResponse;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class UserProfileActivity extends AppCompatActivity {
//
//    private static final String TAG = "UserProfileActivity";
//    LinearLayout btnLogout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_profile);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        // 1. Setup Buttons
//        ImageButton btnBack = findViewById(R.id.btnBack);
//        btnBack.setOnClickListener(v -> finish());
//
//        btnLogout = findViewById(R.id.btnLogout);
//        btnLogout.setOnClickListener(v -> {
//            Log.d(TAG, "Logout button clicked - performing logout");
//            performLogout();
//        });
//
//        // 2. Fetch User Data Dynamically
//        fetchUserData();
//    }
//
//    private void fetchUserData() {
//        // Retrieve the logged-in user's ID
//        String userId = SharedPrefManager.getInstance(this).getUserId();
//
//        if (userId == null || userId.isEmpty()) {
//            Toast.makeText(this, "Error: User ID not found.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Log.d(TAG, "Fetching data for User ID: " + userId);
//
//        // Make API Call
//        DashboardApi api = ApiClient.getClient(this).create(DashboardApi.class);
//        api.getUserProfile(userId).enqueue(new Callback<UserProfileResponse>() {
//            @Override
//            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    UserProfileResponse user = response.body();
//
//                    // Update UI on the main thread
//                    runOnUiThread(() -> updateUI(user));
//                } else {
//                    Log.e(TAG, "Failed to load profile. Code: " + response.code());
//                    Toast.makeText(UserProfileActivity.this, "Failed to load profile data", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
//                Log.e(TAG, "Network error fetching profile", t);
//                Toast.makeText(UserProfileActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void updateUI(UserProfileResponse user) {
//        // Populate the specific layout rows dynamically
//        setupDetailRow(findViewById(R.id.rowFirstName), "First Name", user.getFirstName());
//        setupDetailRow(findViewById(R.id.rowMiddleName), "Middle Name", user.getMiddleName());
//        setupDetailRow(findViewById(R.id.rowLastName), "Last Name", user.getLastName());
//        setupDetailRow(findViewById(R.id.rowExtensionName), "Extension", user.getExtensionName());
//
//        setupDetailRow(findViewById(R.id.rowGender), "Gender", user.getGender());
//        setupDetailRow(findViewById(R.id.rowEmail), "Email", user.getEmail());
//        setupDetailRow(findViewById(R.id.rowPhone), "Phone", user.getPhoneNumber());
//        setupDetailRow(findViewById(R.id.rowRole), "Role", user.getRole());
//        setupDetailRow(findViewById(R.id.rowStatus), "Status", user.getStatus());
//
//        // Note: You can also use Glide here to load user.getUserImage() into ivUserProfile if desired.
//    }
//
//    private void setupDetailRow(View view, String label, String value) {
//        if (view == null) return; // Safety check
//
//        TextView tvLabel = view.findViewById(R.id.tvLabel);
//        TextView tvValue = view.findViewById(R.id.tvValue);
//
//        tvLabel.setText(label);
//        // Handle nulls gracefully
//        tvValue.setText((value != null && !value.equalsIgnoreCase("null") && !value.isEmpty()) ? value : "N/A");
//    }
//
//    private void performLogout() {
//        Log.d(TAG, "=== STARTING LOGOUT PROCESS ===");
//        try {
//            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
//            RetrofitClient.getInstance(getApplicationContext()).clearCookies();
//            SharedPrefManager.getInstance(this).logout();
//
//            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//
//            Log.d(TAG, "=== LOGOUT COMPLETED SUCCESSFULLY ===");
//        } catch (Exception e) {
//            Log.e(TAG, "Error during logout: " + e.getMessage(), e);
//            Toast.makeText(this, "Logout error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    }
//}

package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstoneproject.api.ApiService;
import com.example.capstoneproject.api.RetrofitClient;
import com.example.capstoneproject.user_profile.UserProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserProfileActivity extends AppCompatActivity {


//    private static final String TAG = "UserProfileActivity";
//    LinearLayout btnLogout;

    private static final String TAG = "UserProfileActivity";
    private LinearLayout btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {
            Log.d(TAG, "Logout button clicked - performing logout");
            performLogout();
        });


//        // Example of how to populate one of the rows
//        setupDetailRow(findViewById(R.id.rowFirstName), "First Name", "Bomel");
//        setupDetailRow(findViewById(R.id.rowMiddleName), "Middle Name","Gumatay");
//        setupDetailRow(findViewById(R.id.rowLastName), "Last Name", "Morado");
//        setupDetailRow(findViewById(R.id.rowExtensionName), "Extension Name", "Manaralsal");
//        setupDetailRow(findViewById(R.id.rowGender), "Gender", "Male");
//        setupDetailRow(findViewById(R.id.rowEmail), "Email", "farmer@uepsmartfarming.com");
//        setupDetailRow(findViewById(R.id.rowPhone), "Phone Number", "09123456789");
//        setupDetailRow(findViewById(R.id.rowRole), "Role", "Farm Technician");
//        setupDetailRow(findViewById(R.id.rowStatus), "Status", "Active");
        fetchUserData();
    }

//    private void setupDetailRow(View view, String label, String value) {
//        TextView tvLabel = view.findViewById(R.id.tvLabel);
//        TextView tvValue = view.findViewById(R.id.tvValue);
//        tvLabel.setText(label);
//        tvValue.setText(value != null && !value.equals("null") ? value : "N/A");

    private void fetchUserData() {
        String userId = SharedPrefManager.getInstance(this).getUserId();
        Log.d(TAG, "Fetching data for User ID: " + userId);

        // Use the ApiService from your RetrofitClient
        ApiService api = RetrofitClient.getInstance(this).getApiService();

        // Make sure getUserProfile is defined in ApiService (as shown in my previous response)
        api.getUserProfile(userId).enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfileResponse user = response.body();
                    updateUI(user);
                } else {
                    // If you see "Session Expired" here, the Token/Interceptor is the culprit
                    Log.e(TAG, "Error: " + response.code() + " " + response.message());
                    Toast.makeText(UserProfileActivity.this, "Session Expired. Please login again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.e(TAG, "Network Error", t);
            }
        });
    }

    private void updateUI(UserProfileResponse user) {
        setupDetailRow(findViewById(R.id.rowFirstName), "First Name", user.getFirstName());
        setupDetailRow(findViewById(R.id.rowMiddleName), "Middle Name", user.getMiddleName());
        setupDetailRow(findViewById(R.id.rowLastName), "Last Name", user.getLastName());
        setupDetailRow(findViewById(R.id.rowExtensionName), "Extension Name", user.getExtensionName());
        setupDetailRow(findViewById(R.id.rowGender), "Gender", user.getGender());
        setupDetailRow(findViewById(R.id.rowEmail), "Email", user.getEmail());
        setupDetailRow(findViewById(R.id.rowPhone), "Phone Number", user.getPhoneNumber());
        setupDetailRow(findViewById(R.id.rowRole), "Role", user.getRole());
        setupDetailRow(findViewById(R.id.rowStatus), "Status", user.getStatus());
    }

    private void setupDetailRow(View view, String label, String value) {
        if (view == null) return;
        TextView tvLabel = view.findViewById(R.id.tvLabel);
        TextView tvValue = view.findViewById(R.id.tvValue);

        tvLabel.setText(label);
        // Handle null values from the JSON (like middle_name in your example)
        if (value == null || value.equalsIgnoreCase("null") || value.isEmpty()) {
            tvValue.setText("N/A");
        } else {
            tvValue.setText(value);
        }
    }

    private void performLogout() {
        Log.d(TAG, "=== STARTING LOGOUT PROCESS ===");

        try {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            RetrofitClient.getInstance(getApplicationContext()).clearCookies();
            SharedPrefManager.getInstance(this).logout();

            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
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