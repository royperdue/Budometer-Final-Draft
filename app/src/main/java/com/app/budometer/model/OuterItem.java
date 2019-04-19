package com.app.budometer.model;


import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.budometer.R;
import com.app.budometer.adapter.InnerAdapter;
import com.app.budometer.views.listView.header.HeaderDecorator;
import com.app.budometer.views.listView.header.HeaderItem;
import com.app.budometer.views.listView.inner.BaseInnerLayoutManager;
import com.app.budometer.views.listView.inner.BaseInnerRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

public class OuterItem extends HeaderItem {
    private final static float AVATAR_RATIO_START = 1f;
    private final static float AVATAR_RATIO_MAX = 0.25f;
    private final static float AVATAR_RATIO_DIFF = AVATAR_RATIO_START - AVATAR_RATIO_MAX;

    private final static float ANSWER_RATIO_START = 0.75f;
    private final static float ANSWER_RATIO_MAX = 0.35f;
    private final static float ANSWER_RATIO_DIFF = ANSWER_RATIO_START - ANSWER_RATIO_MAX;

    private final static float MIDDLE_RATIO_START = 0.7f;
    private final static float MIDDLE_RATIO_MAX = 0.1f;
    private final static float MIDDLE_RATIO_DIFF = MIDDLE_RATIO_START- MIDDLE_RATIO_MAX;

    private final static float FOOTER_RATIO_START = 1.1f;
    private final static float FOOTER_RATIO_MAX = 0.35f;
    private final static float FOOTER_RATIO_DIFF = FOOTER_RATIO_START - FOOTER_RATIO_MAX;

    private final View mHeader;
    private final View mHeaderAlpha;

    private final BaseInnerRecyclerView mRecyclerView;

    private final ImageView mAvatar;
    private final TextView mHeaderCaption1;
    private final TextView mHeaderCaption2;
    private final TextView mStrain;
    private final TextView mCropId;

    private final View mMiddle;
    private final View mMiddleAnswer;
    private final View mFooter;

    private final List<View> mMiddleCollapsible = new ArrayList<>(2);

    private final int m10dp;
    private final int m120dp;
    private final int mTitleSize1;
    private final int mTitleSize2;

    private boolean mIsScrolling;

