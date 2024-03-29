package com.app.budometer.views.listView;


import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * ViewHolder class for {@link TailRecyclerView}.
 * @param <T> inner item ViewGroup subclass.
 */
public abstract class TailItem<T extends ViewGroup> extends RecyclerView.ViewHolder {

    public TailItem(View itemView) {
        super(itemView);
    }

    /**
     * @return  Must return inner item scroll state - is it scrolling now or not.
     */
    abstract public boolean isScrolling();

    /**
     * @return  Must return inner item ViewGroup, that contains inner items.
     */
    abstract public T getViewGroup();
}
