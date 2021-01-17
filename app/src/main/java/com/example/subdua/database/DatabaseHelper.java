package com.example.subdua.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.subdua.database.DatabaseContract.DbContractColumn.nama_table;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String nama_db = "userdbname";
    private static final int version_db = 2;

    private static final String create_table = String.format(
            "CREATE TABLE %s" +
                    "(%s INTEGER PRIMARY KEY," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL)",
            nama_table,
            DatabaseContract.DbContractColumn.pk_id,
            DatabaseContract.DbContractColumn.nama,
            DatabaseContract.DbContractColumn.url,
            DatabaseContract.DbContractColumn.avatar
    );

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + nama_table);
        onCreate(db);
    }

    public DatabaseHelper(Context c){
        super(c,nama_db,null,version_db);
    }
}

