package com.example.gymmanager.activities.auth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmanager.R;
import com.example.gymmanager.network.AuthService;
import com.example.gymmanager.network.ProfileService;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNombre, etApellidos, etTelefono, etEmail, etPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        etTelefono = findViewById(R.id.etTelefono);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> registerClient());
    }

    private void registerClient() {
        String nombre = etNombre.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (nombre.isEmpty() || apellidos.isEmpty() || telefono.isEmpty()
                || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRegister.setEnabled(false);
        btnRegister.setText("Registrando...");

        AuthService.register(email, password, new AuthService.RegisterCallback() {
            @Override
            public void onSuccess(String accessToken, String userId) {

                ProfileService.createClientProfile(
                        userId,
                        accessToken,
                        nombre,
                        apellidos,
                        telefono,
                        new ProfileService.CreateProfileCallback() {
                            @Override
                            public void onSuccess() {

                                ProfileService.createClientMember(
                                        userId,
                                        accessToken,
                                        new ProfileService.CreateMemberCallback() {
                                            @Override
                                            public void onSuccess() {
                                                Toast.makeText(
                                                        RegisterActivity.this,
                                                        "Cuenta creada correctamente",
                                                        Toast.LENGTH_SHORT
                                                ).show();

                                                finish();
                                            }

                                            @Override
                                            public void onError(String error) {
                                                resetButton();
                                                Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                );
                            }

                            @Override
                            public void onError(String error) {
                                resetButton();
                                Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }

            @Override
            public void onError(String errorMessage) {
                resetButton();
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetButton() {
        btnRegister.setEnabled(true);
        btnRegister.setText("Registrarse");
    }
}