    public OuterItem(View itemView, RecyclerView.RecycledViewPool pool) {
        super(itemView);

        // Init header
        m10dp = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.pad10dp);
        m120dp = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.pad120dp);
        mTitleSize1 = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.txt20sp);
        mTitleSize2 = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.txt17sp);

        mHeader = itemView.findViewById(R.id.header);
        mHeaderAlpha = itemView.findViewById(R.id.header_alpha);

        mHeaderCaption1 = itemView.findViewById(R.id.header_text_1);
        mHeaderCaption2 = itemView.findViewById(R.id.header_text_2);
        mStrain = itemView.findViewById(R.id.tv_strain);
        mCropId = itemView.findViewById(R.id.tv_crop_id);
        mAvatar = itemView.findViewById(R.id.avatar);

        mMiddle = itemView.findViewById(R.id.header_middle);
        mMiddleAnswer= itemView.findViewById(R.id.header_middle_answer);
        mFooter = itemView.findViewById(R.id.header_footer);

        mMiddleCollapsible.add((View)mAvatar.getParent());
        mMiddleCollapsible.add((View)mStrain.getParent());

        // Init RecyclerView
        mRecyclerView = itemView.findViewById(R.id.recyclerView);
        mRecyclerView.setRecycledViewPool(pool);
        mRecyclerView.setAdapter(new InnerAdapter());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mIsScrolling = newState != RecyclerView.SCROLL_STATE_IDLE;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                onItemScrolled(recyclerView, dx, dy);
            }
        });

        mRecyclerView.addItemDecoration(new HeaderDecorator(
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.pad170dp),
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.pad16dp)));

        // Init fonts
        DataBindingUtil.bind(((FrameLayout)mHeader).getChildAt(0));
    }

    @Override
    public boolean isScrolling() {
        return mIsScrolling;
    }

    @Override
    public BaseInnerRecyclerView getViewGroup() {
        return mRecyclerView;
    }

    @Override
    public View getHeader() {
        return mHeader;
    }

    @Override
    public View getHeaderAlphaView() {
        return mHeaderAlpha;
    }

    public void setContent(@NonNull List<InnerData> innerDataList) {
        final Context context = itemView.getContext();

        final InnerData header = innerDataList.subList(0, 1).get(0);
        final List<InnerData> tail = innerDataList.subList(1, innerDataList.size());

        mRecyclerView.setLayoutManager(new BaseInnerLayoutManager());
        ((InnerAdapter)mRecyclerView.getAdapter()).addData(tail);

        final String title1 = header.cropId + ":";

        final Spannable title2 = new SpannableString(header.cropId + " - " + header.strain);
        title2.setSpan(new AbsoluteSizeSpan(mTitleSize1), 0, title1.length(), SPAN_INCLUSIVE_INCLUSIVE);
        title2.setSpan(new AbsoluteSizeSpan(mTitleSize2), title1.length(), title2.length(), SPAN_INCLUSIVE_INCLUSIVE);
        title2.setSpan(new ForegroundColorSpan(Color.argb(204, 255, 255, 255)), title1.length(), title2.length(), SPAN_INCLUSIVE_INCLUSIVE);

        mHeaderCaption1.setText(title1);
        mHeaderCaption2.setText(title2);

        mStrain.setText(String.format("%s", header.strain));
        mCropId.setText(String.format("%s", new SimpleDateFormat("EEEE, MMMM d, yyyy").format(new Date(Long.parseLong(header.date.toString())))));
    }

    public void clearContent() {
        ((InnerAdapter)mRecyclerView.getAdapter()).clearData();
    }

    private float computeRatio(RecyclerView recyclerView) {
        final View child0 = recyclerView.getChildAt(0);
        final int pos = recyclerView.getChildAdapterPosition(child0);
        if (pos != 0) {
            return 0;
        }

        final int height = child0.getHeight();
        final float y = Math.max(0, child0.getY());
        return y / height;
    }

    private void onItemScrolled(RecyclerView recyclerView, int dx, int dy) {
        final float ratio = computeRatio(recyclerView);

        final float footerRatio = Math.max(0, Math.min(FOOTER_RATIO_START, ratio) - FOOTER_RATIO_DIFF) / FOOTER_RATIO_MAX;
        final float avatarRatio = Math.max(0, Math.min(AVATAR_RATIO_START, ratio) - AVATAR_RATIO_DIFF) / AVATAR_RATIO_MAX;
        final float answerRatio = Math.max(0, Math.min(ANSWER_RATIO_START, ratio) - ANSWER_RATIO_DIFF) / ANSWER_RATIO_MAX;
        final float middleRatio = Math.max(0, Math.min(MIDDLE_RATIO_START, ratio) - MIDDLE_RATIO_DIFF) / MIDDLE_RATIO_MAX;

        ViewCompat.setPivotY(mFooter, 0);
        ViewCompat.setScaleY(mFooter, footerRatio);
        ViewCompat.setAlpha(mFooter, footerRatio);

        ViewCompat.setPivotY(mMiddleAnswer, mMiddleAnswer.getHeight());
        ViewCompat.setScaleY(mMiddleAnswer, 1f - answerRatio);
        ViewCompat.setAlpha(mMiddleAnswer, 0.5f - answerRatio);

        ViewCompat.setAlpha(mHeaderCaption1, answerRatio);
        ViewCompat.setAlpha(mHeaderCaption2, 1f - answerRatio);

        final View mc2 = mMiddleCollapsible.get(1);
        ViewCompat.setPivotX(mc2, 0);
        ViewCompat.setPivotY(mc2, mc2.getHeight() / 2);

        for (final View view: mMiddleCollapsible) {
            ViewCompat.setScaleX(view, avatarRatio);
            ViewCompat.setScaleY(view, avatarRatio);
            ViewCompat.setAlpha(view, avatarRatio);
        }

        final ViewGroup.LayoutParams lp = mMiddle.getLayoutParams();
        lp.height = m120dp - (int)(m10dp * (1f - middleRatio));
        mMiddle.setLayoutParams(lp);
    }
}
