package com.example.gymmanager.activities.client;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmanager.R;
import com.example.gymmanager.network.ProfileService;
import com.example.gymmanager.utils.AnimationHelper;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvCardNumber, tvCardHolder, tvCardDate, tvPaymentStatus;

    private EditText etCardNumber, etCardHolder, etCardDate, etCardCVV;

    private Button btnPay;

    private String accessToken;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        AnimationHelper.applyOpenTransition(this);
        AnimationHelper.fadeIn(findViewById(android.R.id.content));
        accessToken = getIntent().getStringExtra("accessToken");
        userId = getIntent().getStringExtra("userId");

        initViews();
        setupTextWatchers();
        setupListeners();
        loadPayment();
    }

    private void initViews() {
        tvCardNumber = findViewById(R.id.tvCardNumber);
        tvCardHolder = findViewById(R.id.tvCardHolder);
        tvCardDate = findViewById(R.id.tvCardDate);
        tvPaymentStatus = findViewById(R.id.tvPaymentStatus);

        etCardNumber = findViewById(R.id.etCardNumber);
        etCardHolder = findViewById(R.id.etCardHolder);
        etCardDate = findViewById(R.id.etCardDate);
        etCardCVV = findViewById(R.id.etCardCVV);

        btnPay = findViewById(R.id.btnPay);
    }

    private void setupListeners() {
        btnPay.setOnClickListener(v -> validateAndPay());
    }

    private void setupTextWatchers() {
        etCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String number = s.toString();

                if (number.length() >= 4) {
                    String lastFour = number.substring(number.length() - 4);
                    tvCardNumber.setText("**** **** **** " + lastFour);
                } else {
                    tvCardNumber.setText("**** **** **** 4242");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etCardHolder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    tvCardHolder.setText("PABLO HIDALGO");
                } else {
                    tvCardHolder.setText(s.toString().toUpperCase());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        etCardDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    tvCardDate.setText("12/29");
                } else {
                    tvCardDate.setText(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadPayment() {
        ProfileService.getPaymentStatus(
                accessToken,
                userId,
                new ProfileService.PaymentStatusCallback() {
                    @Override
                    public void onSuccess(String estado, String fecha) {
                        if (estado.equals("pagado")) {
                            tvPaymentStatus.setText("Estado: Pagado");
                            tvPaymentStatus.setTextColor(Color.parseColor("#22C55E"));
                            btnPay.setText("Cuota pagada");
                            btnPay.setEnabled(false);
                        } else {
                            tvPaymentStatus.setText("Estado: Pendiente · Vence: " + fecha);
                            tvPaymentStatus.setTextColor(Color.parseColor("#F59E0B"));
                            btnPay.setText("Pagar cuota");
                            btnPay.setEnabled(true);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(
                                PaymentActivity.this,
                                error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void validateAndPay() {
        String cardNumber = etCardNumber.getText().toString().trim();
        String holder = etCardHolder.getText().toString().trim();
        String date = etCardDate.getText().toString().trim();
        String cvv = etCardCVV.getText().toString().trim();

        if (cardNumber.isEmpty() || holder.isEmpty() || date.isEmpty() || cvv.isEmpty()) {
            Toast.makeText(this, "Completa los datos de la tarjeta", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cardNumber.length() < 12) {
            Toast.makeText(this, "Número de tarjeta no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!date.contains("/")) {
            Toast.makeText(this, "Formato de fecha inválido. Usa MM/YY", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cvv.length() < 3) {
            Toast.makeText(this, "CVV no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        btnPay.setEnabled(false);
        btnPay.setText("Procesando pago...");

        ProfileService.payMembership(
                accessToken,
                userId,
                new ProfileService.UpdatePaymentCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(
                                PaymentActivity.this,
                                "Pago realizado correctamente",
                                Toast.LENGTH_SHORT
                        ).show();

                        loadPayment();
                    }

                    @Override
                    public void onError(String error) {
                        btnPay.setEnabled(true);
                        btnPay.setText("Pagar cuota");

                        Toast.makeText(
                                PaymentActivity.this,
                                error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }
}