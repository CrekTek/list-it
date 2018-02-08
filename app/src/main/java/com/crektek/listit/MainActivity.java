package com.crektek.listit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.crektek.listit.data.ListItContract;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int LIST_LOADER_ID = 0;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ListAdapter mListAdapter;

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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new LoadDataTask(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mListAdapter.swapCursor(null);
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
