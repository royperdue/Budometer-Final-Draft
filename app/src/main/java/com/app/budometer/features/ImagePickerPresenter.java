package com.app.budometer.features;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import androidx.fragment.app.Fragment;

import android.widget.Toast;


import com.app.budometer.R;
import com.app.budometer.features.common.BaseConfig;
import com.app.budometer.features.common.BasePresenter;
import com.app.budometer.listener.ImageLoaderListener;
import com.app.budometer.model.Folder;
import com.app.budometer.model.Image;
import com.app.budometer.listener.ImagePickerView;
import com.app.budometer.util.BudometerUtils;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ImagePickerPresenter extends BasePresenter<ImagePickerView> {
    private ImageFileLoader imageLoader;
    private Handler main = new Handler(Looper.getMainLooper());

    public ImagePickerPresenter(ImageFileLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public void abortLoad() {
        imageLoader.abortLoadImages();
    }

    public void loadImages(ImagePickerConfig config) {
        if (!isViewAttached()) return;

        boolean isFolder = config.isFolderMode();
        boolean includeVideo = config.isIncludeVideo();
        ArrayList<File> excludedImages = config.getExcludedImages();

        runOnUiIfAvailable(() -> getView().showLoading(true));

        imageLoader.loadDeviceImages(isFolder, includeVideo, excludedImages, new ImageLoaderListener() {
            @Override
            public void onImageLoaded(final List<Image> images, final List<Folder> folders) {
                runOnUiIfAvailable(() -> {
                    getView().showFetchCompleted(images, folders);

                    final boolean isEmpty = folders != null
                            ? folders.isEmpty()
                            : images.isEmpty();

                    if (isEmpty) {
                        getView().showEmpty();
                    } else {
                        getView().showLoading(false);
                    }
                });
            }

            @Override
            public void onFailed(final Throwable throwable) {
                runOnUiIfAvailable(() -> getView().showError(throwable));
            }
        });
    }

    public void onDoneSelectImages(List<Image> selectedImages) {
        if (selectedImages != null && selectedImages.size() > 0) {
            /* Scan selected images which not existed */
            for (int i = 0; i < selectedImages.size(); i++) {
                Image image = selectedImages.get(i);
                File file = new File(image.getPath());
                if (!file.exists()) {
                    selectedImages.remove(i);
                    i--;
                }
            }
            getView().finishPickImages(selectedImages);
        }
    }

    private void runOnUiIfAvailable(Runnable runnable) {
        main.post(() -> {
            if (isViewAttached()) {
                runnable.run();
            }
        });
    }
}
