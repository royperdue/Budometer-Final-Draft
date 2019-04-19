package com.app.budometer.model;


import com.app.budometer.util.BudometerUtils;

import java.util.ArrayList;
import java.util.List;

public class ImageFactory {
    public static List<Image> singleListFromPath(String path) {
        List<Image> images = new ArrayList<>();
        images.add(new Image(0, BudometerUtils.getNameFromFilePath(path), path));
        return images;
    }
}
