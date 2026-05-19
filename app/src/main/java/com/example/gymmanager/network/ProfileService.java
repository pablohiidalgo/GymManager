package com.example.gymmanager.network;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileService {

    public interface RoleCallback {
        void onSuccess(String role);
        void onError(String error);
    }

    public static void getUserRole(
            String userId,
            String accessToken,
            RoleCallback callback
    ) {

        Request request = new Request.Builder()
                .url(
                        SupabaseClient.SUPABASE_URL +
                                "/rest/v1/perfiles?id=eq." +
                                userId +
                                "&select=rol"
                )
                .addHeader("apikey", SupabaseClient.SUPABASE_ANON_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .get()
                .build();

        SupabaseClient.getClient().newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnMainThread(() ->
                        callback.onError("Error de conexión")
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseBody =
                        response.body() != null
                                ? response.body().string()
                                : "";

                if (!response.isSuccessful()) {

                    runOnMainThread(() ->
                            callback.onError("Error al obtener rol")
                    );

                    return;
                }

                try {

                    JsonArray array =
                            JsonParser.parseString(responseBody)
                                    .getAsJsonArray();

                    if (array.size() == 0) {

                        runOnMainThread(() ->
                                callback.onError("Perfil no encontrado")
                        );

                        return;
                    }

                    JsonObject profile =
                            array.get(0).getAsJsonObject();

                    String role =
                            profile.get("rol").getAsString();

                    runOnMainThread(() ->
                            callback.onSuccess(role)
                    );

                } catch (Exception e) {

                    runOnMainThread(() ->
                            callback.onError("Error procesando rol")
                    );
                }
            }
        });
    }

    private static void runOnMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}