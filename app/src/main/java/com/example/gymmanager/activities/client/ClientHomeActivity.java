package com.example.gymmanager.activities.client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmanager.R;

public class ClientHomeActivity extends AppCompatActivity {

    private LinearLayout cardAvailableClasses, cardReservations;
    private String accessToken;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        accessToken = getIntent().getStringExtra("accessToken");
        userId = getIntent().getStringExtra("userId");

        cardAvailableClasses = findViewById(R.id.cardAvailableClasses);
        cardReservations = findViewById(R.id.cardReservations);

        cardAvailableClasses.setOnClickListener(v -> {
            Intent intent = new Intent(ClientHomeActivity.this, AvailableClassesActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        cardReservations.setOnClickListener(v -> {
            Intent intent = new Intent(ClientHomeActivity.this, ReservationsActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }
}