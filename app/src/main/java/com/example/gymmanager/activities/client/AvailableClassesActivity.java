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

public class AvailableClassesActivity extends AppCompatActivity {

    private RecyclerView recyclerClasses;

    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_classes);

        accessToken = getIntent().getStringExtra("accessToken");

        recyclerClasses = findViewById(R.id.recyclerClasses);

        recyclerClasses.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadClasses();
    }

    private void loadClasses() {

        ClassService.getActiveClasses(
                accessToken,
                new ClassService.GetClassesCallback() {

                    @Override
                    public void onSuccess(List<GymClass> classes) {

                        GymClassAdapter adapter =
                                new GymClassAdapter(classes);

                        recyclerClasses.setAdapter(adapter);
                    }

                    @Override
                    public void onError(String error) {

                        Toast.makeText(
                                AvailableClassesActivity.this,
                                error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}