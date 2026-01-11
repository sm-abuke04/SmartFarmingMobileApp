package com.example.capstoneproject;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgProfile;
    TextView tvName, tvRole, tvBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgProfile = findViewById(R.id.imgProfile);
        tvName = findViewById(R.id.tvName);
        tvRole = findViewById(R.id.tvRole);
        tvBio = findViewById(R.id.tvBio);

        // Get name from intent
        String name = getIntent().getStringExtra("name");

        if(name != null) {
            tvName.setText(name);

            // Example logic to set details per member
            switch(name) {
                case "SM Andrie A. Abuke":
                    imgProfile.setImageResource(R.drawable.sm);
                    tvRole.setText("Lead Android Developer");
                    tvBio.setText("Responsible for developing the Android app, implementing core features, and overseeing development workflow.");
                    break;

                case "Bomel G. Morado":
                    imgProfile.setImageResource(R.drawable.bomel);
                    tvRole.setText("UI/UX Designer and Frontend Architect");
                    tvBio.setText("I don't count mistakes");
                    break;
            }
        }
    }
}
