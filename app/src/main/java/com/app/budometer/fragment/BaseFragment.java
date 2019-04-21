package com.app.budometer.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.budometer.R;
import com.app.budometer.activity.MainActivity;
import com.app.budometer.features.ImageFileLoader;
import com.app.budometer.features.ImagePicker;
import com.app.budometer.features.ImagePickerConfig;
import com.app.budometer.features.ImagePickerPresenter;
import com.app.budometer.features.ReturnMode;
import com.app.budometer.features.camera.CameraHelper;
import com.app.budometer.helper.IpLogger;
import com.app.budometer.listener.OnBackPressedListener;
import com.app.budometer.util.BudometerConfig;
import com.app.budometer.util.BudometerSP;
import com.app.budometer.views.SnackBarView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class BaseFragment extends Fragment implements OnBackPressedListener {
    protected IpLogger logger = IpLogger.getInstance();
    protected ImagePicker.ImagePickerWithActivity imagePickerWithActivity;
    protected ImagePickerPresenter presenter;
    protected ImagePickerConfig config;
    protected SnackBarView snackBarView;
    protected boolean isCameraOnly;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new ImagePickerPresenter(new ImageFileLoader(getActivity()));

        imagePickerWithActivity = ImagePicker.create(getActivity());
        imagePickerWithActivity
                .returnMode(ReturnMode.NONE)
                .folderMode(true)
                .toolbarFolderTitle("Folder")
                .toolbarImageTitle("Tap to select")
                .toolbarArrowColor(Color.WHITE)
                .includeVideo(false)
                .multi()
                .limit(4)
                .showCamera(true)
                .imageDirectory("Budometer")
                .theme(R.style.AppTheme)
                .enableLog(false);
        config = imagePickerWithActivity.getConfig();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    /**
     * Request for camera permission
     */
    public void captureImageWithPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final boolean isCameraGranted = ActivityCompat
                    .checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
            final boolean isWriteGranted = ActivityCompat
                    .checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            if (isCameraGranted && isWriteGranted) {
                captureImage();
            } else {
                logger.w("Camera permission is not granted. Requesting permission");
                requestCameraPermissions();
            }
        } else {
            captureImage();
        }
    }

    /**
     * Request for permission
     * If permission denied or app is first launched, request for permission
     * If permission denied and item_analysis choose 'Never Ask Again', show snackbar with an action that navigate to app settings
     */
    protected void requestWriteExternalPermission() {
        logger.w("Write External permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(permissions, BudometerConfig.RC_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            final String permission = BudometerSP.PREF_WRITE_EXTERNAL_STORAGE_REQUESTED;
            if (!BudometerSP.init(getActivity()).isPermissionRequested(permission)) {
                BudometerSP.init(getActivity()).setPermissionRequested(permission);
                requestPermissions(permissions, BudometerConfig.RC_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                ((MainActivity) getActivity()).getSnackBarView().show(R.string.msg_no_write_external_permission, v -> openAppSettings());
            }
        }
    }

    protected void requestCameraPermissions() {
        logger.w("Write External permission is not granted. Requesting permission");

        ArrayList<String> permissions = new ArrayList<>(2);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (checkForRationale(permissions)) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), BudometerConfig.RC_PERMISSION_REQUEST_CAMERA);
        } else {
            final String permission = BudometerSP.PREF_CAMERA_REQUESTED;
            if (!BudometerSP.init(getActivity()).isPermissionRequested(permission)) {
                BudometerSP.init(getActivity()).setPermissionRequested(permission);
                requestPermissions(permissions.toArray(new String[permissions.size()]), BudometerConfig.RC_PERMISSION_REQUEST_CAMERA);
            } else {
                if (isCameraOnly) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            getString(R.string.msg_no_camera_permission), Toast.LENGTH_SHORT).show();
                    //mListener.cancel();
                } else {
                    ((MainActivity) getActivity()).getSnackBarView().show(R.string.msg_no_camera_permission, v -> openAppSettings());
                }
            }
        }
    }

    protected boolean checkForRationale(List<String> permissions) {
        for (int i = 0, size = permissions.size(); i < size; i++) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handle permission results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case BudometerConfig.RC_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    logger.d("Write External permission granted");
                    getData();
                    return;
                }
                logger.e("Permission not granted: results len = " + grantResults.length +
                        " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));
                //mListener.cancel();
            }
            break;
            case BudometerConfig.RC_PERMISSION_REQUEST_CAMERA: {
                if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    logger.d("Camera permission granted");
                    captureImage();
                    return;
                }
                logger.e("Permission not granted: results len = " + grantResults.length +
                        " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));
                //mListener.cancel();
                break;
            }
            default: {
                logger.d("Got unexpected permission result: " + requestCode);
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
            }
        }
    }

    /**
     * Open app settings screen
     */
    protected void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getActivity().getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * Start camera intent
     * Create a temporary file and pass file Uri to camera intent
     */
    protected void captureImage() {
        if (!CameraHelper.checkCameraAvailability(getActivity())) {
            return;
        }
        //presenter.captureImage(this, config, BudometerConfig.RC_CAPTURE);
    }

    /**
     * Check permission
     */
    protected void getDataWithPermission() {
        int rc = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            getData();
        } else {
            requestWriteExternalPermission();
        }
    }

    protected void getData() {
        presenter.abortLoad();
        ImagePickerConfig config = imagePickerWithActivity.getConfig();
        if (config != null) {
            presenter.loadImages(config);
        }
    }

    public void onBackPressed() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
            getActivity().getSupportFragmentManager().popBackStack();
        else
            setMainFragment();
    }

    public void clearBackStack() {
        int backStackCount = getActivity().getSupportFragmentManager().getBackStackEntryCount();

        for (int i = backStackCount; i > 0; i--) {
            getActivity().getSupportFragmentManager().popBackStack();
        }

        setMainFragment();
    }

    public void setMainFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.fragment_container, MainFragment.newInstance())
                .addToBackStack(MainFragment.TAG).commit();
    }
}
