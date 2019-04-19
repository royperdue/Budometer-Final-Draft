package com.app.budometer.views.listView.inner;


import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter class for {@link BaseInnerRecyclerView}.
 * @param <T> inner item class.
 */
public abstract class BaseInnerAdapter<T extends BaseInnerItem> extends RecyclerView.Adapter<T> {}