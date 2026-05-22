package com.example.gymmanager.activities.monitor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmanager.R;
import com.example.gymmanager.LoginActivity;
import com.example.gymmanager.utils.AnimationHelper;
import com.example.gymmanager.utils.SessionManager;

public class MonitorHomeActivity extends AppCompatActivity {

    private LinearLayout cardMonitorClasses;

    private Button btnLogout;

    private String accessToken;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_home);
        AnimationHelper.applyOpenTransition(this);
        AnimationHelper.fadeIn(findViewById(android.R.id.content));
        accessToken = getIntent().getStringExtra("accessToken");
        userId = getIntent().getStringExtra("userId");

        cardMonitorClasses = findViewById(R.id.cardMonitorClasses);

        btnLogout = findViewById(R.id.btnLogout);

        cardMonitorClasses.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MonitorHomeActivity.this,
                    MonitorClassesActivity.class
            );

            intent.putExtra("accessToken", accessToken);
            intent.putExtra("userId", userId);

            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {

            SessionManager.clearSession(this);

            Intent intent = new Intent(
                    MonitorHomeActivity.this,
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