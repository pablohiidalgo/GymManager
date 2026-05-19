package com.example.gymmanager.activities.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmanager.R;
import com.example.gymmanager.network.ClassService;

public class ManageClassesActivity extends AppCompatActivity {

    private EditText etClassName, etClassDescription, etClassSchedule, etClassCapacity;
    private Button btnCreateClass;

    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_classes);

        accessToken = getIntent().getStringExtra("accessToken");

        initViews();
        setupListeners();
    }

    private void initViews() {
        etClassName = findViewById(R.id.etClassName);
        etClassDescription = findViewById(R.id.etClassDescription);
        etClassSchedule = findViewById(R.id.etClassSchedule);
        etClassCapacity = findViewById(R.id.etClassCapacity);
        btnCreateClass = findViewById(R.id.btnCreateClass);
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

    private void clearFields() {
        etClassName.setText("");
        etClassDescription.setText("");
        etClassSchedule.setText("");
        etClassCapacity.setText("");
    }
}