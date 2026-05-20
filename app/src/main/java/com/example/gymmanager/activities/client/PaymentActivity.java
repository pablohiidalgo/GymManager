package com.example.gymmanager.activities.client;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymmanager.R;
import com.example.gymmanager.network.ProfileService;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvPaymentStatus, tvPaymentDate;
    private Button btnPay;

    private String accessToken;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        accessToken = getIntent().getStringExtra("accessToken");
        userId = getIntent().getStringExtra("userId");

        tvPaymentStatus = findViewById(R.id.tvPaymentStatus);
        tvPaymentDate = findViewById(R.id.tvPaymentDate);
        btnPay = findViewById(R.id.btnPay);

        loadPayment();

        btnPay.setOnClickListener(v -> payMembership());
    }

    private void loadPayment() {
        ProfileService.getPaymentStatus(accessToken, userId, new ProfileService.PaymentStatusCallback() {
            @Override
            public void onSuccess(String estado, String fecha) {
                tvPaymentStatus.setText(estado.toUpperCase());

                if (estado.equals("pagado")) {
                    tvPaymentStatus.setTextColor(Color.GREEN);
                } else {
                    tvPaymentStatus.setTextColor(Color.RED);
                }

                tvPaymentDate.setText("Vencimiento: " + fecha);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(PaymentActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void payMembership() {
        ProfileService.payMembership(accessToken, userId, new ProfileService.UpdatePaymentCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(PaymentActivity.this, "Pago realizado correctamente", Toast.LENGTH_SHORT).show();
                loadPayment();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(PaymentActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}