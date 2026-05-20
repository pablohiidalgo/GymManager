package com.example.gymmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmanager.activities.admin.AdminHomeActivity;
import com.example.gymmanager.activities.client.ClientHomeActivity;
import com.example.gymmanager.activities.auth.RegisterActivity;
import com.example.gymmanager.activities.monitor.MonitorHomeActivity;
import com.example.gymmanager.network.AuthService;
import com.example.gymmanager.network.ProfileService;
import com.example.gymmanager.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;

    private Button btnLogin;

    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);

        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> login());

        tvRegister.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            LoginActivity.this,
                            RegisterActivity.class
                    );

            startActivity(intent);
        });
    }

    private void login() {

        String email =
                etEmail.getText().toString().trim();

        String password =
                etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {

            Toast.makeText(
                    this,
                    "Completa todos los campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Iniciando sesión...");

        AuthService.login(
                email,
                password,
                new AuthService.LoginCallback() {

                    @Override
                    public void onSuccess(
                            String accessToken,
                            String userId
                    ) {

                        loadUserRole(
                                accessToken,
                                userId
                        );
                    }

                    @Override
                    public void onError(String errorMessage) {

                        btnLogin.setEnabled(true);
                        btnLogin.setText("Iniciar sesión");

                        Toast.makeText(
                                LoginActivity.this,
                                errorMessage,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void loadUserRole(
            String accessToken,
            String userId
    ) {

        ProfileService.getUserRole(
                userId,
                accessToken,
                new ProfileService.RoleCallback() {

                    @Override
                    public void onSuccess(String role) {

                        SessionManager.saveSession(
                                LoginActivity.this,
                                accessToken,
                                userId,
                                role
                        );

                        Intent intent;

                        switch (role) {

                            case "admin":

                                intent = new Intent(
                                        LoginActivity.this,
                                        AdminHomeActivity.class
                                );

                                break;

                            case "monitor":

                                intent = new Intent(
                                        LoginActivity.this,
                                        MonitorHomeActivity.class
                                );

                                break;

                            default:

                                intent = new Intent(
                                        LoginActivity.this,
                                        ClientHomeActivity.class
                                );

                                break;
                        }

                        intent.putExtra(
                                "accessToken",
                                accessToken
                        );

                        intent.putExtra(
                                "userId",
                                userId
                        );

                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String error) {

                        btnLogin.setEnabled(true);
                        btnLogin.setText("Iniciar sesión");

                        Toast.makeText(
                                LoginActivity.this,
                                error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }
}