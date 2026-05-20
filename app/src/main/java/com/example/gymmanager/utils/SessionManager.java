package com.example.gymmanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "gym_session";

    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ROLE = "role";

    public static void saveSession(
            Context context,
            String token,
            String userId,
            String role
    ) {

        SharedPreferences prefs =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                );

        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_USER_ID, userId)
                .putString(KEY_ROLE, role)
                .apply();
    }

    public static String getToken(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                );

        return prefs.getString(KEY_TOKEN, null);
    }

    public static String getUserId(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                );

        return prefs.getString(KEY_USER_ID, null);
    }

    public static String getRole(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                );

        return prefs.getString(KEY_ROLE, null);
    }

    public static void clearSession(Context context) {

        SharedPreferences prefs =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                );

        prefs.edit().clear().apply();
    }
}