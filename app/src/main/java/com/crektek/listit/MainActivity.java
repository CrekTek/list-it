package com.crektek.listit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.crektek.listit.data.ListEntity;
import com.crektek.listit.data.ListItContract;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        ListItItemTouchHelperCallback.ListItItemTouchHelperListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int LIST_LOADER_ID = 0;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ListAdapter mListAdapter;

    public List<ListEntity> mLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mListAdapter = new ListAdapter(this);
        mRecyclerView.setAdapter(mListAdapter);

        new ItemTouchHelper(new ListItItemTouchHelperCallback(this, mListAdapter))
                .attachToRecyclerView(mRecyclerView);

        getSupportLoaderManager().initLoader(LIST_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(LIST_LOADER_ID, null, this);
    }

    public void onClickLaunchAddListActivity(View view) {
        Intent addListIntent = new Intent(MainActivity.this, AddListActivity.class);
        startActivity(addListIntent);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        int id = (int) viewHolder.itemView.getTag();

        String stringId = Integer.toString(id);
        Uri uri = ListItContract.ListEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        getContentResolver().delete(uri, null, null);

        getSupportLoaderManager().restartLoader(LIST_LOADER_ID, null, MainActivity.this);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mLists, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mLists, i, i - 1);
            }
        }
        mListAdapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new LoadDataTask(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mLists = parseCursorIntoListEntities(data);
        mListAdapter.swapCursor(mLists);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mListAdapter.swapCursor(null);
    }

    /**
     * Method to take the data from our queried cursor and create a List of ListEntities.
     *
     * @param cursor the queried data containing columns from the List table
     * @return a List of ListEntities
     */
    private List<ListEntity> parseCursorIntoListEntities(Cursor cursor) {

        List<ListEntity> lists = new ArrayList<>();

        int idIndex = cursor.getColumnIndex(ListItContract.ListEntry._ID);
        int titleIndex = cursor.getColumnIndex(ListItContract.ListEntry.COLUMN_NAME_TITLE);
        int priorityIndex = cursor.getColumnIndex(ListItContract.ListEntry.COLUMN_NAME_PRIORITY);

        while(cursor.moveToNext()) {
            final int id = cursor.getInt(idIndex);
            final String title = cursor.getString(titleIndex);
            final int priority = cursor.getInt(priorityIndex);

            lists.add(new ListEntity(id, title, priority));
        }

        return lists;
    }

    private static class LoadDataTask extends AsyncTaskLoader<Cursor> {

        private WeakReference<Context> mContext;

        private Cursor mListData = null;

        public LoadDataTask(Context context) {
            super(context);
            mContext = new WeakReference<>(context);
        }

        @Override
        protected void onStartLoading() {
            if (mListData != null) {
                // Delivers any previously loaded data immediately
                deliverResult(mListData);
            } else {
                forceLoad();
            }
        }

        @Override
        public Cursor loadInBackground() {
            try {
                return mContext.get().getContentResolver().query(ListItContract.ListEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        ListItContract.ListEntry.COLUMN_NAME_PRIORITY);

            } catch (Exception e) {
                Log.e(TAG, "Failed to asynchronously load data.");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void deliverResult(Cursor data) {
            mListData = data;
            super.deliverResult(data);
        }
    }
}
