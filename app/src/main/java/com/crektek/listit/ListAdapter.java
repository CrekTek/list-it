package com.crektek.listit;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crektek.listit.data.ListItContract;

/**
 * Created on 23/01/2018.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListItemViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public ListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ListItemViewHolder viewHolder = new ListItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {

        int idIndex = mCursor.getColumnIndex(ListItContract.ListEntry._ID);
        int titleIndex = mCursor.getColumnIndex(ListItContract.ListEntry.COLUMN_NAME_TITLE);
        int typeIndex = mCursor.getColumnIndex(ListItContract.ListEntry.COLUMN_NAME_TYPE);

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idIndex);
        final String title = mCursor.getString(titleIndex);

        holder.itemView.setTag(id);
        holder.mItemText.setText(title);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * When the data changes a re-query occurs. This will swap the old data
     * for new data.
     *
     * @param cursor the new data passed in
     * @return the old data
     */
    public Cursor swapCursor(Cursor cursor) {

        if(mCursor == cursor) {
            return null;
        }

        Cursor oldCursor = mCursor;
        mCursor = cursor;

        if(mCursor != null) {
            notifyDataSetChanged();
        }

        return oldCursor;
    }

    public static class ListItemViewHolder extends RecyclerView.ViewHolder {

        public TextView mItemText;

        public ListItemViewHolder(View listItemView) {
            super(listItemView);
            mItemText = (TextView) listItemView.findViewById(R.id.tv_item_text);
        }
    }
}
