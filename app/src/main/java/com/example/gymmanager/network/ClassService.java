package com.example.gymmanager.network;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClassService {

    public interface CreateClassCallback {
        void onSuccess();
        void onError(String error);
    }

    public static void createClass(
            String accessToken,
            String nombre,
            String descripcion,
            String horario,
            int aforoMaximo,
            CreateClassCallback callback
    ) {

        JsonObject json = new JsonObject();
        json.addProperty("nombre", nombre);
        json.addProperty("descripcion", descripcion);
        json.addProperty("horario", horario);
        json.addProperty("aforo_maximo", aforoMaximo);
        json.addProperty("activa", true);

        RequestBody body = RequestBody.create(
                json.toString(),
                SupabaseClient.JSON
        );

        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL + "/rest/v1/clases")
                .addHeader("apikey", SupabaseClient.SUPABASE_ANON_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .addHeader("Prefer", "return=minimal")
                .post(body)
                .build();

        SupabaseClient.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnMainThread(() -> callback.onError("Error de conexión"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnMainThread(() -> callback.onError("No se pudo crear la clase"));
                    return;
                }

                runOnMainThread(callback::onSuccess);
            }
        });
    }

    private static void runOnMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}