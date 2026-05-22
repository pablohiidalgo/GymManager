package com.example.gymmanager.activities.client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmanager.LoginActivity;
import com.example.gymmanager.R;
import com.example.gymmanager.utils.AnimationHelper;
import com.example.gymmanager.utils.SessionManager;

public class ClientHomeActivity extends AppCompatActivity {

    private LinearLayout cardAvailableClasses;
    private LinearLayout cardReservations;
    private LinearLayout cardPayment;
    private LinearLayout cardProfile;

    private Button btnLogout;

    private String accessToken;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);
        AnimationHelper.applyOpenTransition(this);
        AnimationHelper.fadeIn(findViewById(android.R.id.content));
        accessToken = getIntent().getStringExtra("accessToken");
        userId = getIntent().getStringExtra("userId");

        cardAvailableClasses = findViewById(R.id.cardAvailableClasses);
        cardReservations = findViewById(R.id.cardReservations);
        cardPayment = findViewById(R.id.cardPayment);
        cardProfile = findViewById(R.id.cardProfile);

        btnLogout = findViewById(R.id.btnLogout);

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

        cardPayment.setOnClickListener(v -> {
            Intent intent = new Intent(ClientHomeActivity.this, PaymentActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ClientHomeActivity.this, ProfileActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            SessionManager.clearSession(this);

            Intent intent = new Intent(ClientHomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            finish();
        });
    }
}