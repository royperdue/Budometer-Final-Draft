package com.app.budometer.listener;



import com.app.budometer.features.common.MvpView;
import com.app.budometer.model.Folder;
import com.app.budometer.model.Image;

import java.util.List;

public interface ImagePickerView extends MvpView {
    void showLoading(boolean isLoading);
    void showFetchCompleted(List<Image> images, List<Folder> folders);
    void showError(Throwable throwable);
    void showEmpty();
    void showCapturedImage();
    void finishPickImages(List<Image> images);
}
