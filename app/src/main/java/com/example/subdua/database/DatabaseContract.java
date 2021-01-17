package com.example.subdua.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String SATU = "com.example.subdua";
    public static final String DUA = "content";

    public static final class DbContractColumn implements BaseColumns {
        public static final String nama_table = "user";
        public static final String pk_id = "id";
        public static final String nama = "username";
        public static final String url = "url";
        public static final String avatar = "avatar";
        public static final Uri uri_database = new Uri.Builder().scheme(DUA)
                .authority(SATU)
                .appendPath(nama_table)
                .build();
    }
}

