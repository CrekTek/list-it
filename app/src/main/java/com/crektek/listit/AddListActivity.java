package com.crektek.listit;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.crektek.listit.data.ListItContract;

public class AddListActivity extends AppCompatActivity {

    private static final String TAG = AddListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
    }

    public void onClickCreateList(View view) {

        String input = ((EditText) findViewById(R.id.et_list_description)).getText().toString();
        if (input.length() == 0) {
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(ListItContract.ListEntry.COLUMN_NAME_TITLE, input);
        contentValues.put(ListItContract.ListEntry.COLUMN_NAME_TYPE, "TODO");
        contentValues.put(ListItContract.ListEntry.COLUMN_NAME_PRIORITY, 1);

        Uri uri = getContentResolver().insert(ListItContract.ListEntry.CONTENT_URI, contentValues);

        Log.i(TAG, "New list created. Returned uri from insert: " + uri);

        finish();
    }
}
