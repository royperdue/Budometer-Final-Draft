package com.app.budometer.listener;


import com.app.budometer.model.Image;

import java.util.List;

public interface OnImageReadyListener {
    void onImageReady(List<Image> image);
}
