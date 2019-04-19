package com.app.budometer.features.camera;


import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;

import com.app.budometer.features.ImagePickerConfigFactory;
import com.app.budometer.features.common.BaseConfig;
import com.app.budometer.helper.IpLogger;
import com.app.budometer.listener.OnImageReadyListener;
import com.app.budometer.model.ImageFactory;
import com.app.budometer.util.BudometerUtils;
import com.desmond.squarecamera.CameraActivity;

import java.io.File;
import java.io.Serializable;
import java.util.Locale;

import androidx.core.content.FileProvider;

public class DefaultCameraModule implements CameraModule, Serializable {
    private String currentImagePath;

    public Intent getCameraIntent(Context context) {
        return getCameraIntent(context, ImagePickerConfigFactory.createDefault());
    }

    @Override
    public Intent getCameraIntent(Context context, BaseConfig config) {
        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent intent = new Intent(context, CameraActivity.class);
        File imageFile = BudometerUtils.createImageFile(config.getImageDirectory());
        if (imageFile != null) {
            Context appContext = context.getApplicationContext();
            String providerName = String.format(Locale.ENGLISH, "%s%s", appContext.getPackageName(), ".imagepicker.provider");
            Uri uri = FileProvider.getUriForFile(appContext, providerName, imageFile);
            currentImagePath = "file:" + imageFile.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            BudometerUtils.grantAppPermission(context, intent, uri);

            return intent;
        }
        return null;
    }

    @Override
    public void getImage(final Context context, Intent intent, final OnImageReadyListener imageReadyListener) {
        if (imageReadyListener == null) {
            throw new IllegalStateException("OnImageReadyListener must not be null");
        }

        if (currentImagePath == null) {
            IpLogger.getInstance().w("currentImagePath null. " +
                    "This happen if you haven't call #getCameraIntent() or the activity is being recreated");
            imageReadyListener.onImageReady(null);
            return;
        }

        final Uri imageUri = Uri.parse(currentImagePath);
        if (imageUri != null) {
            MediaScannerConnection.scanFile(context.getApplicationContext(),
                    new String[]{imageUri.getPath()}, null, (path, uri) -> {

                        IpLogger.getInstance().d("File " + path + " was scanned successfully: " + uri);

                        if (path == null) {
                            IpLogger.getInstance().d("This should not happen, go back to Immediate implemenation");
                            path = currentImagePath;
                        }

                        imageReadyListener.onImageReady(ImageFactory.singleListFromPath(path));
                        BudometerUtils.revokeAppPermission(context, imageUri);
                    });
        }
    }

    @Override
    public void removeImage() {
        if (currentImagePath != null) {
            File file = new File(currentImagePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}