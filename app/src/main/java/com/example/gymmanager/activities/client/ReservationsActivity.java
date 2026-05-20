package com.example.gymmanager.activities.client;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymmanager.R;
import com.example.gymmanager.adapters.GymClassAdapter;
import com.example.gymmanager.models.GymClass;
import com.example.gymmanager.network.ClassService;

import java.util.List;

public class ReservationsActivity extends AppCompatActivity {

    private RecyclerView recyclerReservations;
    private String accessToken;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        accessToken = getIntent().getStringExtra("accessToken");
        userId = getIntent().getStringExtra("userId");

        recyclerReservations = findViewById(R.id.recyclerReservations);
        recyclerReservations.setLayoutManager(new LinearLayoutManager(this));

        loadReservations();
    }

    private void loadReservations() {
        ClassService.getUserReservations(
                accessToken,
                userId,
                new ClassService.GetClassesCallback() {
                    @Override
                    public void onSuccess(List<GymClass> classes) {
                        GymClassAdapter adapter = new GymClassAdapter(
                                classes,
                                gymClass -> {}
                        );

                        recyclerReservations.setAdapter(adapter);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(
                                ReservationsActivity.this,
                                error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }
}