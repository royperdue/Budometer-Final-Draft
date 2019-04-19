package com.app.budometer.helper;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import com.app.budometer.cache.DiskLruCacheHelper;
import com.app.budometer.cache.LruCacheHelper;
import com.app.budometer.util.BudometerUtils;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BitmapLoader {
    private static String TAG = BitmapLoader.class.getSimpleName();
    private static final int BUFFER_SIZE = 8 * 1024;
    private LruCacheHelper lruCacheHelper;
    private DiskLruCache mDiskLruCache;
    private CompressHelper compressHelper;
    private volatile static BitmapLoader manager;

    public static BitmapLoader getInstance(Context context) {
        if (manager == null) {
            synchronized (BitmapLoader.class) {
                if (manager == null) {
                    manager = new BitmapLoader(context);
                }
            }
        }
        return manager;
    }

    private Map<String, Runnable> doingTasks;
    private Map<String, List<Runnable>> undoTasks;


    private BitmapLoader(Context context) {
        mDiskLruCache = new DiskLruCacheHelper(context).mDiskLruCache;
        lruCacheHelper = new LruCacheHelper();
        compressHelper = CompressHelper.getInstance();

        doingTasks = new HashMap<>();
        undoTasks = new HashMap<>();
    }


    public void asyncLoad(final int index, final String url, final int reqWidth, final int reqHeight, final Handler handler) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(url, reqWidth, reqHeight);
                if (bitmap != null) {
                    handler.obtainMessage(1, index, -1, bitmap).sendToTarget();
                } else {
                    handler.obtainMessage(2, index, -1, null).sendToTarget();
                }
            }
        };

        if (collectUndoTasks(url, task)) {
            return;
        }

        ThreadPool.getInstance().execute(task);
    }

    private Bitmap loadBitmap(String url, int reqWidth, int reqHeight) {
        String key = BudometerUtils.hashKeyFormUrl(url);
        Bitmap bitmap = lruCacheHelper.getBitmapFromMemCache(key);
        if (bitmap != null) {
            Log.e(TAG, "load from memory:" + url);
            return bitmap;
        }

        try {
            bitmap = loadBitmapFromDiskCache(url, reqWidth, reqHeight);
            if (bitmap != null) {
                Log.e(TAG, "load from disk:" + url);
                return bitmap;
            }
            bitmap = loadBitmapFromHttp(url, reqWidth, reqHeight);
            if (bitmap != null) {
                Log.e(TAG, "load from http:" + url);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Bitmap loadBitmapFromHttp(String url, int reqWidth, int reqHeight)
            throws IOException {
        String key = BudometerUtils.hashKeyFormUrl(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(0);
            if (downloadUrlToStream(url, outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskLruCache.flush();

            executeUndoTasks(url);
        }
        return loadBitmapFromDiskCache(url, reqWidth, reqHeight);
    }

    private boolean downloadUrlToStream(
            String urlString,
            OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "downloadBitmap failed." + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            BudometerUtils.close(out);
            BudometerUtils.close(in);
        }
        return false;
    }

    private Bitmap loadBitmapFromDiskCache(
            String url, int reqWidth,
            int reqHeight) throws IOException {
        Bitmap bitmap = null;
        String key = BudometerUtils.hashKeyFormUrl(url);
        DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
        if (snapShot != null) {
            FileInputStream fileInputStream = (FileInputStream) snapShot.getInputStream(0);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = compressHelper.compressDescriptor(fileDescriptor, reqWidth, reqHeight);
            if (bitmap != null) {
                lruCacheHelper.addBitmapToMemoryCache(key, bitmap);
            }
        }

        return bitmap;
    }

    private boolean collectUndoTasks(String url, Runnable task) {
        String key = BudometerUtils.hashKeyFormUrl(url);

        if (lruCacheHelper.getBitmapFromMemCache(key) != null) {
            return false;
        }

        DiskLruCache.Snapshot snapShot = null;
        try {
            snapShot = mDiskLruCache.get(key);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (snapShot != null) {
            return false;
        }

        if (doingTasks.containsKey(key)) {
            if (undoTasks.containsKey(key)) {
                List<Runnable> tasks = undoTasks.get(key);
                tasks.add(task);
                undoTasks.put(key, tasks);
            } else {
                List<Runnable> tasks = new ArrayList<>();
                tasks.add(task);
                undoTasks.put(key, tasks);
            }
            return true;
        }

        doingTasks.put(key, task);
        return false;
    }

    private void executeUndoTasks(String url) {
        String key = BudometerUtils.hashKeyFormUrl(url);

        if (undoTasks.containsKey(key)) {
            for (Runnable task : undoTasks.get(key)) {
                ThreadPool.getInstance().execute(task);
            }
            undoTasks.remove(key);
        }

        doingTasks.remove(key);
    }
}
