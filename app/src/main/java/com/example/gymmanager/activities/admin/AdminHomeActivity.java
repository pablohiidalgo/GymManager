package com.example.gymmanager.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmanager.R;

public class AdminHomeActivity extends AppCompatActivity {

    private LinearLayout cardClasses;
    private String accessToken;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        accessToken = getIntent().getStringExtra("accessToken");
        userId = getIntent().getStringExtra("userId");
        cardClasses = findViewById(R.id.cardClasses);

        cardClasses.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, ManageClassesActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }
}