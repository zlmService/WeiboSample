package com.demo.zlm.weibosample.SQLite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by malinkang on 2016/3/31.
 */
public class DataContentProvider extends ContentProvider {
    private DataHelper helper;
    public static final String AUTHROITY = "com.zlm.weibo.ContentProvider";
    private static final int WEIBO_SINGLE_CODE = 2;
    private static final int WEIBO_MUTIPLE_CODE = 1;
    private static final int TOKEN_MUTIPLE_CODE = 3;
    private static final int TOKEN_SINGLE_CODE = 4;
    private static final int USER_SINGLE_CODE = 5;
    private static final int USER_MULTIPLE_CODE = 6;
    SQLiteDatabase dbWrite;
    SQLiteDatabase dbRead;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHROITY, "token", TOKEN_MUTIPLE_CODE);
        uriMatcher.addURI(AUTHROITY, "token/#", TOKEN_SINGLE_CODE);
        uriMatcher.addURI(AUTHROITY, "weibo", WEIBO_MUTIPLE_CODE);
        uriMatcher.addURI(AUTHROITY, "weibo/#", WEIBO_SINGLE_CODE);
        uriMatcher.addURI(AUTHROITY, "user", USER_MULTIPLE_CODE);
        uriMatcher.addURI(AUTHROITY, "user/#", USER_SINGLE_CODE);
    }

    @Override
    public boolean onCreate() {
        helper = new DataHelper(getContext());
        dbWrite = helper.getWritableDatabase();
        dbRead = helper.getReadableDatabase();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor query;
        switch (uriMatcher.match(uri)) {
            case TOKEN_SINGLE_CODE:
                long id = ContentUris.parseId(uri);
                selection = DataMata.TokenTable._ID + "=?";
                selectionArgs = new String[]{String.valueOf(id)};
                query = dbRead.query(DataMata.TokenTable.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                if (query != null) {
                    query.setNotificationUri(getContext().getContentResolver(), uri);
                }
                return query;
            case TOKEN_MUTIPLE_CODE:
                query = dbRead.query(DataMata.TokenTable.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                if (query != null) {
                    query.setNotificationUri(getContext().getContentResolver(), uri);
                }
                return query;
            case WEIBO_MUTIPLE_CODE:
                query = dbRead.query(DataMata.WeiBoTable.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                if (query != null) {
                    query.setNotificationUri(getContext().getContentResolver(), uri);
                }
                return query;
            case WEIBO_SINGLE_CODE:
                long iD = ContentUris.parseId(uri);
                selection = DataMata.TokenTable._ID + "=?";
                selectionArgs = new String[]{String.valueOf(iD)};
                query = dbRead.query(DataMata.WeiBoTable.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                if (query != null) {
                    query.setNotificationUri(getContext().getContentResolver(), uri);
                }
                return query;
            case USER_MULTIPLE_CODE:
                query = dbRead.query(DataMata.UserTable.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                if (query != null) {
                    query.setNotificationUri(getContext().getContentResolver(), uri);
                }
                return query;
            case USER_SINGLE_CODE:
                long uid = ContentUris.parseId(uri);
                selection = DataMata.TokenTable._ID + "=?";
                selectionArgs = new String[]{String.valueOf(uid)};
                query = dbRead.query(DataMata.UserTable.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                if (query != null) {
                    query.setNotificationUri(getContext().getContentResolver(), uri);
                }
                return query;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long insert;
        switch (uriMatcher.match(uri)) {
            case TOKEN_MUTIPLE_CODE:
                insert = dbWrite.insert(DataMata.TokenTable.TABLE_NAME, null, values);
                if (insert != -1) {
                    uri = ContentUris.withAppendedId(uri, insert);
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return uri;
            case WEIBO_MUTIPLE_CODE:
                insert = dbWrite.insert(DataMata.WeiBoTable.TABLE_NAME, null, values);
                if (insert != -1) {
                    uri = ContentUris.withAppendedId(uri, insert);
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return uri;
            case USER_MULTIPLE_CODE:
                insert = dbWrite.insert(DataMata.UserTable.TABLE_NAME, null, values);
                if (insert != -1) {
                    uri = ContentUris.withAppendedId(uri, insert);
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return uri;

        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case TOKEN_MUTIPLE_CODE:
                int delete = dbWrite.delete(DataMata.TokenTable.TABLE_NAME, selection, selectionArgs);
                if (delete > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return delete;
            case WEIBO_MUTIPLE_CODE:
                delete = dbWrite.delete(DataMata.WeiBoTable.TABLE_NAME, selection, selectionArgs);
                if (delete > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return delete;
            case USER_MULTIPLE_CODE:
                delete = dbWrite.delete(DataMata.UserTable.TABLE_NAME, selection, selectionArgs);
                if (delete > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return delete;
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
