package com.example.gymmanager.activities.admin;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmanager.R;
import com.example.gymmanager.models.AdminStats;
import com.example.gymmanager.network.ProfileService;

public class AdminStatsActivity extends AppCompatActivity {

    private TextView tvTotalUsers;
    private TextView tvTotalClasses;
    private TextView tvTotalReservations;

    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_stats);

        accessToken = getIntent().getStringExtra("accessToken");

        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvTotalClasses = findViewById(R.id.tvTotalClasses);
        tvTotalReservations = findViewById(R.id.tvTotalReservations);

        loadStats();
    }

    private void loadStats() {

        ProfileService.getAdminStats(
                accessToken,
                new ProfileService.AdminStatsCallback() {

                    @Override
                    public void onSuccess(AdminStats stats) {

                        tvTotalUsers.setText(
                                String.valueOf(
                                        stats.getTotalUsers()
                                )
                        );

                        tvTotalClasses.setText(
                                String.valueOf(
                                        stats.getTotalClasses()
                                )
                        );

                        tvTotalReservations.setText(
                                String.valueOf(
                                        stats.getTotalReservations()
                                )
                        );
                    }

                    @Override
                    public void onError(String error) {

                        Toast.makeText(
                                AdminStatsActivity.this,
                                error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }
}