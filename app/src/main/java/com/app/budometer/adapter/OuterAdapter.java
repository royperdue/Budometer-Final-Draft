package com.app.budometer.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.budometer.R;
import com.app.budometer.model.InnerData;
import com.app.budometer.model.OuterItem;
import com.app.budometer.views.listView.TailAdapter;


import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class OuterAdapter extends TailAdapter<OuterItem> {
    private final List<List<InnerData>> mData;
    private final RecyclerView.RecycledViewPool mPool;

    public OuterAdapter(List<List<InnerData>> data) {
        this.mData = data;

        mPool = new RecyclerView.RecycledViewPool();
        int POOL_SIZE = 16;
        mPool.setMaxRecycledViews(0, POOL_SIZE);
    }

    @Override
    public OuterItem onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new OuterItem(view, mPool);
    }

    @Override
    public void onBindViewHolder(OuterItem holder, int position) {
        holder.setContent(mData.get(position));
    }

    @Override
    public void onViewRecycled(OuterItem holder) {
        holder.clearContent();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_outer;
    }
}
