package com.app.budometer.views.listView.header;

import android.view.View;

import com.app.budometer.views.listView.TailItem;
import com.app.budometer.views.listView.inner.BaseInnerRecyclerView;


public abstract class HeaderItem extends TailItem<BaseInnerRecyclerView> {
    public HeaderItem(View itemView) {
        super(itemView);
    }

    /**
     * @return  Must return header main layout
     */
    abstract public View getHeader();

    /**
     * @return  Must return header alpha-layout.
     * Alpha-layout is the layout which will hide header's views,
     * when item will be scrolled to left or right.
     */
    abstract public View getHeaderAlphaView();
}
