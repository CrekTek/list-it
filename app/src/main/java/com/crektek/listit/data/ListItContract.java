package com.crektek.listit.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract class for the ListIt database
 *
 * Created by George on 01/02/2018.
 */
public class ListItContract {

    public static final String AUTHORITY = "com.crektek.listit";

    public static final Uri BASE_CONTENT_URL = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_ITEM = "items";
    public static final String PATH_LIST = "lists";

    // Private constructor to stop instantiation
    private ListItContract() {}

    public static class ItemEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URL.buildUpon()
                .appendPath(PATH_ITEM).build();

        public static final String TABLE_NAME = "item";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_ORDER = "order";
    }

    public static class ListEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URL.buildUpon()
                .appendPath(PATH_LIST).build();

        public static final String TABLE_NAME = "list";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_ORDER = "order";
    }

    public static class ListJoinItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "list_item";
        public static final String COLUMN_NAME_LIST_ID = "list_id";
        public static final String COLUMN_NAME_ITEM_ID = "item_id";
    }
}
