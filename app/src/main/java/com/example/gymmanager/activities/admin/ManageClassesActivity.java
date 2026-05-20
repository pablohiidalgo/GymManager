package com.example.gymmanager.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymmanager.R;
import com.example.gymmanager.adapters.AdminClassAdapter;
import com.example.gymmanager.models.GymClass;
import com.example.gymmanager.network.ClassService;

import java.util.List;

public class ManageClassesActivity extends AppCompatActivity {

    private EditText etClassName, etClassDescription, etClassSchedule, etClassCapacity;
    private Button btnCreateClass;
    private RecyclerView recyclerAdminClasses;

    private String accessToken;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_classes);

        accessToken = getIntent().getStringExtra("accessToken");
        userId = getIntent().getStringExtra("userId");

        initViews();
        setupListeners();
        loadClasses();
    }

    private void initViews() {
        etClassName = findViewById(R.id.etClassName);
        etClassDescription = findViewById(R.id.etClassDescription);
        etClassSchedule = findViewById(R.id.etClassSchedule);
        etClassCapacity = findViewById(R.id.etClassCapacity);
        btnCreateClass = findViewById(R.id.btnCreateClass);
        recyclerAdminClasses = findViewById(R.id.recyclerAdminClasses);

        recyclerAdminClasses.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        btnCreateClass.setOnClickListener(v -> createClass());
    }

    private void createClass() {
        String nombre = etClassName.getText().toString().trim();
        String descripcion = etClassDescription.getText().toString().trim();
        String horario = etClassSchedule.getText().toString().trim();
        String aforoText = etClassCapacity.getText().toString().trim();

        if (nombre.isEmpty() || descripcion.isEmpty() || horario.isEmpty() || aforoText.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int aforoMaximo;

        try {
            aforoMaximo = Integer.parseInt(aforoText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El aforo debe ser un número válido", Toast.LENGTH_SHORT).show();
            return;
        }

        btnCreateClass.setEnabled(false);
        btnCreateClass.setText("Creando...");

        ClassService.createClass(
                accessToken,
                nombre,
                descripcion,
                horario,
                aforoMaximo,
                new ClassService.CreateClassCallback() {
                    @Override
                    public void onSuccess() {
                        btnCreateClass.setEnabled(true);
                        btnCreateClass.setText("Crear clase");

                        Toast.makeText(
                                ManageClassesActivity.this,
                                "Clase creada correctamente",
                                Toast.LENGTH_SHORT
                        ).show();

                        clearFields();
                        loadClasses();
                    }

                    @Override
                    public void onError(String error) {
                        btnCreateClass.setEnabled(true);
                        btnCreateClass.setText("Crear clase");

                        Toast.makeText(
                                ManageClassesActivity.this,
                                error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void loadClasses() {
        ClassService.getActiveClasses(
                accessToken,
                new ClassService.GetClassesCallback() {
                    @Override
                    public void onSuccess(List<GymClass> classes) {
                        AdminClassAdapter adapter = new AdminClassAdapter(
                                classes,
                                gymClass -> {
                                    Intent intent = new Intent(
                                            ManageClassesActivity.this,
                                            EditClassActivity.class
                                    );

                                    intent.putExtra("accessToken", accessToken);
                                    intent.putExtra("userId", userId);
                                    intent.putExtra("classId", gymClass.getId());
                                    intent.putExtra("nombre", gymClass.getNombre());
                                    intent.putExtra("descripcion", gymClass.getDescripcion());
                                    intent.putExtra("horario", gymClass.getHorario());
                                    intent.putExtra("aforoMaximo", gymClass.getAforoMaximo());

                                    startActivity(intent);
                                }
                        );

                        recyclerAdminClasses.setAdapter(adapter);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(
                                ManageClassesActivity.this,
                                error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void clearFields() {
        etClassName.setText("");
        etClassDescription.setText("");
        etClassSchedule.setText("");
        etClassCapacity.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accessToken != null) {
            loadClasses();
        }
    }
}