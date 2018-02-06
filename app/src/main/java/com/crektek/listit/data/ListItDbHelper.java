package com.crektek.listit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.crektek.listit.data.ListItContract.ItemEntry;
import com.crektek.listit.data.ListItContract.ListEntry;

/**
 * Created by George on 01/02/2018.
 */
public class ListItDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "listItDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;

    public ListItDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_ITEM_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +
                ItemEntry._ID + " INTEGER PRIMARY KEY, " +
                ItemEntry.COLUMN_NAME_DESCRIPTION + " TEXT NOT NULL, " +
                ItemEntry.COLUMN_NAME_PRIORITY + " INTEGER NOT NULL, " +
                ItemEntry.COLUMN_NAME_LIST_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + ItemEntry.COLUMN_NAME_LIST_ID + ") REFERENCES " +
                    ListEntry.TABLE_NAME + " (" + ListEntry._ID + "));";

        final String CREATE_LIST_TABLE = "CREATE TABLE " + ListEntry.TABLE_NAME + " (" +
                ListEntry._ID + " INTEGER PRIMARY KEY, " +
                ListEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                ListEntry.COLUMN_NAME_TYPE + " TEXT NOT NULL, " +
                ListEntry.COLUMN_NAME_PRIORITY + " INTEGER NOT NULL);";

        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ListEntry.TABLE_NAME);
        onCreate(db);
    }
}
