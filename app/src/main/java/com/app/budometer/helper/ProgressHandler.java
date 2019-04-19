package com.app.budometer.helper;


import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.app.budometer.listener.OnHandlerListener;


public class ProgressHandler extends Handler {
    private int i = 0;
    private Bitmap[] bitmaps;
    private Bitmap defaultBitmap;
    private OnHandlerListener listener;

    public ProgressHandler(Bitmap defaultBitmap, int count, OnHandlerListener listener) {
        this.defaultBitmap = defaultBitmap;
        this.bitmaps = new Bitmap[count];
        this.listener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 1:
                bitmaps[msg.arg1] = (Bitmap) msg.obj;
                break;
            case 2:
                bitmaps[msg.arg1] = defaultBitmap;
                break;
        }
        i++;
        if (i == bitmaps.length && listener != null) {
            listener.onComplete(bitmaps);
        }
    }
}
