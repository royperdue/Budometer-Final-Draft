package com.app.budometer.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.budometer.R;
import com.app.budometer.model.InnerData;
import com.app.budometer.model.InnerItem;
import com.app.budometer.views.listView.inner.BaseInnerAdapter;
import com.app.budometer.databinding.ItemInnerBinding;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;


public class InnerAdapter extends BaseInnerAdapter<InnerItem> {
    private final List<InnerData> mData = new ArrayList<>();

    @Override
    public InnerItem onCreateViewHolder(ViewGroup parent, int viewType) {
        final ItemInnerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        return new InnerItem(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(InnerItem holder, int position) {
        holder.setContent(mData.get(position));
    }

    @Override
    public void onViewRecycled(InnerItem holder) {
        holder.clearContent();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_inner;
    }

    public void addData(@NonNull List<InnerData> innerDataList) {
        final int size = mData.size();
        mData.addAll(innerDataList);
        notifyItemRangeInserted(size, innerDataList.size());
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }
}
