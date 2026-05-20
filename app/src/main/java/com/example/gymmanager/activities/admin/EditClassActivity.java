package com.example.gymmanager.activities.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmanager.R;
import com.example.gymmanager.network.ClassService;

public class EditClassActivity extends AppCompatActivity {

    private EditText etEditClassName;
    private EditText etEditClassDescription;
    private EditText etEditClassSchedule;
    private EditText etEditClassCapacity;

    private Button btnUpdateClass;
    private Button btnDeleteClass;

    private String accessToken;
    private String classId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);

        accessToken = getIntent().getStringExtra("accessToken");
        classId = getIntent().getStringExtra("classId");

        initViews();
        loadIntentData();
        setupListeners();
    }

    private void initViews() {
        etEditClassName = findViewById(R.id.etEditClassName);
        etEditClassDescription = findViewById(R.id.etEditClassDescription);
        etEditClassSchedule = findViewById(R.id.etEditClassSchedule);
        etEditClassCapacity = findViewById(R.id.etEditClassCapacity);

        btnUpdateClass = findViewById(R.id.btnUpdateClass);
        btnDeleteClass = findViewById(R.id.btnDeleteClass);
    }

    private void loadIntentData() {
        etEditClassName.setText(getIntent().getStringExtra("nombre"));
        etEditClassDescription.setText(getIntent().getStringExtra("descripcion"));
        etEditClassSchedule.setText(getIntent().getStringExtra("horario"));

        int aforoMaximo = getIntent().getIntExtra("aforoMaximo", 0);
        etEditClassCapacity.setText(String.valueOf(aforoMaximo));
    }

    private void setupListeners() {
        btnUpdateClass.setOnClickListener(v -> updateClass());
        btnDeleteClass.setOnClickListener(v -> deleteClass());
    }

    private void updateClass() {
        String nombre = etEditClassName.getText().toString().trim();
        String descripcion = etEditClassDescription.getText().toString().trim();
        String horario = etEditClassSchedule.getText().toString().trim();
        String aforoText = etEditClassCapacity.getText().toString().trim();

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

        btnUpdateClass.setEnabled(false);
        btnUpdateClass.setText("Guardando...");

        ClassService.updateClass(
                accessToken,
                classId,
                nombre,
                descripcion,
                horario,
                aforoMaximo,
                new ClassService.ClassActionCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(
                                EditClassActivity.this,
                                "Clase actualizada correctamente",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        btnUpdateClass.setEnabled(true);
                        btnUpdateClass.setText("Guardar cambios");

                        Toast.makeText(
                                EditClassActivity.this,
                                error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void deleteClass() {
        btnDeleteClass.setEnabled(false);
        btnDeleteClass.setText("Eliminando...");

        ClassService.deleteClass(
                accessToken,
                classId,
                new ClassService.ClassActionCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(
                                EditClassActivity.this,
                                "Clase eliminada correctamente",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        btnDeleteClass.setEnabled(true);
                        btnDeleteClass.setText("Eliminar clase");

                        Toast.makeText(
                                EditClassActivity.this,
                                error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }
}