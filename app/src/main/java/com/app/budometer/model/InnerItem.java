package com.app.budometer.model;


import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.budometer.R;
import com.app.budometer.views.listView.inner.BaseInnerItem;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;


public class InnerItem  extends BaseInnerItem {
    private final View mInnerLayout;
    public final TextView mHeader;
    public final TextView mStrain;
    public final TextView mDate;
    public final View mLine;
    public long analysisId;

    private InnerData mInnerData;

    public InnerItem(View itemView) {
        super(itemView);
        mInnerLayout = ((ViewGroup)itemView).getChildAt(0);

        mHeader = itemView.findViewById(R.id.tv_header);
        mStrain = itemView.findViewById(R.id.tv_strain);
        mDate = itemView.findViewById(R.id.tv_date);
        mLine = itemView.findViewById(R.id.line);

        mInnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(InnerItem.this);
            }
        });
    }

    @Override
    protected View getInnerLayout() {
        return mInnerLayout;
    }

    public InnerData getItemData() {
        return mInnerData;
    }

    public void clearContent() {
        mInnerData = null;
    }

    public void setContent(InnerData data) {
        mInnerData = data;
        analysisId = data.analysisId;

        mStrain.setText(String.format("%s", data.strain));
        mDate.setText( new SimpleDateFormat("EEEE, MMMM d, yyyy").format(new Date(Long.parseLong(data.date.toString()))));
    }
}
