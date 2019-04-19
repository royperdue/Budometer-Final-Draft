package com.app.budometer.util;


import android.view.animation.Interpolator;

import androidx.interpolator.view.animation.FastOutLinearInInterpolator;


public final class BudometerConfig {
    public static final String ARG_TF_TITLE_GROWING = "arg_tf_title_growing**";
    public static final String ARG_TF_CONFIDENCE_GROWING = "arg_tf_confidence_growing**";
    public static final String ARG_TF_TITLE_READY = "arg_tf_title_ready**";
    public static final String ARG_TF_CONFIDENCE_READY = "arg_tf_confidence_ready**";

    public static final String GOT_IT = "got_it**";

    public static final String IMAGE_PATH_1 = "image_path_1**";
    public static final String IMAGE_PATH_2 = "image_path_2**";
    public static final String IMAGE_PATH_3 = "image_path_3**";
    public static final String IMAGE_PATH_4 = "image_path_4**";

    public static final String BUNDLE_ANALYSIS_ID = "bundle_analysis_id";
    public static final String GREEN_DAO_ANALYSIS_ID = "green_dao_analysis_id";

    public static final String INPUT_NAME = "input";
    public static final String OUTPUT_NAME = "final_result";
    public static final String MODEL_FILE = "file:///android_asset/optimized_graph.pb";

    public static final String STATE_KEY_CAMERA_MODULE = "Key.CameraModule";
    public static final String STATE_KEY_RECYCLER = "Key.Recycler";
    public static final String STATE_KEY_SELECTED_IMAGES = "Key.SelectedImages";

    public static final int RC_CAPTURE = 2000;
    public static final int RC_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 23;
    public static final int RC_PERMISSION_REQUEST_CAMERA = 24;

    public static final int INPUT_SIZE = 224;
    public static final int IMAGE_MEAN = 128;
    public static final float IMAGE_STD = 128.0f;

    public static final int DEFAULT_BUTTON_SIZE = 42;
    public static final float DEFAULT_DISTANCE = DEFAULT_BUTTON_SIZE * 1.5f;
    public static final float DEFAULT_RING_SCALE_RATIO = 1.1f;
    public static final float DEFAULT_CLOSE_ICON_ALPHA = 0.3f;

    public static final int STEP_DEGREE = 5;

    public static final int ANIM_DURATION = 200;
    public static final Interpolator INTERPOLATOR = new FastOutLinearInInterpolator();
}
