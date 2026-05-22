package com.example.gymmanager.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmanager.R;
import com.example.gymmanager.LoginActivity;
import com.example.gymmanager.utils.AnimationHelper;
import com.example.gymmanager.utils.SessionManager;

public class AdminHomeActivity extends AppCompatActivity {

    private LinearLayout cardManageClasses;
    private LinearLayout cardAdminStats;

    private Button btnLogout;

    private String accessToken;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        AnimationHelper.applyOpenTransition(this);
        AnimationHelper.fadeIn(findViewById(android.R.id.content));
        accessToken = getIntent().getStringExtra("accessToken");
        userId = getIntent().getStringExtra("userId");

        cardManageClasses = findViewById(R.id.cardManageClasses);
        cardAdminStats = findViewById(R.id.cardAdminStats);

        btnLogout = findViewById(R.id.btnLogout);

        cardManageClasses.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminHomeActivity.this,
                    ManageClassesActivity.class
            );

            intent.putExtra("accessToken", accessToken);
            intent.putExtra("userId", userId);

            startActivity(intent);
        });

        cardAdminStats.setOnClickListener(v -> {

            Intent intent = new Intent(
                    AdminHomeActivity.this,
                    AdminStatsActivity.class
            );

            intent.putExtra("accessToken", accessToken);

            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {

            SessionManager.clearSession(this);

            Intent intent = new Intent(
                    AdminHomeActivity.this,
                    LoginActivity.class
            );

            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );

            startActivity(intent);
            finish();
        });
    }
}