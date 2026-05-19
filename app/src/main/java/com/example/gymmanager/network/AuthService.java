package com.example.gymmanager.network;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthService {

    public interface LoginCallback {
        void onSuccess(String accessToken, String userId);
        void onError(String errorMessage);
    }

    public static void login(String email, String password, LoginCallback callback) {

        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("password", password);

        RequestBody body = RequestBody.create(
                json.toString(),
                SupabaseClient.JSON
        );

        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL + "/auth/v1/token?grant_type=password")
                .addHeader("apikey", SupabaseClient.SUPABASE_ANON_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        SupabaseClient.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnMainThread(() -> callback.onError("Error de conexión"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseBody = response.body() != null ? response.body().string() : "";

                if (!response.isSuccessful()) {
                    runOnMainThread(() -> callback.onError("Correo o contraseña incorrectos"));
                    return;
                }

                try {
                    JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

                    String accessToken = jsonResponse.get("access_token").getAsString();
                    String userId = jsonResponse
                            .getAsJsonObject("user")
                            .get("id")
                            .getAsString();

                    runOnMainThread(() -> callback.onSuccess(accessToken, userId));

                } catch (Exception e) {
                    runOnMainThread(() -> callback.onError("Error al procesar la respuesta"));
                }
            }
        });
    }

    private static void runOnMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}