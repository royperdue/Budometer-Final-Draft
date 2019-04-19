package com.app.budometer.features.camera;

import android.content.Context;
import android.content.Intent;

import com.app.budometer.features.common.BaseConfig;
import com.app.budometer.listener.OnImageReadyListener;


public interface CameraModule {
    Intent getCameraIntent(Context context, BaseConfig config);
    void getImage(Context context, Intent intent, OnImageReadyListener imageReadyListener);
    void removeImage();
}
