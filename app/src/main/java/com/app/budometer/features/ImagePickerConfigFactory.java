package com.app.budometer.features;


import com.app.budometer.features.imageloader.DefaultImageLoader;

import java.util.ArrayList;

public class ImagePickerConfigFactory {
    public static ImagePickerConfig createDefault() {
        ImagePickerConfig config = new ImagePickerConfig();
        config.setMode(IpCons.MODE_MULTIPLE);
        config.setLimit(IpCons.MAX_LIMIT);
        config.setShowCamera(true);
        config.setFolderMode(false);
        config.setSelectedImages(new ArrayList<>());
        config.setSavePath(ImagePickerSavePath.DEFAULT);
        config.setReturnMode(ReturnMode.NONE);
        config.setImageLoader(new DefaultImageLoader());
        return config;
    }
}
