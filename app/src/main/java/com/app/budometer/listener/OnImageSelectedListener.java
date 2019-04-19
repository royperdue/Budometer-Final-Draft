package com.app.budometer.listener;


import com.app.budometer.model.Image;

import java.util.List;

public interface OnImageSelectedListener {
    void onSelectionUpdate(List<Image> selectedImage);
}
