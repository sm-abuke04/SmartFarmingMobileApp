package com.example.capstoneproject.motors;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneproject.R;

import java.util.Calendar;

public class MotorAutomationActivity extends AppCompatActivity {

    Button btnPickDate, btnPickTime, btnSave, btnCancel;
    EditText etDuration;
    int motorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor_automation);

        // Bind views
        btnPickDate = findViewById(R.id.btnPickDate);
        btnPickTime = findViewById(R.id.btnPickTime);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        etDuration = findViewById(R.id.etDuration);

        // Get motor id from intent
        motorId = getIntent().getIntExtra("motor_id", 0);

        Calendar calendar = Calendar.getInstance();

        // Pick date
        btnPickDate.setOnClickListener(v -> {
            new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) ->
                            btnPickDate.setText((month + 1) + "/" + dayOfMonth + "/" + year),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // Pick time
        btnPickTime.setOnClickListener(v -> {
            new TimePickerDialog(
                    this,
                    (view, hourOfDay, minute) ->
                            btnPickTime.setText(String.format("%02d:%02d", hourOfDay, minute)),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
            ).show();
        });

        // Save schedule
        btnSave.setOnClickListener(v -> {
            String date = btnPickDate.getText().toString();
            String time = btnPickTime.getText().toString();
            String duration = etDuration.getText().toString();

            if(date.isEmpty() || time.isEmpty() || duration.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Send schedule to backend API or Arduino
            Toast.makeText(this, "Automation saved for Motor " + motorId, Toast.LENGTH_SHORT).show();
            finish();
        });

        // Cancel schedule
        btnCancel.setOnClickListener(v -> finish());
    }
}
