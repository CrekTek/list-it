package com.crektek.listit.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by George on 03/02/2018.
 */

public class ListItContentProvider extends ContentProvider {

    private static final String TAG = ListItContentProvider.class.getSimpleName();

    public static final int ITEMS = 100;
    public static final int ITEM_WITH_ID = 101;

    public static final int LISTS = 200;
    public static final int LIST_WITH_ID = 201;
    public static final int ITEMS_IN_LIST = 202;

    private static final UriMatcher mUriMatcher = buildUriMatcher();

    private ListItDbHelper mListItDbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(ListItContract.AUTHORITY, ListItContract.PATH_ITEM, ITEMS);
        uriMatcher.addURI(ListItContract.AUTHORITY, ListItContract.PATH_ITEM + "/#", ITEM_WITH_ID);

        uriMatcher.addURI(ListItContract.AUTHORITY, ListItContract.PATH_LIST, LISTS);
        uriMatcher.addURI(ListItContract.AUTHORITY, ListItContract.PATH_LIST + "/#", LIST_WITH_ID);
        uriMatcher.addURI(ListItContract.AUTHORITY, ListItContract.PATH_LIST + "/#/" +
            ListItContract.PATH_ITEM, ITEMS_IN_LIST);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mListItDbHelper = new ListItDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        Log.d(TAG, "Dealing with insert uri: " + uri);

        SQLiteDatabase db = mListItDbHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case ITEMS:
                long itemId = db.insert(ListItContract.ItemEntry.TABLE_NAME, null, contentValues);
                if ( itemId > 0 ) {
                    returnUri = ContentUris.withAppendedId(ListItContract.ItemEntry.CONTENT_URI, itemId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case LISTS:
                long listId = db.insert(ListItContract.ListEntry.TABLE_NAME, null, contentValues);
                if ( listId > 0 ) {
                    returnUri = ContentUris.withAppendedId(ListItContract.ListEntry.CONTENT_URI, listId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
