package com.app.budometer.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;

import com.app.budometer.R;
import com.app.budometer.features.ImagePickerConfig;
import com.app.budometer.features.ImagePickerSavePath;
import com.app.budometer.features.IpCons;
import com.app.budometer.features.ReturnMode;
import com.app.budometer.features.common.BaseConfig;
import com.app.budometer.model.Image;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class BudometerUtils {
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String hashKeyFormUrl(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static ImagePickerConfig checkConfig(ImagePickerConfig config) {
        if (config == null) {
            throw new IllegalStateException("ImagePickerConfig cannot be null");
        }
        if (config.getMode() != IpCons.MODE_SINGLE
                && (config.getReturnMode() == ReturnMode.GALLERY_ONLY
                || config.getReturnMode() == ReturnMode.ALL)) {
            throw new IllegalStateException("ReturnMode.GALLERY_ONLY and ReturnMode.ALL is only applicable in Single Mode!");
        }
        if (config.getImageLoader() != null && !(config.getImageLoader() instanceof Serializable)) {
            throw new IllegalStateException("Custom image loader must be a class that implement ImageLoader." +
                    " This limitation due to Serializeable");
        }
        return config;
    }

    public static boolean shouldReturn(BaseConfig config, boolean isCamera) {
        ReturnMode mode = config.getReturnMode();
        if (isCamera) {
            return mode == ReturnMode.ALL || mode == ReturnMode.CAMERA_ONLY;
        } else {
            return mode == ReturnMode.ALL || mode == ReturnMode.GALLERY_ONLY;
        }
    }

    public static String getFolderTitle(Context context, ImagePickerConfig config) {
        final String folderTitle = config.getFolderTitle();
        return BudometerUtils.isStringEmpty(folderTitle)
                ? context.getString(R.string.title_folder)
                : folderTitle;
    }

    public static String getImageTitle(Context context, ImagePickerConfig config) {
        final String configImageTitle = config.getImageTitle();
        return BudometerUtils.isStringEmpty(configImageTitle)
                ? context.getString(R.string.title_select_image)
                : configImageTitle;
    }

    public static boolean isStringEmpty(@Nullable String str) {
        return str == null || str.length() == 0;
    }

    public static File createImageFile(ImagePickerSavePath savePath) {
        // External sdcard location
        final String path = savePath.getPath();
        File mediaStorageDir = savePath.isFullPath()
                ? new File(path)
                : new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), path);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                //IpLogger.getInstance().d("Oops! Failed create " + path);
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp;

        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", mediaStorageDir);
        } catch (IOException e) {
            //IpLogger.getInstance().d("Oops! Failed create " + imageFileName + " file");
        }
        return imageFile;
    }

    public static String getNameFromFilePath(String path) {
        if (path.contains(File.separator)) {
            return path.substring(path.lastIndexOf(File.separator) + 1);
        }
        return path;
    }

    public static void grantAppPermission(Context context, Intent intent, Uri fileUri) {
        List<ResolveInfo> resolvedIntentActivities = context.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
            String packageName = resolvedIntentInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, fileUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    public static void revokeAppPermission(Context context, Uri fileUri) {
        context.revokeUriPermission(fileUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    public static boolean isGifFormat(Image image) {
        String extension = getExtension(image.getPath());
        return extension.equalsIgnoreCase("gif");
    }

    public static boolean isVideoFormat(Image image) {
        String extension = getExtension(image.getPath());
        String mimeType = TextUtils.isEmpty(extension)
                ? URLConnection.guessContentTypeFromName(image.getPath())
                : MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        return mimeType != null && mimeType.startsWith("video");

    }

    private static String getExtension(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        if (!TextUtils.isEmpty(extension)) {
            return extension;
        }
        if (path.contains(".")) {
            return path.substring(path.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    public static GradientDrawable getRedColor(final Context context) {
        return new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{ContextCompat.getColor(context, R.color.red_1),
                        ContextCompat.getColor(context, R.color.red_2),
                        ContextCompat.getColor(context, R.color.red_3),
                        ContextCompat.getColor(context, R.color.red_4)});
    }

    public static GradientDrawable getPurpleColor(final Context context) {
        return new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{ContextCompat.getColor(context, R.color.purple_1),
                        ContextCompat.getColor(context, R.color.purple_2),
                        ContextCompat.getColor(context, R.color.purple_3),
                        ContextCompat.getColor(context, R.color.purple_4)});
    }

    public static GradientDrawable getGreenColor(final Context context) {
        return new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{ContextCompat.getColor(context, R.color.green_1),
                        ContextCompat.getColor(context, R.color.green_2),
                        ContextCompat.getColor(context, R.color.green_3),
                        ContextCompat.getColor(context, R.color.green_4)});
    }

    public static GradientDrawable getYellowColor(final Context context) {
        return new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{ContextCompat.getColor(context, R.color.yellow_1),
                        ContextCompat.getColor(context, R.color.yellow_2),
                        ContextCompat.getColor(context, R.color.yellow_3),
                        ContextCompat.getColor(context, R.color.yellow_4)});
    }

    public static GradientDrawable getOrangeColor(final Context context) {
        return new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{ContextCompat.getColor(context, R.color.orange_1),
                        ContextCompat.getColor(context, R.color.orange_2),
                        ContextCompat.getColor(context, R.color.orange_3),
                        ContextCompat.getColor(context, R.color.orange_4)});
    }

    public static GradientDrawable getBrownColor(final Context context) {
        return new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{ContextCompat.getColor(context, R.color.brown_1),
                        ContextCompat.getColor(context, R.color.brown_2),
                        ContextCompat.getColor(context, R.color.brown_3),
                        ContextCompat.getColor(context, R.color.brown_4)});
    }

    public static GradientDrawable getGreyColor(final Context context) {
        return new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{ContextCompat.getColor(context, R.color.grey_1),
                        ContextCompat.getColor(context, R.color.grey_2),
                        ContextCompat.getColor(context, R.color.grey_3),
                        ContextCompat.getColor(context, R.color.grey_4)});
    }

    public static String createFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        return "IMG_" + timeStamp;
    }

    public static void hideKeyboard(final AppCompatActivity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();

        if (view == null)
            view = new View(activity);

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
