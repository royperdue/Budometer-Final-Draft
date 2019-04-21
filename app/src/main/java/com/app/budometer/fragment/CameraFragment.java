package com.app.budometer.fragment;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.budometer.R;
import com.app.budometer.features.camera.Control;
import com.app.budometer.views.ControlView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;


public class CameraFragment extends BaseFragment implements View.OnClickListener, ControlView.Callback {
    public static final String TAG = "Camera";
    private static CameraFragment fragment = null;
    private CameraFragment.OnCameraFragmentInteractionListener mListener;
    private CameraView camera;
    private ViewGroup controlPanel;

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

        view.findViewById(R.id.editImageButton).setOnClickListener(this);
        view.findViewById(R.id.captureImageButton).setOnClickListener(this);
        view.findViewById(R.id.toggleCameraImageButton).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);

        controlPanel = view.findViewById(R.id.controls);
        ViewGroup group = (ViewGroup) controlPanel.getChildAt(0);
        Control[] controls = Control.values();

        for (Control control : controls) {
            ControlView controlView = new ControlView(getActivity(), control, this);
            group.addView(controlView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        controlPanel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(controlPanel);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    private void message(String content, boolean important) {
        int length = important ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(getActivity(), content, length).show();
    }

    private class Listener extends CameraListener {

        @Override
        public void onCameraOpened(@NonNull CameraOptions options) {
            ViewGroup group = (ViewGroup) controlPanel.getChildAt(0);
            for (int i = 0; i < group.getChildCount(); i++) {
                ControlView controlView = (ControlView) group.getChildAt(i);
                controlView.onCameraOpened(camera, options);
            }
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
            case R.id.editImageButton: edit(); break;
            case R.id.captureImageButton: capturePicture(); break;
            case R.id.toggleCameraImageButton: toggleCamera(); break;
            case R.id.cancel: closeBottomSheet(); break;
        }
    }

    private void closeBottomSheet() {
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(controlPanel);
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }

        clearBackStack();
    }

    private void edit() {
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(controlPanel);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
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
    public boolean onValueChanged(Control control, Object value, String name) {
        control.applyValue(camera, value);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(controlPanel);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        message("Changed " + control.getName() + " to " + name, false);
        return true;
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
