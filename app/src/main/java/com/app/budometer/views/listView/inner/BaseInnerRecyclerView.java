package com.app.budometer.views.listView.inner;


import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Extended {@link androidx.recyclerview.widget.RecyclerView}
 * that works <b>only</b> with {@link BaseInnerLayoutManager} and {@link BaseInnerAdapter}.
 */
public class BaseInnerRecyclerView extends RecyclerView {
    private String TAG = "BaseInnerRecyclerView";
    public BaseInnerRecyclerView(Context context) {
        super(context);
    }

    public BaseInnerRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseInnerRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param adapter must be instance of TailAdapter class
     */
    @Override
    public void setAdapter(Adapter adapter) {
        /*if (!(adapter instanceof BaseInnerAdapter)) {
            Log.i(TAG, "Adapter must be instance of BaseInnerAdapter class");
        }*/
        super.setAdapter(adapter);
    }

    /**
     *
     * @param lm must be instance of BaseInnerLayoutManager class
     */
    @Override
    public void setLayoutManager(LayoutManager lm) {
        if (!(lm instanceof BaseInnerLayoutManager)) {
            throw new IllegalArgumentException("LayoutManager must be instance of BaseInnerLayoutManager class");
        }
        super.setLayoutManager(lm);
    }
}
