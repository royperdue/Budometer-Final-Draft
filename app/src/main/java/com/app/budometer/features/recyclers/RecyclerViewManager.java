package com.app.budometer.features.recyclers;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.widget.Toast;


import com.app.budometer.R;
import com.app.budometer.adapter.FolderPickerAdapter;
import com.app.budometer.adapter.ImagePickerAdapter;
import com.app.budometer.features.ImagePickerConfig;
import com.app.budometer.features.ReturnMode;
import com.app.budometer.features.imageloader.ImageLoader;
import com.app.budometer.listener.OnFolderClickListener;
import com.app.budometer.listener.OnImageClickListener;
import com.app.budometer.listener.OnImageSelectedListener;
import com.app.budometer.model.Folder;
import com.app.budometer.model.Image;
import com.app.budometer.util.BudometerUtils;
import com.app.budometer.views.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.app.budometer.features.IpCons.MAX_LIMIT;
import static com.app.budometer.features.IpCons.MODE_MULTIPLE;
import static com.app.budometer.features.IpCons.MODE_SINGLE;


public class RecyclerViewManager {
    private final Context context;
    private final RecyclerView recyclerView;
    private final ImagePickerConfig config;

    private GridLayoutManager layoutManager;
    private GridSpacingItemDecoration itemOffsetDecoration;

    private ImagePickerAdapter imageAdapter;
    private FolderPickerAdapter folderAdapter;

    private Parcelable foldersState;

    private int imageColumns;
    private int folderColumns;

    public RecyclerViewManager(RecyclerView recyclerView, ImagePickerConfig config, int orientation) {
        this.recyclerView = recyclerView;
        this.config = config;
        this.context = recyclerView.getContext();
        changeOrientation(orientation);
    }

    public void onRestoreState(Parcelable recyclerState) {
        layoutManager.onRestoreInstanceState(recyclerState);
    }

    public Parcelable getRecyclerState() {
        return layoutManager.onSaveInstanceState();
    }

    /**
     * Set item size, column size base on the screen orientation
     */
    public void changeOrientation(int orientation) {
        imageColumns = orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 5;
        folderColumns = orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4;

        boolean shouldShowFolder = config.isFolderMode() && isDisplayingFolderView();
        int columns = shouldShowFolder ? folderColumns : imageColumns;
        layoutManager = new GridLayoutManager(context, columns);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        setItemDecoration(columns);
    }

    public void setupAdapters(ArrayList<Image> selectedImages, OnImageClickListener onImageClickListener, OnFolderClickListener onFolderClickListener) {
        if (config.getMode() == MODE_SINGLE && selectedImages != null && selectedImages.size() > 1) {
            selectedImages = null;
        }
        // Init folder and image adapter.
        final ImageLoader imageLoader = config.getImageLoader();
        imageAdapter = new ImagePickerAdapter(context, imageLoader, selectedImages, onImageClickListener);
        folderAdapter = new FolderPickerAdapter(context, imageLoader, bucket -> {
            foldersState = recyclerView.getLayoutManager().onSaveInstanceState();
            onFolderClickListener.onFolderClick(bucket);
        });
    }

    private void setItemDecoration(int columns) {
        if (itemOffsetDecoration != null) {
            recyclerView.removeItemDecoration(itemOffsetDecoration);
        }
        itemOffsetDecoration = new GridSpacingItemDecoration(
                columns,
                context.getResources().getDimensionPixelSize(R.dimen.pad1dp),
                false
        );
        recyclerView.addItemDecoration(itemOffsetDecoration);

        layoutManager.setSpanCount(columns);
    }

    // Returns true if a back action was handled by going back a folder; false otherwise.
    public boolean handleBack() {
        if (config.isFolderMode() && !isDisplayingFolderView()) {
            setFolderAdapter(null);
            return true;
        }
        return false;
    }

    private boolean isDisplayingFolderView() {
        return recyclerView.getAdapter() == null || recyclerView.getAdapter() instanceof FolderPickerAdapter;
    }

    public String getTitle() {
        if (isDisplayingFolderView()) {
            return BudometerUtils.getFolderTitle(context, config);
        }

        if (config.getMode() == MODE_SINGLE) {
            return BudometerUtils.getImageTitle(context, config);
        }

        final int imageSize = imageAdapter.getSelectedImages().size();
        final boolean useDefaultTitle = !BudometerUtils.isStringEmpty(config.getImageTitle()) && imageSize == 0;

        if (useDefaultTitle) {
            return BudometerUtils.getImageTitle(context, config);
        }
        return config.getLimit() == MAX_LIMIT
                ? String.format(context.getString(R.string.selected), imageSize)
                : String.format(context.getString(R.string.selected_with_limit), imageSize, config.getLimit());
    }

    public void setImageAdapter(List<Image> images) {
        imageAdapter.setData(images);
        setItemDecoration(imageColumns);
        recyclerView.setAdapter(imageAdapter);
    }

    public ImagePickerAdapter getImageAdapter() {
        return (ImagePickerAdapter) recyclerView.getAdapter();
    }

    public void setFolderAdapter(List<Folder> folders) {
        folderAdapter.setData(folders);
        setItemDecoration(folderColumns);
        recyclerView.setAdapter(folderAdapter);

        if (foldersState != null) {
            layoutManager.setSpanCount(folderColumns);
            recyclerView.getLayoutManager().onRestoreInstanceState(foldersState);
        }
    }

    /* --------------------------------------------------- */
    /* > Images */
    /* --------------------------------------------------- */

    private void checkAdapterIsInitialized() {
        if (imageAdapter == null) {
            throw new IllegalStateException("Must call setupAdapters first!");
        }
    }

    public List<Image> getSelectedImages() {
        checkAdapterIsInitialized();
        return imageAdapter.getSelectedImages();
    }

    public FolderPickerAdapter getFolderAdapter() {
        return folderAdapter;
    }

    public void setImageSelectedListener(OnImageSelectedListener listener) {
        checkAdapterIsInitialized();
        imageAdapter.setImageSelectedListener(listener);
    }

    public boolean selectImage(boolean isSelected) {
        if (config.getMode() == MODE_MULTIPLE) {
            if (imageAdapter.getSelectedImages().size() >= config.getLimit() && !isSelected) {
                Toast.makeText(context, R.string.msg_limit_images, Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (config.getMode() == MODE_SINGLE) {
            if (imageAdapter.getSelectedImages().size() > 0) {
                imageAdapter.removeAllSelectedSingleClick();
            }
        }
        return true;
    }

    public boolean isShowDoneButton() {
        return !isDisplayingFolderView()
                && !imageAdapter.getSelectedImages().isEmpty()
                && (config.getReturnMode() != ReturnMode.ALL && config.getReturnMode() != ReturnMode.GALLERY_ONLY);
    }
}
