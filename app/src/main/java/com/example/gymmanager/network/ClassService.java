package com.example.gymmanager.network;

import android.os.Handler;
import android.os.Looper;

import com.example.gymmanager.models.GymClass;
import com.example.gymmanager.models.Reservation;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public interface GetClassesCallback {
        void onSuccess(List<GymClass> classes);
        void onError(String error);
    }

    public interface ReserveClassCallback {
        void onSuccess();
        void onError(String error);
    }

    public interface ReservationsCallback {
        void onSuccess(List<Reservation> reservations);
        void onError(String error);
    }

    public interface AttendanceCallback {
        void onSuccess();
        void onError(String error);
    }

    public interface ClassActionCallback {
        void onSuccess();
        void onError(String error);
    }

    public static void createClass(String accessToken, String nombre, String descripcion,
                                   String horario, int aforoMaximo, CreateClassCallback callback) {

        JsonObject json = new JsonObject();
        json.addProperty("nombre", nombre);
        json.addProperty("descripcion", descripcion);
        json.addProperty("horario", horario);
        json.addProperty("aforo_maximo", aforoMaximo);
        json.addProperty("activa", true);

        RequestBody body = RequestBody.create(json.toString(), SupabaseClient.JSON);

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
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    runOnMainThread(() -> callback.onError("No se pudo crear la clase"));
                    return;
                }

                runOnMainThread(callback::onSuccess);
            }
        });
    }

    public static void getActiveClasses(String accessToken, GetClassesCallback callback) {

        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL + "/rest/v1/clases?activa=eq.true&select=*")
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
                    runOnMainThread(() -> callback.onError("No se pudieron cargar las clases"));
                    return;
                }

                try {
                    JsonArray array = JsonParser.parseString(responseBody).getAsJsonArray();
                    List<GymClass> classes = new ArrayList<>();

                    for (int i = 0; i < array.size(); i++) {
                        JsonObject item = array.get(i).getAsJsonObject();

                        GymClass gymClass = new GymClass(
                                item.get("id").getAsString(),
                                item.get("nombre").getAsString(),
                                item.get("descripcion").getAsString(),
                                item.get("horario").getAsString(),
                                item.get("aforo_maximo").getAsInt()
                        );

                        classes.add(gymClass);
                    }

                    runOnMainThread(() -> callback.onSuccess(classes));

                } catch (Exception e) {
                    runOnMainThread(() -> callback.onError("Error procesando clases"));
                }
            }
        });
    }

    public static void reserveClass(String accessToken, String classId, String userId,
                                    ReserveClassCallback callback) {

        JsonObject json = new JsonObject();
        json.addProperty("clase_id", classId);
        json.addProperty("socio_id", userId);
        json.addProperty("asistio", false);

        RequestBody body = RequestBody.create(json.toString(), SupabaseClient.JSON);

        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL + "/rest/v1/reservas")
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
                    runOnMainThread(() -> callback.onError("No se pudo reservar"));
                    return;
                }

                runOnMainThread(callback::onSuccess);
            }
        });
    }

    public static void getUserReservations(String accessToken, String userId,
                                           GetClassesCallback callback) {

        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL +
                        "/rest/v1/reservas?socio_id=eq." + userId + "&select=clases(*)")
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
                    runOnMainThread(() -> callback.onError("No se pudieron cargar reservas"));
                    return;
                }

                try {
                    JsonArray array = JsonParser.parseString(responseBody).getAsJsonArray();
                    List<GymClass> classes = new ArrayList<>();

                    for (int i = 0; i < array.size(); i++) {
                        JsonObject reserva = array.get(i).getAsJsonObject();
                        JsonObject clase = reserva.getAsJsonObject("clases");

                        GymClass gymClass = new GymClass(
                                clase.get("id").getAsString(),
                                clase.get("nombre").getAsString(),
                                clase.get("descripcion").getAsString(),
                                clase.get("horario").getAsString(),
                                clase.get("aforo_maximo").getAsInt()
                        );

                        classes.add(gymClass);
                    }

                    runOnMainThread(() -> callback.onSuccess(classes));

                } catch (Exception e) {
                    runOnMainThread(() -> callback.onError("Error procesando reservas"));
                }
            }
        });
    }

    public static void getReservationsByClass(String accessToken, String classId,
                                              ReservationsCallback callback) {

        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL + "/rest/v1/reservas?clase_id=eq." + classId)
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
                    runOnMainThread(() -> callback.onError("No se pudieron cargar reservas"));
                    return;
                }

                try {
                    JsonArray array = JsonParser.parseString(responseBody).getAsJsonArray();
                    List<Reservation> reservations = new ArrayList<>();

                    for (int i = 0; i < array.size(); i++) {
                        JsonObject item = array.get(i).getAsJsonObject();

                        Reservation reservation = new Reservation(
                                item.get("id").getAsString(),
                                "Cliente reservado",
                                item.get("asistio").getAsBoolean()
                        );

                        reservations.add(reservation);
                    }

                    runOnMainThread(() -> callback.onSuccess(reservations));

                } catch (Exception e) {
                    runOnMainThread(() -> callback.onError("Error procesando reservas"));
                }
            }
        });
    }

    public static void markAttendance(String accessToken, String reservationId,
                                      AttendanceCallback callback) {

        JsonObject json = new JsonObject();
        json.addProperty("asistio", true);

        RequestBody body = RequestBody.create(json.toString(), SupabaseClient.JSON);

        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL + "/rest/v1/reservas?id=eq." + reservationId)
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
                    runOnMainThread(() -> callback.onError("No se pudo marcar asistencia"));
                    return;
                }

                runOnMainThread(callback::onSuccess);
            }
        });
    }

    public static void updateClass(String accessToken, String classId, String nombre,
                                   String descripcion, String horario, int aforoMaximo,
                                   ClassActionCallback callback) {

        JsonObject json = new JsonObject();
        json.addProperty("nombre", nombre);
        json.addProperty("descripcion", descripcion);
        json.addProperty("horario", horario);
        json.addProperty("aforo_maximo", aforoMaximo);

        RequestBody body = RequestBody.create(json.toString(), SupabaseClient.JSON);

        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL + "/rest/v1/clases?id=eq." + classId)
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
                    runOnMainThread(() -> callback.onError("No se pudo actualizar la clase"));
                    return;
                }

                runOnMainThread(callback::onSuccess);
            }
        });
    }

    public static void deleteClass(String accessToken, String classId,
                                   ClassActionCallback callback) {

        Request request = new Request.Builder()
                .url(SupabaseClient.SUPABASE_URL + "/rest/v1/clases?id=eq." + classId)
                .addHeader("apikey", SupabaseClient.SUPABASE_ANON_KEY)
                .addHeader("Authorization", "Bearer " + accessToken)
                .delete()
                .build();

        SupabaseClient.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnMainThread(() -> callback.onError("Error de conexión"));
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()) {
                    runOnMainThread(() -> callback.onError("No se pudo eliminar la clase"));
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