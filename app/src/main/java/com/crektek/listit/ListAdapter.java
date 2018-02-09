package com.crektek.listit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crektek.listit.data.ListEntity;

import java.util.List;

/**
 * Created on 23/01/2018.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListItemViewHolder> {

    private static final String TAG = ListAdapter.class.getSimpleName();

    private Context mContext;
    private List<ListEntity> mLists;

    public ListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {

        ListEntity listEntity = mLists.get(position);

        holder.itemView.setTag(listEntity.getId());
        holder.mItemText.setText(listEntity.getTitle());
    }

    @Override
    public int getItemCount() {
        if (mLists == null || mLists.isEmpty()) {
            return 0;
        }
        return mLists.size();
    }

    /**
     * When the data changes a re-query occurs. This will swap the old data
     * for new data.
     *
     * @param lists the new data passed in
     * @return the old data
     */
    public List<ListEntity> swapCursor(List<ListEntity> lists) {

        if(mLists == lists) {
            return null;
        }

        List<ListEntity> oldLists = mLists;
        mLists = lists;

        if(mLists != null) {
            notifyDataSetChanged();
        }

        return oldLists;
    }

    public static class ListItemViewHolder extends RecyclerView.ViewHolder {

        public TextView mItemText;

        public ListItemViewHolder(View listItemView) {
            super(listItemView);
            mItemText = (TextView) listItemView.findViewById(R.id.tv_item_text);
        }
    }
}
