package com.app.budometer.fragment;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.budometer.R;
import com.app.budometer.util.BudometerConfig;
import com.app.budometer.util.BudometerSP;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.PictureResult;


public class CameraFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "Camera";
    private static CameraFragment fragment = null;
    private CameraFragment.OnCameraFragmentInteractionListener mListener;
    private ImageButton flashImageButton;
    private CameraView camera;

    public interface OnCameraFragmentInteractionListener {
        void onCameraFragmentInteraction();
    }

    public CameraFragment() {
    }

    public static Fragment newInstance() {
        if (fragment == null)
            fragment = new CameraFragment();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CameraFragment.OnCameraFragmentInteractionListener) {
            mListener = (CameraFragment.OnCameraFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCameraFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        camera = view.findViewById(R.id.camera);
        camera.setLifecycleOwner(getViewLifecycleOwner());
        camera.addCameraListener(new Listener());

        flashImageButton = view.findViewById(R.id.flashImageButton);
        flashImageButton.setOnClickListener(this);
        setFlash();
        view.findViewById(R.id.captureImageButton).setOnClickListener(this);
        view.findViewById(R.id.toggleCameraImageButton).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);
    }

    private void message(String content, boolean important) {
        int length = important ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(getActivity(), content, length).show();
    }

    private class Listener extends CameraListener {
        @Override
        public void onCameraOpened(@NonNull CameraOptions options) {

        }

        @Override
        public void onCameraError(@NonNull CameraException exception) {
            super.onCameraError(exception);
            message("Got CameraException #" + exception.getReason(), true);
        }

        @Override
        public void onPictureTaken(@NonNull PictureResult result) {
            super.onPictureTaken(result);

            CameraPreviewFragment.setPictureResult(result);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            transaction.replace(R.id.fragment_container, CameraPreviewFragment.newInstance())
                    .addToBackStack(CameraPreviewFragment.TAG).commit();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.flashImageButton: setFlash(); break;
            case R.id.captureImageButton: capturePicture(); break;
            case R.id.toggleCameraImageButton: toggleCamera(); break;
            case R.id.cancel: clearBackStack(); break;
        }
    }

    private void setFlash() {
        int cameraFlashSetting = BudometerSP.init(getActivity()).getInt(BudometerConfig.CAMERA_FLASH_SETTING, -1);

        if (flashImageButton.getTag() != null) {
            if (flashImageButton.getTag().equals("flash_off")) {
                flashImageButton.setBackgroundResource(0);
                flashImageButton.setImageResource(R.drawable.ic_flash_on_white_48dp);
                flashImageButton.setTag("flash_on");
                camera.setFlash(Flash.ON);
                cameraFlashSetting = 1;
            } else if (flashImageButton.getTag().equals("flash_on")) {
                flashImageButton.setBackgroundResource(0);
                flashImageButton.setImageResource(R.drawable.ic_flash_auto_white_48dp);
                flashImageButton.setTag("flash_auto");
                camera.setFlash(Flash.AUTO);
                cameraFlashSetting = 2;
            } else if (flashImageButton.getTag().equals("flash_auto")) {
                flashImageButton.setBackgroundResource(0);
                flashImageButton.setImageResource(R.drawable.ic_flash_off_white_48dp);
                flashImageButton.setTag("flash_off");
                camera.setFlash(Flash.OFF);
                cameraFlashSetting = 3;
            }
        } else if (cameraFlashSetting > -1) {
            if (cameraFlashSetting == 1) {
                flashImageButton.setBackgroundResource(0);
                flashImageButton.setImageResource(R.drawable.ic_flash_on_white_48dp);
                flashImageButton.setTag("flash_on");
                camera.setFlash(Flash.ON);
                cameraFlashSetting = 1;
            } else if (cameraFlashSetting == 2) {
                flashImageButton.setBackgroundResource(0);
                flashImageButton.setImageResource(R.drawable.ic_flash_auto_white_48dp);
                flashImageButton.setTag("flash_auto");
                camera.setFlash(Flash.AUTO);
                cameraFlashSetting = 2;
            } else if (cameraFlashSetting == 3) {
                flashImageButton.setBackgroundResource(0);
                flashImageButton.setImageResource(R.drawable.ic_flash_off_white_48dp);
                flashImageButton.setTag("flash_off");
                camera.setFlash(Flash.OFF);
                cameraFlashSetting = 3;
            }
        } else {
            flashImageButton.setBackgroundResource(0);
            flashImageButton.setImageResource(R.drawable.ic_flash_off_white_48dp);
            flashImageButton.setTag("flash_off");
            camera.setFlash(Flash.OFF);
            cameraFlashSetting = 3;
        }

        BudometerSP.init(getActivity()).putInt(BudometerConfig.CAMERA_FLASH_SETTING, cameraFlashSetting);
    }

    private void capturePicture() {
        if (camera.isTakingPicture()) return;
        message("Capturing picture...", false);
        camera.takePicture();
    }

    private void toggleCamera() {
        if (camera.isTakingPicture() || camera.isTakingVideo()) return;
        switch (camera.toggleFacing()) {
            case BACK:
                message("Switched to back camera!", false);
                break;
            case FRONT:
                message("Switched to front camera!", false);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean valid = true;
        for (int grantResult : grantResults) {
            valid = valid && grantResult == PackageManager.PERMISSION_GRANTED;
        }
        if (valid && !camera.isOpened()) {
            camera.open();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
