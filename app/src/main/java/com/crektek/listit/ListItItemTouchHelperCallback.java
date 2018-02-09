package com.crektek.listit;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created on 08/02/2018.
 */
public class ListItItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ListItItemTouchHelperListener mListener;
    private ListAdapter mAdapter;

    public ListItItemTouchHelperCallback(ListItItemTouchHelperListener listener, ListAdapter adapter) {
        mListener = listener;
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mListener.onItemMove(viewHolder.getAdapterPosition(),
                target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mListener.onSwiped(viewHolder);
    }

    public interface ListItItemTouchHelperListener {

        void onSwiped(RecyclerView.ViewHolder viewHolder);

        void onItemMove(int fromPosition, int toPosition);
    }
}
