package com.app.budometer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.budometer.R;
import com.app.budometer.features.imageloader.ImageLoader;
import com.app.budometer.features.imageloader.ImageType;
import com.app.budometer.util.BudometerSP;
import com.app.budometer.listener.OnImageClickListener;
import com.app.budometer.listener.OnImageSelectedListener;
import com.app.budometer.model.Image;
import com.app.budometer.util.BudometerUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ImagePickerAdapter extends BaseListAdapter<ImagePickerAdapter.ImageViewHolder> {
    private List<Image> images = new ArrayList<>();
    private List<Image> selectedImages = new ArrayList<>();
    private OnImageClickListener itemClickListener;
    private OnImageSelectedListener imageSelectedListener;
    private Context context;

    public ImagePickerAdapter(Context context, ImageLoader imageLoader, List<Image> selectedImages, OnImageClickListener itemClickListener) {
        super(context, imageLoader);
        
        this.context = context;
        this.itemClickListener = itemClickListener;
        BudometerSP.init(context).putInt(R.string.easy_prefs_key_selected_images_count, 0);

        if (selectedImages != null && !selectedImages.isEmpty()) {
            this.selectedImages.addAll(selectedImages);

            BudometerSP.init(context).putInt(R.string.easy_prefs_key_selected_images_count, this.selectedImages.size());
        }
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(
                getInflater().inflate(R.layout.item_image_picker_image, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ImageViewHolder viewHolder, int position) {
        final Image image = images.get(position);
        final boolean isSelected = isSelected(image);

        getImageLoader().loadImage(
                image.getPath(),
                viewHolder.imageView,
                ImageType.GALLERY
        );

        boolean showFileTypeIndicator = false;
        String fileTypeLabel = "";
        if(BudometerUtils.isGifFormat(image)) {
            fileTypeLabel = getContext().getResources().getString(R.string.gif);
            showFileTypeIndicator = true;
        }
        if(BudometerUtils.isVideoFormat(image)) {
            fileTypeLabel = getContext().getResources().getString(R.string.video);
            showFileTypeIndicator = true;
        }
        viewHolder.fileTypeIndicator.setText(fileTypeLabel);
        viewHolder.fileTypeIndicator.setVisibility(showFileTypeIndicator
                ? View.VISIBLE
                : View.GONE);

        viewHolder.alphaView.setAlpha(isSelected
                ? 0.5f
                : 0f);

        // Save URI with sharedPreferences so can access image for cropping.
        viewHolder.itemView.setOnClickListener(v -> {
            boolean shouldSelect = itemClickListener.onImageClick(
                    isSelected
            );

            if (isSelected) {
                removeSelectedImage(image, position);
                BudometerSP.init(context).putString(context.getString(R.string.easy_prefs_key_path_selected_image), "");
            } else if (shouldSelect) {
                addSelected(image, position);
                BudometerSP.init(context).putString(context.getString(R.string.easy_prefs_key_path_selected_image), image.getPath());
            }
        });

        viewHolder.container.setForeground(isSelected
                ? ContextCompat.getDrawable(getContext(), R.drawable.ic_done_white_24dp)
                : null);
    }

    private boolean isSelected(Image image) {
        for (Image selectedImage : selectedImages) {
            if (selectedImage.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    public void setData(List<Image> images) {
        this.images.clear();
        this.images.addAll(images);
    }

    private void addSelected(final Image image, final int position) {
        mutateSelection(() -> {
            selectedImages.add(image);
            notifyItemChanged(position);
        });

        BudometerSP.init(context).putInt(R.string.easy_prefs_key_selected_images_count, this.selectedImages.size());
    }

    private void removeSelectedImage(final Image image, final int position) {
        mutateSelection(() -> {
            selectedImages.remove(image);
            notifyItemChanged(position);
        });

        BudometerSP.init(context).putInt(R.string.easy_prefs_key_selected_images_count, this.selectedImages.size());
    }

    public void removeAllSelectedSingleClick() {
        mutateSelection(() -> {
            selectedImages.clear();
            notifyDataSetChanged();
        });

        BudometerSP.init(context).putInt(R.string.easy_prefs_key_selected_images_count, this.selectedImages.size());
    }

    public void clearSelectedImages() {
        mutateSelection(() -> {
            selectedImages.clear();
            notifyDataSetChanged();
        });

        BudometerSP.init(context).putInt(R.string.easy_prefs_key_selected_images_count, this.selectedImages.size());
    }

    private void mutateSelection(Runnable runnable) {
        runnable.run();
        if (imageSelectedListener != null) {
            imageSelectedListener.onSelectionUpdate(selectedImages);
        }
    }

    public void setImageSelectedListener(OnImageSelectedListener imageSelectedListener) {
        this.imageSelectedListener = imageSelectedListener;
    }

    public Image getItem(int position) {
        return images.get(position);
    }

    public List<Image> getSelectedImages() {
        return selectedImages;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private View alphaView;
        private TextView fileTypeIndicator;
        private FrameLayout container;

        ImageViewHolder(View itemView) {
            super(itemView);

            container = (FrameLayout) itemView;
            imageView = itemView.findViewById(R.id.image_view);
            alphaView = itemView.findViewById(R.id.view_alpha);
            fileTypeIndicator = itemView.findViewById(R.id.item_file_type_indicator);
        }
    }
}
