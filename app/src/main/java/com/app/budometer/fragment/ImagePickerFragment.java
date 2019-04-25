package com.app.budometer.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.app.budometer.R;
import com.app.budometer.features.ImagePickerConfig;
import com.app.budometer.listener.ImagePickerView;
import com.app.budometer.features.IpCons;
import com.app.budometer.features.common.BaseConfig;
import com.app.budometer.features.recyclers.RecyclerViewManager;
import com.app.budometer.model.Folder;
import com.app.budometer.model.Image;
import com.app.budometer.util.BudometerConfig;
import com.app.budometer.util.BudometerUtils;
import com.app.budometer.views.CircleMenuView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import dmax.dialog.SpotsDialog;


public class ImagePickerFragment extends BaseFragment implements ImagePickerView {
    public static final String TAG = "Select Images";
    private static ImagePickerFragment fragment = null;
    private OnImagePickerInteractionListener mListener;
    private RecyclerViewManager recyclerViewManager;
    private Handler handler;
    private ContentObserver observer;
    private RecyclerView recyclerView;
    private SpotsDialog progressDialog;
    private TextView emptyTextView;

    public interface OnImagePickerInteractionListener {
        void setTitle(String title);

        void cancel();

        void finishPickImages(Intent result);

        void selectionChanged(List<Image> imageList);

        void onPickerFragmentInteraction(int index);
    }

    public ImagePickerFragment() {
    }

    public static ImagePickerFragment newInstance() {
        if (fragment == null)
            fragment = new ImagePickerFragment();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnImagePickerInteractionListener) {
            mListener = (OnImagePickerInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnImagePickerInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        observer = null;
        startContentObserver();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setupComponents();

        LayoutInflater localInflater = inflater.cloneInContext(new ContextThemeWrapper(getActivity(), config.getTheme()));

        View view = localInflater.inflate(R.layout.fragment_image_picker, container, false);
        setupView(view);

        setupRecyclerView(config, config.getSelectedImages());

        if (savedInstanceState == null) {
            setupRecyclerView(config, config.getSelectedImages());
        } else {
            setupRecyclerView(config, savedInstanceState.getParcelableArrayList(BudometerConfig.STATE_KEY_SELECTED_IMAGES));
            recyclerViewManager.onRestoreState(savedInstanceState.getParcelable(BudometerConfig.STATE_KEY_RECYCLER));
        }

        mListener.selectionChanged(recyclerViewManager.getSelectedImages());

        CircleMenuView circleMenuView = view.findViewById(R.id.circle_menu);
        circleMenuView.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
            }

            @Override
            public void onMenuOpenAnimationEnd(@NonNull CircleMenuView view) {
            }

            @Override
            public void onMenuCloseAnimationStart(@NonNull CircleMenuView view) {
            }

            @Override
            public void onMenuCloseAnimationEnd(@NonNull CircleMenuView view) {
            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int index) {
                if (index == 0) {
                    mListener.onPickerFragmentInteraction(index);
                } else if (index == 1) {
                    mListener.onPickerFragmentInteraction(index);
                } else if (index == 2) {
                    mListener.onPickerFragmentInteraction(index);
                } else if (index == 3) {
                    mListener.onPickerFragmentInteraction(index);
                } else if (index == 4) {
                    mListener.onPickerFragmentInteraction(index);
                }
            }

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
            }

            @Override
            public boolean onButtonLongClick(@NonNull CircleMenuView view, int index) {
                return true;
            }

            @Override
            public void onButtonLongClickAnimationStart(@NonNull CircleMenuView view, int index) {
            }

            @Override
            public void onButtonLongClickAnimationEnd(@NonNull CircleMenuView view, int index) {
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (!isCameraOnly) {
            outState.putParcelable(BudometerConfig.STATE_KEY_RECYCLER, recyclerViewManager.getRecyclerState());
            outState.putParcelableArrayList(BudometerConfig.STATE_KEY_SELECTED_IMAGES, (ArrayList<? extends Parcelable>)
                    recyclerViewManager.getSelectedImages());
        }
    }

