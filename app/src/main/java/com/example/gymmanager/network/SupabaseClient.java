package com.example.gymmanager.network;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class SupabaseClient {

    public static final String SUPABASE_URL =
            "https://pjrqhwkhanldamykhuzm.supabase.co";

    public static final String SUPABASE_ANON_KEY =
            "sb_publishable_lLK1emF1B6joqkw_4HUpqg_S1VvrIX2";

    public static final MediaType JSON =
            MediaType.get("application/json; charset=utf-8");

    private static final OkHttpClient client =
            new OkHttpClient();

    public static OkHttpClient getClient() {
        return client;
    }
}