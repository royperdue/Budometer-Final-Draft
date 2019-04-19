package com.app.budometer.features.imageloader;

import android.widget.ImageView;

import com.app.budometer.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;


public class DefaultImageLoader implements ImageLoader {
    @Override
    public void loadImage(String path, ImageView imageView, ImageType imageType) {
        Glide.with(imageView.getContext())
                .load(path)
                .apply(new RequestOptions()
                        .placeholder(imageType == ImageType.FOLDER
                                ? R.drawable.folder_placeholder
                                : R.drawable.image_placeholder)
                        .error(imageType == ImageType.FOLDER
                                ? R.drawable.folder_placeholder
                                : R.drawable.image_placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                )
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }
}