    private void setupView(View rootView) {
        progressDialog = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(getActivity())
                .setTheme(R.style.CustomProgressDialog)
                .setMessage(R.string.loading).build();
        emptyTextView = rootView.findViewById(R.id.emptyImagesTextView);
        recyclerView = rootView.findViewById(R.id.recyclerView);
    }

    private void setupRecyclerView(ImagePickerConfig config, ArrayList<Image> selectedImages) {
        recyclerViewManager = new RecyclerViewManager(
                recyclerView,
                config,
                getResources().getConfiguration().orientation
        );

        recyclerViewManager.setupAdapters(selectedImages, (isSelected) -> recyclerViewManager.selectImage(isSelected)
                , bucket -> setImageAdapter(bucket.getImages()));

        recyclerViewManager.setImageSelectedListener(selectedImage -> {
            updateTitle();
            mListener.selectionChanged(recyclerViewManager.getSelectedImages());
            if (BudometerUtils.shouldReturn(config, false) && !selectedImage.isEmpty()) {
                onDone();
            }
        });
    }

    private void setupComponents() {
        presenter.attachView(this);
    }

    private BaseConfig getBaseConfig() {
        return imagePickerWithActivity.getConfig();
    }

    /**
     * Set image adapter
     * 1. Set new data
     * 2. Update item decoration
     * 3. Update title
     */
    void setImageAdapter(List<Image> images) {
        recyclerViewManager.setImageAdapter(images);
        updateTitle();
    }

    void setFolderAdapter(List<Folder> folders) {
        recyclerViewManager.setFolderAdapter(folders);
        updateTitle();
    }

    private void updateTitle() {
        mListener.setTitle(recyclerViewManager.getTitle());
    }

    /**
     * On finish selected image
     * Get all selected images then return image to caller activity
     */
    public void onDone() {
        presenter.onDoneSelectImages(recyclerViewManager.getSelectedImages());
        recyclerViewManager.getImageAdapter().clearSelectedImages();
    }

    /**
     * Config recyclerView when configuration changed
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (recyclerViewManager != null) {
            // recyclerViewManager can be null here if we use cameraOnly mode
            recyclerViewManager.changeOrientation(newConfig.orientation);
        }
    }

    private void startContentObserver() {
        if (isCameraOnly) {
            return;
        }
        if (handler == null) {
            handler = new Handler();
        }
        observer = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange) {
                getData();
            }
        };

        getActivity().getContentResolver()
                .registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, observer);
    }

    public void refreshObserver() {
        observer.deliverSelfNotifications();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isCameraOnly)
            getDataWithPermission();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.abortLoad();
            presenter.detachView();
        }

        if (observer != null) {
            getActivity().getContentResolver().unregisterContentObserver(observer);
            observer = null;
        }

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    @Override
    public void onBackPressed() {
        recyclerViewManager.handleBack();
    }

    public boolean isShowDoneButton() {
        return recyclerViewManager.isShowDoneButton();
    }

    public void setInteractionListener(OnImagePickerInteractionListener listener) {
        mListener = listener;
    }

    /* --------------------------------------------------- */
    /* > View Methods */
    /* --------------------------------------------------- */

    @Override
    public void finishPickImages(List<Image> images) {
        Intent data = new Intent();
        data.putParcelableArrayListExtra(IpCons.EXTRA_SELECTED_IMAGES, (ArrayList<? extends Parcelable>) images);
        mListener.finishPickImages(data);
    }

    @Override
    public void showCapturedImage() {
        getDataWithPermission();
    }

    @Override
    public void showFetchCompleted(List<Image> images, List<Folder> folders) {
        ImagePickerConfig config = imagePickerWithActivity.getConfig();
        if (config != null && config.isFolderMode()) {
            setFolderAdapter(folders);
        } else {
            setImageAdapter(images);
        }
    }

    @Override
    public void showError(Throwable throwable) {
        String message = "Unknown Error";
        if (throwable != null && throwable instanceof NullPointerException) {
            message = "Images do not exist";
        }
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(boolean isLoading) {
        if (isLoading)
            progressDialog.show();
        else
            progressDialog.dismiss();

        recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        emptyTextView.setVisibility(View.GONE);
    }

    @Override
    public void showEmpty() {
        progressDialog.dismiss();
        recyclerView.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
    }
}
