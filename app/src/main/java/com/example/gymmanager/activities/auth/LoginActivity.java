package com.example.gymmanager.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmanager.R;
import com.example.gymmanager.activities.client.ClientHomeActivity;
import com.example.gymmanager.network.AuthService;
import com.example.gymmanager.activities.admin.AdminHomeActivity;
import com.example.gymmanager.activities.monitor.MonitorHomeActivity;
import com.example.gymmanager.network.ProfileService;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
    }

    private void initViews() {

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);

        tvRegister = findViewById(R.id.tvRegister);
    }

    private void setupListeners() {

        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            btnLogin.setEnabled(false);
            btnLogin.setText("Iniciando...");

            AuthService.login(email, password, new AuthService.LoginCallback() {
                @Override
                public void onSuccess(String accessToken, String userId) {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Iniciar sesión");

                    ProfileService.getUserRole(userId, accessToken, new ProfileService.RoleCallback() {
                        @Override
                        public void onSuccess(String role) {
                            Intent intent;

                            switch (role) {
                                case "admin":
                                    intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                    break;

                                case "monitor":
                                    intent = new Intent(LoginActivity.this, MonitorHomeActivity.class);
                                    break;

                                case "cliente":
                                    intent = new Intent(LoginActivity.this, ClientHomeActivity.class);
                                    break;

                                default:
                                    Toast.makeText(LoginActivity.this, "Rol no válido", Toast.LENGTH_SHORT).show();
                                    return;
                            }

                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Iniciar sesión");

                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

        tvRegister.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });
    }
}