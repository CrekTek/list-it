package com.crektek.listit.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by George on 03/02/2018.
 */

public class ListItContentProvider extends ContentProvider {

    public static final int ITEMS = 100;
    public static final int ITEM_WITH_ID = 101;

    public static final int LISTS = 200;
    public static final int LIST_WITH_ID = 201;
    public static final int ITEMS_IN_LIST = 202;

    private static final UriMatcher uriMatcher = buildUriMatcher();

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
        return false;
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
        return null;
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
