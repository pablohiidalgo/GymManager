package com.example.gymmanager.activities.monitor;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymmanager.R;
import com.example.gymmanager.adapters.AttendanceAdapter;
import com.example.gymmanager.models.Reservation;
import com.example.gymmanager.network.ClassService;
import com.example.gymmanager.utils.AnimationHelper;

import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    private RecyclerView recyclerAttendance;

    private String accessToken;
    private String classId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        AnimationHelper.applyOpenTransition(this);
        AnimationHelper.fadeIn(findViewById(android.R.id.content));
        accessToken = getIntent().getStringExtra("accessToken");
        classId = getIntent().getStringExtra("classId");

        recyclerAttendance =
                findViewById(R.id.recyclerAttendance);

        recyclerAttendance.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadReservations();
    }

    private void loadReservations() {

        ClassService.getReservationsByClass(
                accessToken,
                classId,
                new ClassService.ReservationsCallback() {

                    @Override
                    public void onSuccess(List<Reservation> reservations) {

                        AttendanceAdapter adapter =
                                new AttendanceAdapter(
                                        reservations,
                                        reservation ->
                                                markAttendance(
                                                        reservation.getReservaId()
                                                )
                                );

                        recyclerAttendance.setAdapter(adapter);
                    }

                    @Override
                    public void onError(String error) {

                        Toast.makeText(
                                AttendanceActivity.this,
                                error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void markAttendance(String reservationId) {

        ClassService.markAttendance(
                accessToken,
                reservationId,
                new ClassService.AttendanceCallback() {

                    @Override
                    public void onSuccess() {

                        Toast.makeText(
                                AttendanceActivity.this,
                                "Asistencia marcada",
                                Toast.LENGTH_SHORT
                        ).show();

                        loadReservations();
                    }

                    @Override
                    public void onError(String error) {

                        Toast.makeText(
                                AttendanceActivity.this,
                                error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }
}