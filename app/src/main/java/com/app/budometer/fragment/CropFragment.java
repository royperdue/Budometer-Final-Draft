package com.app.budometer.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.app.budometer.R;
import com.app.budometer.util.BudometerSP;
import com.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import androidx.fragment.app.Fragment;


public class CropFragment extends BaseFragment {
    private CropFragment.OnCropFragmentInteractionListener mListener;
    private static CropFragment fragment = null;
    private CropImageView cropImageView;
    private ImageButton cropImageButton;
    private ImageButton closeImageButton;

    public interface OnCropFragmentInteractionListener {
        void onCropFragmentInteraction();
    }

    public CropFragment() {
    }

    public static Fragment newInstance() {
        if (fragment == null)
            fragment = new CropFragment();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CropFragment.OnCropFragmentInteractionListener) {
            mListener = (CropFragment.OnCropFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCropFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crop, container, false);

        cropImageView = view.findViewById(R.id.cropImageView);
        cropImageView.setAspectRatio(1, 1);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setGuidelines(1);

        cropImageButton = view.findViewById(R.id.cropImageButton);
        cropImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(cropImageView.getCroppedImage());
            }
        });

        closeImageButton = view.findViewById(R.id.closeImageButton);
        closeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bitmap bitmap = getBitmap(BudometerSP.init(getActivity()).getString(R.string.easy_prefs_key_path_selected_image));
        cropImageView.setImageBitmap(resizeBitmap(bitmap));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        cropImageView = null;
    }

    private Bitmap getBitmap(String path) {
        Bitmap bitmap = null;
        try {
            File file = new File(path);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap resizeBitmap(Bitmap bitmap) {
        Bitmap newBitmap = null;
        float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth() - 75;
        int height = Math.round(width / aspectRatio);

        newBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

        return newBitmap;
    }

    private void saveImage(Bitmap finalBitmap) {
        File file = new File(BudometerSP.init(getActivity()).getString(R.string.easy_prefs_key_path_selected_image));

        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyMediaScannerService(BudometerSP.init(getActivity()).getString(R.string.easy_prefs_key_path_selected_image));
    }

    private void notifyMediaScannerService(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            final Uri contentUri = Uri.fromFile(new File(path));
            scanIntent.setData(contentUri);
            getActivity().sendBroadcast(scanIntent);
        } else {
            final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(path));
            getActivity().sendBroadcast(intent);
        }

        mListener.onCropFragmentInteraction();
    }
}
