package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstoneproject.api.RetrofitClient;
import com.example.capstoneproject.api.models.LoginRequest;
import com.example.capstoneproject.api.models.LoginResponse;
import com.example.capstoneproject.dashboard.DashboardActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button loginButton;
    private SharedPrefManager sharedPrefManager;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d(TAG, "LoginActivity onCreate");

        // Initialize SharedPrefManager
        sharedPrefManager = SharedPrefManager.getInstance(this);

        // Check if user is already logged in
        if (sharedPrefManager.isLoggedIn()) {
            Log.d(TAG, "User already logged in, redirecting to Dashboard");
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
            return;
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.btnLogin);

        loginButton.setOnClickListener(v -> {
            Log.d(TAG, "Login button clicked");
            performLogin();
        });
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Log.d(TAG, "Attempting login with email: " + email);

        // Validation
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            etEmail.requestFocus();
            return;
        }

        // Show loading state
        loginButton.setText("Logging in...");
        loginButton.setEnabled(false);

        // Create login request
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Make API call
        Call<LoginResponse> call = RetrofitClient.getInstance(getApplicationContext())
                .getApiService()
                .login(loginRequest);

        Log.d(TAG, "Making API call to: " + RetrofitClient.BASE_URL + "api/auth/signin");

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                // Reset button state
                loginButton.setText("Log in");
                loginButton.setEnabled(true);

                Log.d(TAG, "API Response received. Code: " + response.code() + ", Successful: " + response.isSuccessful());

                // Log response headers (to see cookies)
                Log.d(TAG, "Response Headers: " + response.headers());

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    Log.d(TAG, "Parsed Response: " + loginResponse.toString());

                    // Check if login was successful based on message or status
                    if (loginResponse.isLoginSuccessful()) {
                        Log.d(TAG, "Login successful!");

                        // Get user data
                        LoginResponse.UserData userData = loginResponse.getUser();

                        if (userData != null) {
                            String userId = userData.getUser() != null ? userData.getUser().toString() : "";
                            String userName = userData.getFull_name() != null ? userData.getFull_name() : "User";
                            String userEmail = email; // Use the email from login since it's not in response
                            String userRole = userData.getRole() != null ? userData.getRole() : "";

                            Log.d(TAG, "User data - ID: " + userId + ", Name: " + userName +
                                    ", Email: " + userEmail + ", Role: " + userRole);

                            // Save user session
                            // Note: We don't have a token in the response body, but we have it in the cookie
                            // The cookie will be automatically handled by Retrofit for subsequent requests
                            String sessionToken = "cookie_auth"; // Placeholder since token is in cookie
                            sharedPrefManager.userLogin(userId, userName, userEmail, sessionToken);
                            Log.d(TAG, "User session saved to SharedPreferences");

                            // Navigate to dashboard
                            try {
                                Log.d(TAG, "Starting DashboardActivity...");
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                Log.d(TAG, "DashboardActivity started successfully");
                                finish();
                                Log.d(TAG, "LoginActivity finished");
                            } catch (Exception e) {
                                Log.e(TAG, "Error starting DashboardActivity: " + e.getMessage(), e);
                                Toast.makeText(LoginActivity.this, "Error starting app: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG, "No user data in response");
                            Toast.makeText(LoginActivity.this, "Login failed: No user data received", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // Login failed with custom message
                        String errorMessage = loginResponse.getMessage() != null ?
                                loginResponse.getMessage() : "Login failed";
                        Log.w(TAG, "Login failed: " + errorMessage);
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Handle error response
                    Log.e(TAG, "Response not successful. Code: " + response.code());
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error response body: " + errorBody);

                            try {
                                JSONObject errorJson = new JSONObject(errorBody);
                                String errorMessage = errorJson.optString("message", "Login failed");
                                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                // If not JSON, show generic error
                                if (response.code() == 401) {
                                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                                } else if (response.code() == 404) {
                                    Toast.makeText(LoginActivity.this, "Server endpoint not found", Toast.LENGTH_LONG).show();
                                } else if (response.code() >= 500) {
                                    Toast.makeText(LoginActivity.this, "Server error, please try again later", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Login failed. Error code: " + response.code(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Unknown error occurred", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Error parsing response", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Reset button state
                loginButton.setText("Log in");
                loginButton.setEnabled(true);

                // Handle network failure
                Log.e(TAG, "Network error: ", t);

                // Check if it's a network connectivity issue
                if (t.getMessage() != null && t.getMessage().contains("Unable to resolve host")) {
                    Toast.makeText(LoginActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                } else if (t.getMessage() != null && t.getMessage().contains("timeout")) {
                    Toast.makeText(LoginActivity.this, "Connection timeout. Please try again", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}