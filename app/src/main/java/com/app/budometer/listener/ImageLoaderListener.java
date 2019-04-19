package com.app.budometer.listener;


import com.app.budometer.model.Folder;
import com.app.budometer.model.Image;

import java.util.List;

public interface ImageLoaderListener {
    void onImageLoaded(List<Image> images, List<Folder> folders);
    void onFailed(Throwable throwable);
}
