package com.app.budometer.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.budometer.R;
import com.app.budometer.util.BudometerUtils;
import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.PictureResult;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;


public class CameraPreviewFragment extends BaseFragment {
    private static CameraPreviewFragment fragment = null;
    private static WeakReference<PictureResult> image;

    public static void setPictureResult(@Nullable PictureResult im) {
        image = im != null ? new WeakReference<>(im) : null;
    }

    public CameraPreviewFragment() {
    }

    public static Fragment newInstance() {
        if (fragment == null)
            fragment = new CameraPreviewFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera_preview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageView imageView = view.findViewById(R.id.image);

        final LinearLayout saveImageButton = view.findViewById(R.id.savePhotoButton);
        saveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                saveImage(drawable.getBitmap());
            }
        });

        final LinearLayout retakeButton = view.findViewById(R.id.retakeButton);
        retakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreCamera();
            }
        });

        final ImageButton closeButton = view.findViewById(R.id.cancel);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        PictureResult result = image == null ? null : image.get();
        if (result == null) {
            getActivity().finish();
        }

        result.toBitmap(1000, 1000, new BitmapCallback() {
            @Override
            public void onBitmapReady(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    private void saveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File directory = new File(root + "/Budometer");
        directory.mkdirs();

        File file = new File(directory, BudometerUtils.createFileName() + ".jpg");

        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            galleryAddPic(file.getPath());
            restoreCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void galleryAddPic(String filePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(filePath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void restoreCamera() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, CameraFragment.newInstance(), CameraFragment.TAG)
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if (!isChangingConfigurations()) {
            setPictureResult(null);
        }*/
    }
}
