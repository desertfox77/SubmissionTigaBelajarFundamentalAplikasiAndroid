package com.example.subdua.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.example.subdua.database.UserHelper;

import java.util.Objects;

import static com.example.subdua.database.DatabaseContract.SATU;
import static com.example.subdua.database.DatabaseContract.DbContractColumn.uri_database;
import static com.example.subdua.database.DatabaseContract.DbContractColumn.nama_table;

public class MyContentProvider extends ContentProvider {
    private static final int satu = 0;
    private static final int dua = 1;
    UserHelper userHelper;


    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        uriMatcher.addURI(SATU,nama_table,satu);
        uriMatcher.addURI(SATU,nama_table + "/#",dua);
    }
    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        userHelper = UserHelper.getInstance(getContext());
        userHelper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)){
            case satu:
                cursor = userHelper.queryAll();
                break;
            case dua:
                cursor = userHelper.queryById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null){
            cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(),uri);
        }
        return cursor;
        // TODO: Implement this to handle query requests from clients.
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long added;
        Uri contentUri = null;
        switch (uriMatcher.match(uri)){
            case satu:
                added = userHelper.InsertProvider(values);
                if (added > 0) {
                    contentUri = ContentUris.withAppendedId(uri_database, added);
                }
                break;
            default:
                added = 0;
                break;

        }


        if (added > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return contentUri;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int update;
        switch (uriMatcher.match(uri)){
            case dua:
                update = userHelper.UpdateProvider(uri.getLastPathSegment(),values);
                break;
            default:
                update = 0;
                break;
        }


        if (update > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }
        return update;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int delete;
        switch (uriMatcher.match(uri)){
            case dua:
                delete = userHelper.DeleteProvider(uri.getLastPathSegment());
                break;
            default:
                delete = 0;
                break;
        }
        if (delete > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);

        }
        return delete;
    }

}
