package com.example.capstoneproject.motors;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.capstoneproject.R;
import com.example.capstoneproject.api.RetrofitClient;
import com.example.capstoneproject.api.ApiService;
import com.example.capstoneproject.api.models.AutomationRequest;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MotorInfoActivity extends AppCompatActivity {

    // Views
    private TextView tvTitle, tvMotorName, tvMotorStatus;
    private View viewStatusIndicator;
    private Switch autoSwitch;
    private LinearLayout scheduleLayout, historyContainer;
    private TextView tvScheduleDate, tvScheduleTime, tvDuration;
    private Button btnSetDate, btnSetTime, btnSetDuration, btnBack, btnSaveAutomation;

    // Motor info
    private int motorId;
    private String motorStatus;

    // SharedPreferences
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "MotorPrefs";
    private static final String KEY_DATE = "schedule_date";
    private static final String KEY_TIME = "schedule_time";
    private static final String KEY_DURATION = "schedule_duration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor_info);

        // Bind views
        tvTitle = findViewById(R.id.tvTitle);
        tvMotorName = findViewById(R.id.tvMotorName);
        tvMotorStatus = findViewById(R.id.tvMotorStatus);
        viewStatusIndicator = findViewById(R.id.viewStatusIndicator);

        autoSwitch = findViewById(R.id.autoSwitch);
        scheduleLayout = findViewById(R.id.scheduleLayout);
        historyContainer = findViewById(R.id.historyContainer);

        tvScheduleDate = findViewById(R.id.tvScheduleDate);
        tvScheduleTime = findViewById(R.id.tvScheduleTime);
        tvDuration = findViewById(R.id.tvDuration);

        btnSetDate = findViewById(R.id.btnSetDate);
        btnSetTime = findViewById(R.id.btnSetTime);
        btnSetDuration = findViewById(R.id.btnSetDuration);
        btnBack = findViewById(R.id.btnBack);
        btnSaveAutomation = findViewById(R.id.btnSaveSchedule);

        // SharedPreferences
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Get motor info from intent
        if (getIntent() != null) {
            motorId = getIntent().getIntExtra("motor_id", -1);
            motorStatus = getIntent().getStringExtra("motor_status");
        }

        if (motorId == -1 || motorStatus == null) {
            Toast.makeText(this, "Invalid motor info", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set UI
        tvTitle.setText("Motor " + motorId + " Information");
        tvMotorName.setText("Motor " + motorId);
        setMotorStatus(motorStatus);

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Automation toggle
        autoSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                scheduleLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE));

        // Date picker
        btnSetDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) ->
                    tvScheduleDate.setText(y + "-" + (m + 1) + "-" + d),
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // Time picker
        btnSetTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this, (view, h, m) ->
                    tvScheduleTime.setText(String.format("%02d:%02d", h, m)),
                    c.get(Calendar.HOUR_OF_DAY),
                    c.get(Calendar.MINUTE),
                    true
            ).show();
        });

        // Duration picker
        btnSetDuration.setOnClickListener(v -> {
            NumberPicker picker = new NumberPicker(this);
            picker.setMinValue(1);
            picker.setMaxValue(120);
            int current = tvDuration.getText().toString().contains("mins") ?
                    Integer.parseInt(tvDuration.getText().toString().replace(" mins", "")) : 30;
            picker.setValue(current);

            LinearLayout layout = new LinearLayout(this);
            layout.setPadding(50, 40, 50, 10);
            layout.addView(picker);

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Select Duration (minutes)")
                    .setView(layout)
                    .setPositiveButton("OK", (dialog, which) ->
                            tvDuration.setText(picker.getValue() + " mins"))
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // Hide schedule initially
        scheduleLayout.setVisibility(View.GONE);

        // Load saved schedule
        loadSchedule();

        // Save automation
        btnSaveAutomation.setOnClickListener(v -> saveAutomation());
    }

    private void setMotorStatus(String status) {
        tvMotorStatus.setText(status);

        switch (status.toUpperCase()) {
            case "RUNNING":
                viewStatusIndicator.setBackgroundResource(R.drawable.bg_circle_indicator_green);
                tvMotorStatus.setTextColor(ContextCompat.getColor(this, R.color.green));
                break;
            case "OFF":
                viewStatusIndicator.setBackgroundResource(R.drawable.bg_circle_indicator_gray);
                tvMotorStatus.setTextColor(ContextCompat.getColor(this, R.color.gray));
                break;
            case "RESERVE":
                viewStatusIndicator.setBackgroundResource(R.drawable.bg_circle_indicator_red);
                tvMotorStatus.setTextColor(ContextCompat.getColor(this, R.color.red_off));
                break;
            default:
                viewStatusIndicator.setBackgroundResource(R.drawable.bg_circle_indicator_gray);
                tvMotorStatus.setTextColor(ContextCompat.getColor(this, R.color.gray));
                break;
        }
    }

    private void saveAutomation() {
        String date = tvScheduleDate.getText().toString().trim();
        String time = tvScheduleTime.getText().toString().trim();
        String durationText = tvDuration.getText().toString().trim();

        if (date.isEmpty() || time.isEmpty() || durationText.isEmpty()) {
            Toast.makeText(this, "Please set date, time, and duration", Toast.LENGTH_SHORT).show();
            return;
        }

        int duration;
        try {
            duration = Integer.parseInt(durationText.replace(" mins", ""));
        } catch (Exception e) {
            Toast.makeText(this, "Invalid duration", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save locally
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_DATE + motorId, date);
        editor.putString(KEY_TIME + motorId, time);
        editor.putString(KEY_DURATION + motorId, durationText);
        editor.apply();

        // Build AutomationRequest
        AutomationRequest automationRequest = new AutomationRequest();
        automationRequest.setDate(date);
        automationRequest.setTime(time);
        automationRequest.setDuration(duration);
        automationRequest.setEnabled(autoSwitch.isChecked());

        // Send to API
        ApiService apiService = RetrofitClient.getInstance(this).getApiService();
        apiService.setAutomation(motorId, automationRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MotorInfoActivity.this,
                            "Automation saved to server", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MotorInfoActivity.this,
                            "Server error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MotorInfoActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Update history UI
        TextView tvHistory = historyContainer.findViewById(
                getResources().getIdentifier(
                        "tvHistoryMotor" + motorId,
                        "id",
                        getPackageName()
                )
        );

        if (tvHistory != null) {
            tvHistory.setText(
                    "Motor " + motorId + ": " +
                            (autoSwitch.isChecked() ? "ON" : "OFF") +
                            " | Last: " + time + " " + date
            );
        }
    }

    private void loadSchedule() {
        String date = prefs.getString(KEY_DATE + motorId, "");
        String time = prefs.getString(KEY_TIME + motorId, "");
        String duration = prefs.getString(KEY_DURATION + motorId, "");

        if (!date.isEmpty() && !time.isEmpty() && !duration.isEmpty()) {
            tvScheduleDate.setText(date);
            tvScheduleTime.setText(time);
            tvDuration.setText(duration);
            scheduleLayout.setVisibility(View.VISIBLE);
            autoSwitch.setChecked(true);
        }
    }
}
