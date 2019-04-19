package com.app.budometer.helper;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Region;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.app.budometer.R;
import com.app.budometer.layout.DLayoutManager;
import com.app.budometer.layout.ILayoutManager;
import com.app.budometer.layout.WeChatLayoutManager;
import com.app.budometer.listener.OnProgressListener;
import com.app.budometer.listener.OnSubItemClickListener;
import com.app.budometer.region.DRegionManager;
import com.app.budometer.region.IRegionManager;
import com.app.budometer.region.WeChatRegionManager;
import com.app.budometer.util.BudometerUtils;

import androidx.annotation.ColorInt;


public class Builder {
    public Context context;
    public ImageView imageView;
    public int size;
    public int gap;
    public int gapColor;
    public int placeholder = R.drawable.bud_placeholder;
    public int count;
    public int subSize;
    public ILayoutManager layoutManager;
    public Region[] regions;
    public OnSubItemClickListener subItemClickListener;
    public OnProgressListener progressListener;
    public Bitmap[] bitmaps;
    public int[] resourceIds;
    public String[] urls;

    public Builder(Context context) {
        this.context = context;
    }

    public Builder setImageView(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }

    public Builder setSize(int size) {
        this.size = BudometerUtils.dp2px(context, size);
        return this;
    }

    public Builder setGap(int gap) {
        this.gap = BudometerUtils.dp2px(context, gap);
        return this;
    }

    public Builder setGapColor(@ColorInt int gapColor) {
        this.gapColor = gapColor;
        return this;
    }

    public Builder setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public Builder setLayoutManager(ILayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }

    public Builder setOnProgressListener(OnProgressListener progressListener) {
        this.progressListener = progressListener;
        return this;
    }

    public Builder setOnSubItemClickListener(OnSubItemClickListener subItemClickListener) {
        this.subItemClickListener = subItemClickListener;
        return this;
    }

    public Builder setBitmaps(Bitmap... bitmaps) {
        this.bitmaps = bitmaps;
        this.count = bitmaps.length;
        return this;
    }

    public Builder setUrls(String... urls) {
        this.urls = urls;
        this.count = urls.length;
        return this;
    }

    public Builder setResourceIds(int... resourceIds) {
        this.resourceIds = resourceIds;
        this.count = resourceIds.length;
        return this;
    }

    public void build() {
        subSize = getSubSize(size, gap, layoutManager, count);
        initRegions();
        CombineHelper.init().load(this);
    }

    private int getSubSize(int size, int gap, ILayoutManager layoutManager, int count) {
        int subSize = 0;
        if (layoutManager instanceof DLayoutManager) {
            subSize = size;
        } else if (layoutManager instanceof WeChatLayoutManager) {
            if (count < 2) {
                subSize = size;
            } else if (count < 5) {
                subSize = (size - 3 * gap) / 2;
            } else if (count < 10) {
                subSize = (size - 4 * gap) / 3;
            }
        } else {
            throw new IllegalArgumentException("Must use DLayoutManager or WeChatRegionManager!");
        }
        return subSize;
    }

    private void initRegions() {
        if (subItemClickListener == null || imageView == null) {
            return;
        }

        IRegionManager regionManager;

        if (layoutManager instanceof DLayoutManager) {
            regionManager = new DRegionManager();
        } else if (layoutManager instanceof WeChatLayoutManager) {
            regionManager = new WeChatRegionManager();
        } else {
            throw new IllegalArgumentException("Must use DLayoutManager or WeChatRegionManager!");
        }

        regions = regionManager.calculateRegion(size, subSize, gap, count);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            int initIndex = -1;
            int currentIndex = -1;
            Point point = new Point();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                point.set((int) event.getX(), (int) event.getY());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initIndex = getRegionIndex(point.x, point.y);
                        currentIndex = initIndex;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        currentIndex = getRegionIndex(point.x, point.y);
                        break;
                    case MotionEvent.ACTION_UP:
                        currentIndex = getRegionIndex(point.x, point.y);
                        if (currentIndex != -1 && currentIndex == initIndex) {
                            subItemClickListener.onSubItemClick(currentIndex);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        initIndex = currentIndex = -1;
                        break;
                }
                return true;
            }
        });
    }

    private int getRegionIndex(int x, int y) {
        for (int i = 0; i < regions.length; i++) {
            if (regions[i].contains(x, y)) {
                return i;
            }
        }
        return -1;
    }
}
