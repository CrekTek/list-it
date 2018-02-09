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

import com.crektek.listit.data.ListItContract.ItemEntry;
import com.crektek.listit.data.ListItContract.ListEntry;

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

    private static final String LIST_TABLE_NAME = ListEntry.TABLE_NAME;
    private static final String ITEM_TABLE_NAME = ItemEntry.TABLE_NAME;

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
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Log.d(TAG, "Dealing with query uri: " + uri);

        final SQLiteDatabase db = mListItDbHelper.getReadableDatabase();

        int match = mUriMatcher.match(uri);
        Cursor returnCursor;

        switch (match) {
            case LISTS:
                returnCursor = db.query(LIST_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case ITEMS_IN_LIST:
                String listId = uri.getPathSegments().get(1);
                returnCursor = db.rawQuery("SELECT " + ITEM_TABLE_NAME + ".* FROM " +
                                ITEM_TABLE_NAME + " item " + "INNER JOIN " +
                                LIST_TABLE_NAME + " list " + "ON item." + ItemEntry.COLUMN_NAME_LIST_ID + " = list._id " +
                                "WHERE item." + ItemEntry.COLUMN_NAME_LIST_ID + " = ?",
                        new String[]{listId});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return returnCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        Log.d(TAG, "Dealing with insert uri: " + uri);

        final SQLiteDatabase db = mListItDbHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case ITEMS:
                long itemId = db.insert(ITEM_TABLE_NAME, null, contentValues);
                if ( itemId > 0 ) {
                    returnUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI, itemId);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case LISTS:
                long listId = db.insert(LIST_TABLE_NAME, null, contentValues);
                if ( listId > 0 ) {
                    returnUri = ContentUris.withAppendedId(ListEntry.CONTENT_URI, listId);
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

        Log.d(TAG, "Dealing with delete uri: " + uri);

        final SQLiteDatabase db = mListItDbHelper.getWritableDatabase();

        int match = mUriMatcher.match(uri);

        int tasksDeleted;

        switch (match) {
            case LIST_WITH_ID:
                // TODO This will need to change in order to account for deletion of list items under the list.
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                tasksDeleted = db.delete(LIST_TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change
        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
