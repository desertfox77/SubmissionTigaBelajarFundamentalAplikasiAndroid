package com.example.subdua.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.subdua.model.User;

import java.util.ArrayList;

import static com.example.subdua.database.DatabaseContract.DbContractColumn.avatar;
import static com.example.subdua.database.DatabaseContract.DbContractColumn.pk_id;
import static com.example.subdua.database.DatabaseContract.DbContractColumn.nama_table;
import static com.example.subdua.database.DatabaseContract.DbContractColumn.url;
import static com.example.subdua.database.DatabaseContract.DbContractColumn.nama;

public class UserHelper {
    private static final String DATABASE_TABLE = nama_table;
    private static DatabaseHelper databaseHelper;
    private static UserHelper INST;
    private static SQLiteDatabase database;

    public UserHelper(Context c) {
        databaseHelper = new DatabaseHelper(c);
    }

    public static UserHelper getInstance(Context c) {
        if (INST == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INST == null) {
                    INST = new UserHelper(c);
                }
            }
        }
        return INST;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    public Cursor queryAll() {
        return database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                pk_id+ " ASC");
    }

    public Cursor queryById(String id) {
        return database.query(DATABASE_TABLE, null
                , pk_id + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }
    public ArrayList<User> getDataUser(){
        ArrayList<User> userGithubList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE,null,
                null,
                null,
                null,
                null,
                nama + " ASC",
                null);
        cursor.moveToFirst();
        User userGithub;
        if (cursor.getCount() > 0){
            do {
                userGithub = new User();
                userGithub.setId(cursor.getInt(cursor.getColumnIndexOrThrow(pk_id)));
                userGithub.setLogin(cursor.getString(cursor.getColumnIndexOrThrow(nama)));
                userGithub.setAvatarUrl(cursor.getString(cursor.getColumnIndexOrThrow(avatar)));
                userGithub.setHtmlUrl(cursor.getString(cursor.getColumnIndexOrThrow(url)));
                userGithubList.add(userGithub);
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }cursor.close();
        return userGithubList;
    }

    public long userInsert(User userGithub){
        ContentValues contentValues = new ContentValues();
        contentValues.put(pk_id,userGithub.getId());
        contentValues.put(nama,userGithub.getLogin());
        contentValues.put(url,userGithub.getHtmlUrl());
        contentValues.put(avatar,userGithub.getAvatarUrl());

        return database.insert(DATABASE_TABLE,null, contentValues);

    }

    public int userDelete(String id){
        return database.delete(nama_table,pk_id + " = '" + id + "'", null);
    }

    public int DeleteProvider(String id) {
        return database.delete(nama_table, pk_id+ "=?",new String[]{id});
    }
    public int UpdateProvider(String id, ContentValues values) {
        return database.update(nama_table, values, pk_id + " =?", new String[]{id});
    }
    public long InsertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }
}

