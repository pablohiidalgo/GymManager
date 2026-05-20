package com.example.gymmanager.network;

import android.os.Handler;
import android.os.Looper;

import com.example.gymmanager.models.AdminStats;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileService {

    public interface RoleCallback {
        void onSuccess(String role);
        void onError(String error);
    }

    public interface CreateProfileCallback {
        void onSuccess();
        void onError(String error);
    }

    public interface CreateMemberCallback {
        void onSuccess();
        void onError(String error);
    }

    public interface PaymentStatusCallback {
        void onSuccess(String estado, String fecha);
        void onError(String error);
    }

    public interface UpdatePaymentCallback {
        void onSuccess();
        void onError(String error);
    }

    public static void getUserRole(String userId, String accessToken, RoleCallback callback) {
        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL + "/rest/v1/perfiles?id=eq." + userId + "&select=rol")
                .addHeader("apikey", SupabaseClient.SUPABASE_ANON_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .get()
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
                    runOnMainThread(() -> callback.onError("Error al obtener rol"));
                    return;
                }

                try {
                    JsonArray array = JsonParser.parseString(responseBody).getAsJsonArray();

                    if (array.size() == 0) {
                        runOnMainThread(() -> callback.onError("Perfil no encontrado"));
                        return;
                    }

                    String role = array.get(0).getAsJsonObject().get("rol").getAsString();
                    runOnMainThread(() -> callback.onSuccess(role));

                } catch (Exception e) {
                    runOnMainThread(() -> callback.onError("Error procesando rol"));
                }
            }
        });
    }

    public static void createClientProfile(
            String userId,
            String accessToken,
            String nombre,
            String apellidos,
            String telefono,
            CreateProfileCallback callback
    ) {
        JsonObject json = new JsonObject();
        json.addProperty("id", userId);
        json.addProperty("nombre", nombre);
        json.addProperty("apellidos", apellidos);
        json.addProperty("telefono", telefono);
        json.addProperty("rol", "cliente");

        RequestBody body = RequestBody.create(json.toString(), SupabaseClient.JSON);

        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL + "/rest/v1/perfiles")
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
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    runOnMainThread(() -> callback.onError("No se pudo crear el perfil"));
                    return;
                }

                runOnMainThread(callback::onSuccess);
            }
        });
    }

    public static void createClientMember(
            String userId,
            String accessToken,
            CreateMemberCallback callback
    ) {
        JsonObject json = new JsonObject();
        json.addProperty("id", userId);
        json.addProperty("estado_pago", "pendiente");
        json.addProperty("fecha_vencimiento", "2026-06-30");

        RequestBody body = RequestBody.create(json.toString(), SupabaseClient.JSON);

        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL + "/rest/v1/socios")
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
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    runOnMainThread(() -> callback.onError("No se pudo crear el socio"));
                    return;
                }

                runOnMainThread(callback::onSuccess);
            }
        });
    }

    public static void getPaymentStatus(
            String accessToken,
            String userId,
            PaymentStatusCallback callback
    ) {
        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL + "/rest/v1/socios?id=eq." + userId + "&select=estado_pago,fecha_vencimiento")
                .addHeader("apikey", SupabaseClient.SUPABASE_ANON_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .get()
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
                    runOnMainThread(() -> callback.onError("No se pudo cargar cuota"));
                    return;
                }

                try {
                    JsonArray array = JsonParser.parseString(responseBody).getAsJsonArray();

                    if (array.size() == 0) {
                        runOnMainThread(() -> callback.onError("Socio no encontrado"));
                        return;
                    }

                    JsonObject socio = array.get(0).getAsJsonObject();

                    String estado = socio.get("estado_pago").getAsString();
                    String fecha = socio.get("fecha_vencimiento").getAsString();

                    runOnMainThread(() -> callback.onSuccess(estado, fecha));

                } catch (Exception e) {
                    runOnMainThread(() -> callback.onError("Error procesando cuota"));
                }
            }
        });
    }

    public static void payMembership(
            String accessToken,
            String userId,
            UpdatePaymentCallback callback
    ) {
        JsonObject json = new JsonObject();
        json.addProperty("estado_pago", "pagado");

        RequestBody body = RequestBody.create(json.toString(), SupabaseClient.JSON);

        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL + "/rest/v1/socios?id=eq." + userId)
                .addHeader("apikey", SupabaseClient.SUPABASE_ANON_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json")
                .patch(body)
                .build();

        SupabaseClient.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnMainThread(() -> callback.onError("Error de conexión"));
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    runOnMainThread(() -> callback.onError("No se pudo actualizar cuota"));
                    return;
                }

                runOnMainThread(callback::onSuccess);
            }
        });
    }
    public interface AdminStatsCallback {
        void onSuccess(AdminStats stats);
        void onError(String error);
    }
    public static void getAdminStats(
            String accessToken,
            AdminStatsCallback callback
    ) {

        Request usersRequest = new Request.Builder()
                .url(
                        SupabaseClient.SUPABASE_URL +
                                "/rest/v1/perfiles?select=id"
                )
                .addHeader("apikey", SupabaseClient.SUPABASE_ANON_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .get()
                .build();

        SupabaseClient.getClient().newCall(usersRequest).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

                runOnMainThread(() ->
                        callback.onError("Error conexión")
                );
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {

                String usersBody =
                        response.body() != null
                                ? response.body().string()
                                : "";

                try {

                    int totalUsers =
                            JsonParser.parseString(usersBody)
                                    .getAsJsonArray()
                                    .size();

                    getClassesCount(
                            accessToken,
                            totalUsers,
                            callback
                    );

                } catch (Exception e) {

                    runOnMainThread(() ->
                            callback.onError("Error estadísticas")
                    );
                }
            }
        });
    }

    private static void getClassesCount(
            String accessToken,
            int totalUsers,
            AdminStatsCallback callback
    ) {

        Request request = new Request.Builder()
                .url(
                        SupabaseClient.SUPABASE_URL +
                                "/rest/v1/clases?select=id"
                )
                .addHeader("apikey", SupabaseClient.SUPABASE_ANON_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .get()
                .build();

        SupabaseClient.getClient().newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

                runOnMainThread(() ->
                        callback.onError("Error conexión")
                );
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {

                String body =
                        response.body() != null
                                ? response.body().string()
                                : "";

                try {

                    int totalClasses =
                            JsonParser.parseString(body)
                                    .getAsJsonArray()
                                    .size();

                    getReservationsCount(
                            accessToken,
                            totalUsers,
                            totalClasses,
                            callback
                    );

                } catch (Exception e) {

                    runOnMainThread(() ->
                            callback.onError("Error estadísticas")
                    );
                }
            }
        });
    }

    private static void getReservationsCount(
            String accessToken,
            int totalUsers,
            int totalClasses,
            AdminStatsCallback callback
    ) {

        Request request = new Request.Builder()
                .url(
                        SupabaseClient.SUPABASE_URL +
                                "/rest/v1/reservas?select=id"
                )
                .addHeader("apikey", SupabaseClient.SUPABASE_ANON_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .get()
                .build();

        SupabaseClient.getClient().newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

                runOnMainThread(() ->
                        callback.onError("Error conexión")
                );
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {

                String body =
                        response.body() != null
                                ? response.body().string()
                                : "";

                try {

                    int totalReservations =
                            JsonParser.parseString(body)
                                    .getAsJsonArray()
                                    .size();

                    AdminStats stats =
                            new AdminStats(
                                    totalUsers,
                                    totalClasses,
                                    totalReservations
                            );

                    runOnMainThread(() ->
                            callback.onSuccess(stats)
                    );

                } catch (Exception e) {

                    runOnMainThread(() ->
                            callback.onError("Error estadísticas")
                    );
                }
            }
        });
    }
    private static void runOnMainThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}