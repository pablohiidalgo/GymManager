package com.example.gymmanager.activities.monitor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymmanager.R;
import com.example.gymmanager.adapters.MonitorClassAdapter;
import com.example.gymmanager.models.GymClass;
import com.example.gymmanager.network.ClassService;

import java.util.List;

public class MonitorClassesActivity extends AppCompatActivity {

    private RecyclerView recyclerMonitorClasses;

    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_classes);

        accessToken = getIntent().getStringExtra("accessToken");

        recyclerMonitorClasses = findViewById(R.id.recyclerMonitorClasses);
        recyclerMonitorClasses.setLayoutManager(new LinearLayoutManager(this));

        loadClasses();
    }

    private void loadClasses() {
        ClassService.getActiveClasses(
                accessToken,
                new ClassService.GetClassesCallback() {
                    @Override
                    public void onSuccess(List<GymClass> classes) {
                        MonitorClassAdapter adapter = new MonitorClassAdapter(
                                classes,
                                gymClass -> {
                                    Intent intent = new Intent(
                                            MonitorClassesActivity.this,
                                            AttendanceActivity.class
                                    );

                                    intent.putExtra("accessToken", accessToken);
                                    intent.putExtra("classId", gymClass.getId());

                                    startActivity(intent);
                                }
                        );

                        recyclerMonitorClasses.setAdapter(adapter);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(
                                MonitorClassesActivity.this,
                                error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }
}