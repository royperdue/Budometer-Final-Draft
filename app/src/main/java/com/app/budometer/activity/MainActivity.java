package com.app.budometer.activity;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.budometer.R;
import com.app.budometer.features.ImagePicker;
import com.app.budometer.fragment.BaseFragment;
import com.app.budometer.fragment.CameraFragment;
import com.app.budometer.fragment.CropFragment;
import com.app.budometer.fragment.DetailsFragment;
import com.app.budometer.fragment.ImagePickerFragment;
import com.app.budometer.fragment.MainFragment;
import com.app.budometer.fragment.ResultFragment;
import com.app.budometer.fragment.ReviewFragment;
import com.app.budometer.helper.CombineBitmap;
import com.app.budometer.helper.LocaleManager;
import com.app.budometer.layout.DLayoutManager;
import com.app.budometer.listener.ImagePickerView;
import com.app.budometer.listener.OnProgressListener;
import com.app.budometer.model.Analysis;
import com.app.budometer.model.AnalysisDao;
import com.app.budometer.model.Counter;
import com.app.budometer.model.Folder;
import com.app.budometer.model.Image;
import com.app.budometer.model.ResultData;
import com.app.budometer.tensorflow.Classifier;
import com.app.budometer.tensorflow.TensorFlowImageClassifier;
import com.app.budometer.util.BudometerApp;
import com.app.budometer.util.BudometerConfig;
import com.app.budometer.util.BudometerSP;
import com.app.budometer.util.Palette;
import com.app.budometer.util.PaletteGraph;
import com.app.budometer.views.SnackBarView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class MainActivity extends AppCompatActivity implements MainFragment.OnMainFragmentInteractionListener,
        ResultFragment.OnResultFragmentInteractionListener, ReviewFragment.OnReviewFragmentInteractionListener,
        DetailsFragment.OnDetailsFragmentInteractionListener, ImagePickerFragment.OnImagePickerInteractionListener,
        ImagePickerView, ReviewFragment.OnReviewButtonClickListener, CropFragment.OnCropFragmentInteractionListener,
        CameraFragment.OnCameraFragmentInteractionListener {
    private Classifier classifier;
    private SpotsDialog progressDialog;
    private SnackBarView snackBarView;
    private Fragment fragment;
    private String title = null;
    private int totalPixelCount = 0;
    private Bitmap[] bitmaps = new Bitmap[4];
    private List<Float> percentTotals = new ArrayList<>();
    private List<Image> images = null;
    private List<Palette.Swatch> swatches = null;

    private View.OnClickListener hideSnackBarListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            snackBarView.hide();
        }
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleManager.updateResources(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        MobileAds.initialize(this, "ca-app-pub-2047793645426902~9568454094");

        snackBarView = findViewById(R.id.snackBarMainActivity);

        launchFragment(0);
    }

    public void showSnackBar(int message, int drawable) {
        snackBarView.setBackground(getDrawable(drawable));
        snackBarView.show(message, hideSnackBarListener);
    }

    private void launchFragment(int position) {
        if (position == 0) {
            title = "Home";
            fragment = MainFragment.newInstance();
        } else if (position == 1) {
            title = "Select Images";
            fragment = ImagePickerFragment.newInstance();
            ((ImagePickerFragment) fragment).setInteractionListener(this);
        } else if (position == 2) {
            title = "Result";
            fragment = ResultFragment.newInstance();
        } else if (position == 3) {
            title = "Review";
            fragment = ReviewFragment.newInstance();
        } else if (position == 4) {
            title = "Crop";
            fragment = CropFragment.newInstance();
        } else if (position == 5) {
            title = "Camera";
            fragment = CameraFragment.newInstance();
        }

        if (fragment != null) {
            if (fragment != null && fragment instanceof MainFragment) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                transaction.replace(R.id.fragment_container, fragment).disallowAddToBackStack().commitNow();
            } else if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(title).commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        tellFragments();
        super.onBackPressed();
    }

    private void tellFragments() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment instanceof BaseFragment)
                ((BaseFragment) fragment).onBackPressed();
        }
    }

    /* --------------------------------------------------- */
    /* > Fragment Interaction Listener Methods */
    /* --------------------------------------------------- */

    @Override
    public void onMainFragmentInteraction(int index) {
        if (index == 0) {
            launchFragment(1);
        } else if (index == 1) {
            launchFragment(3);
        } else if (index == 2) {
            startActivity(new Intent(MainActivity.this, IntroActivity.class));
            Animatoo.animateSplit(MainActivity.this);
        } else if (index == 3) {
            launchFragment(5);
        }
    }

    @Override
    public void onPickerFragmentInteraction(int index) {
        if (index == 0) {
            if (fragment != null && fragment instanceof ImagePickerFragment) {
                if (BudometerSP.init(MainActivity.this).getInt(R.string.easy_prefs_key_selected_images_count) == 4) {
                    ((ImagePickerFragment) fragment).onDone();
                } else
                    showSnackBar(R.string.select_four_images, R.drawable.warning_background);
            }
        } else if (index == 1) {
            launchFragment(3);
        } else if (index == 2) {
            startActivity(new Intent(MainActivity.this, IntroActivity.class));
            Animatoo.animateSplit(MainActivity.this);
        } else if (index == 3) {
            launchFragment(5);
        } else if (index == 4) {
            if (BudometerSP.init(MainActivity.this).getInt(R.string.easy_prefs_key_selected_images_count) >= 1) {
                launchFragment(4);
            } else
                showSnackBar(R.string.select_image_cropping, R.drawable.warning_background);
        }
    }

    @Override
    public void onResultFragmentInteraction() {

    }

    @Override
    public void onReviewFragmentInteraction(final long analysisId) {
        title = "Details";
        fragment = DetailsFragment.newInstance(analysisId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.fragment_container, fragment).addToBackStack(title)
                .commit();
    }

    @Override
    public void onDetailsFragmentInteraction() {
    }

    @Override
    public void onCropFragmentInteraction() {
        launchFragment(1);
    }

    @Override
    public void onReviewButtonClick(int index) {
        if (index == 0) {
            launchFragment(1);
        } else if (index == 1) {
            startActivity(new Intent(MainActivity.this, IntroActivity.class));
            Animatoo.animateSplit(MainActivity.this);
        } else if (index == 2) {
            launchFragment(5);
        }
    }

    @Override
    public void onCameraFragmentInteraction() {
    }

    /* --------------------------------------------------- */
    /* > ImagePickerInteractionListener Methods */
    /* --------------------------------------------------- */

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void finishPickImages(Intent data) {
        images = ImagePicker.getImages(data);

        progressDialog = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.CustomProgressDialog)
                .setMessage(R.string.analyzing).build();
        progressDialog.show();

        for (int i = 0; i < images.size(); i++) {
            if (i == 0) {
                BudometerSP.init(MainActivity.this).putString(BudometerConfig.IMAGE_PATH_1, images.get(i).getPath());
            } else if (i == 1) {
                BudometerSP.init(MainActivity.this).putString(BudometerConfig.IMAGE_PATH_2, images.get(i).getPath());
            } else if (i == 2) {
                BudometerSP.init(MainActivity.this).putString(BudometerConfig.IMAGE_PATH_3, images.get(i).getPath());
            } else if (i == 3) {
                BudometerSP.init(MainActivity.this).putString(BudometerConfig.IMAGE_PATH_4, images.get(i).getPath());
            }

            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(images.get(i).getPath())));
                bitmaps[i] = bitmap;
                segmentImageColors(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void segmentImageColors(Bitmap bitmap) {
        Palette.from(bitmap).maximumColorCount(20).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                Counter counter = new Counter();
                PaletteGraph paletteGraph = new PaletteGraph();
                swatches = p.getSwatches();

                Map<String, List<Palette.Swatch>> adjacencyList = paletteGraph.getAdjacencyList();

                for (int i = 0; i < swatches.size(); i++) {
                    if (swatches.get(i).getColorName() != null)
                        paletteGraph.addSwatchData(swatches.get(i), swatches.get(i).getColorName());
                }

                Iterator iterator = adjacencyList.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry elementData = (Map.Entry) iterator.next();
                    List<Palette.Swatch> swatchList = (List<Palette.Swatch>) elementData.getValue();

                    for (int i = 0; i < swatchList.size(); i++) {
                        Palette.Swatch swatch = swatchList.get(i);

                        if (swatch.getColorName().equals("yellowLight")) {
                            counter.setLightYellowPixelCount(counter.getLightYellowPixelCount() + swatch.getPopulation());
                        } else if (swatch.getColorName().equals("yellowMedium")) {
                            counter.setMediumYellowPixelCount(counter.getMediumYellowPixelCount() + swatch.getPopulation());
                        } else if (swatch.getColorName().equals("yellow")) {
                            counter.setYellowPixelCount(counter.getYellowPixelCount() + swatch.getPopulation());
                        } else if (swatch.getColorName().equals("yellowDark")) {
                            counter.setDarkYellowPixelCount(counter.getDarkYellowPixelCount() + swatch.getPopulation());
                        } else if (swatch.getColorName().equals("orangeLight")) {
                            counter.setLightOrangePixelCount(counter.getLightOrangePixelCount() + swatch.getPopulation());
                        } else if (swatch.getColorName().equals("orangeMedium")) {
                            counter.setMediumOrangePixelCount(counter.getMediumOrangePixelCount() + swatch.getPopulation());
                        } else if (swatch.getColorName().equals("orange")) {
                            counter.setOrangePixelCount(counter.getOrangePixelCount() + swatch.getPopulation());
                        } else if (swatch.getColorName().equals("orangeDark")) {
                            counter.setDarkOrangePixelCount(counter.getDarkOrangePixelCount() + swatch.getPopulation());
                        }

                        totalPixelCount += swatch.getPopulation();
                    }
                }

                percentTotals.add(calculatePercentTotal(counter));

                if (percentTotals.size() == 4) {
                    if (percentTotals.get(0) < 100.0 && percentTotals.get(1) < 100.0 && percentTotals.get(2) < 100.0 && percentTotals.get(3) < 100.0) {
                        percentTotals.clear();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long analysisId = createAnalysis();

                                BudometerSP.init(MainActivity.this).putLong(BudometerConfig.GREEN_DAO_ANALYSIS_ID, analysisId);

                                Analysis analysis = getAnalysis(analysisId);

                                analysis.setImagePath1(BudometerSP.init(MainActivity.this).getString(BudometerConfig.IMAGE_PATH_1));
                                analysis.setImagePath2(BudometerSP.init(MainActivity.this).getString(BudometerConfig.IMAGE_PATH_2));
                                analysis.setImagePath3(BudometerSP.init(MainActivity.this).getString(BudometerConfig.IMAGE_PATH_3));
                                analysis.setImagePath4(BudometerSP.init(MainActivity.this).getString(BudometerConfig.IMAGE_PATH_4));

                                BudometerApp.getDaoSession().getAnalysisDao().update(analysis);

                                loadBitmap(bitmaps);
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        percentTotals.clear();
                        displayDialog();
                    }
                }
            }
        });

    }

    private void clearPixelCounters() {
        totalPixelCount = 0;
    }

    private void displayDialog() {
        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.CustomAlertDialogTheme))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recreate();
                    }
                }).setMessage(R.string.message_error_bad_lighting)
                .create().show();
    }

    private float calculatePercentTotal(Counter counter) {
        int totalPixelsTurned = counter.getLightOrangePixelCount() + counter.getMediumOrangePixelCount() + counter.getOrangePixelCount() + counter.getDarkOrangePixelCount()
                + counter.getLightYellowPixelCount() + counter.getMediumYellowPixelCount() + counter.getYellowPixelCount() + counter.getDarkYellowPixelCount();

        float percentTotal = (totalPixelsTurned * 100.0f) / totalPixelCount;
        clearPixelCounters();
        //System.out.println("PERCENT-TOTAL: " + percentTotal);

        return percentTotal;
    }

    private void loadBitmap(Bitmap[] bitmaps) {
        CombineBitmap.init(this)
                .setLayoutManager(new DLayoutManager())
                .setSize(112)
                .setGap(0)
                .setBitmaps(bitmaps)
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(Bitmap bitmap) {
                        new BudColorsAsyncTask().execute(bitmap);
                    }
                })
                .build();
    }

    private long createAnalysis() {
        Analysis analysis = new Analysis();
        analysis.setDate(System.currentTimeMillis());

        BudometerApp.getDaoSession().getAnalysisDao().insert(analysis);

        return analysis.getAnalysisId();
    }

    private Analysis getAnalysis(long analysisId) {
        return BudometerApp.getDaoSession().getAnalysisDao().queryBuilder()
                .where(AnalysisDao.Properties.AnalysisId.eq(analysisId))
                .list().get(0);
    }

    private class BudColorsAsyncTask extends AsyncTask<Bitmap, Void, ResultData> {
        protected void onPreExecute() {
            super.onPreExecute();

            classifier = TensorFlowImageClassifier.create(getAssets(),
                    BudometerConfig.MODEL_FILE_BUD_COLORS,
                    BudometerConfig.LABEL_FILE_BUD_COLORS,
                    BudometerConfig.INPUT_SIZE,
                    BudometerConfig.IMAGE_MEAN,
                    BudometerConfig.IMAGE_STD,
                    BudometerConfig.INPUT_NAME,
                    BudometerConfig.OUTPUT_NAME);
        }

        @Override
        protected ResultData doInBackground(Bitmap... params) {
            ResultData resultData = null;
            final List<Classifier.Recognition> results = classifier.recognizeImage(params[0]);

            if (results != null) {
                resultData = new ResultData(params[0]);
                for (int i = 0; i < results.size(); i++) {
                    if (results.get(i).getTitle().contains("orange")) {
                        resultData.setTensorFlowConfidenceOrange(results.get(i).getConfidence());
                    } else if (results.get(i).getTitle().contains("purple")) {
                        resultData.setTensorFlowConfidencePurple(results.get(i).getConfidence());
                    } else if (results.get(i).getTitle().contains("white")) {
                        resultData.setTensorFlowConfidenceWhite(results.get(i).getConfidence());
                    }

                    System.out.println("TITLE: " + results.get(i).getTitle() + ":CONFIDENCE SCORE: " + results.get(i).getConfidence());
                }
            }
            return resultData;
        }

        @Override
        protected void onPostExecute(ResultData resultData) {
            super.onPostExecute(resultData);

            Analysis analysis = getAnalysis(BudometerSP.init(MainActivity.this).getLong(BudometerConfig.GREEN_DAO_ANALYSIS_ID));
            analysis.setTensorFlowConfidenceOrange(resultData.getTensorFlowConfidenceOrange());
            analysis.setTensorFlowConfidencePurple(resultData.getTensorFlowConfidencePurple());
            analysis.setTensorFlowConfidenceWhite(resultData.getTensorFlowConfidenceWhite());

            BudometerApp.getDaoSession().getAnalysisDao().update(analysis);

            new GrowingReadyAsyncTask().execute(resultData.getBitmap());
        }
    }


    private class GrowingReadyAsyncTask extends AsyncTask<Bitmap, Void, ResultData> {
        protected void onPreExecute() {
            super.onPreExecute();

            classifier = TensorFlowImageClassifier.create(getAssets(),
                    BudometerConfig.MODEL_FILE_GROWING_READY,
                    BudometerConfig.LABEL_FILE_GROWING_READY,
                    BudometerConfig.INPUT_SIZE,
                    BudometerConfig.IMAGE_MEAN,
                    BudometerConfig.IMAGE_STD,
                    BudometerConfig.INPUT_NAME,
                    BudometerConfig.OUTPUT_NAME);
        }

        @Override
        protected ResultData doInBackground(Bitmap... params) {
            ResultData resultData = null;
            final List<Classifier.Recognition> results = classifier.recognizeImage(params[0]);

            if (results != null) {
                resultData = new ResultData(params[0]);
                for (int i = 0; i < results.size(); i++) {
                    if (results.get(i).getTitle().equals("growing")) {
                        resultData.setTensorFlowConfidenceGrowing(results.get(i).getConfidence());
                    } else if (results.get(i).getTitle().equals("ready")) {
                        resultData.setTensorFlowConfidenceReady(results.get(i).getConfidence());
                    }

                    System.out.println("TITLE: " + results.get(i).getTitle() + ":CONFIDENCE SCORE: " + results.get(i).getConfidence());
                }
            }
            return resultData;
        }

        @Override
        protected void onPostExecute(ResultData resultData) {
            super.onPostExecute(resultData);

            Analysis analysis = getAnalysis(BudometerSP.init(MainActivity.this).getLong(BudometerConfig.GREEN_DAO_ANALYSIS_ID));
            analysis.setTensorFlowConfidenceGrowing(resultData.getTensorFlowConfidenceGrowing());
            analysis.setTensorFlowConfidenceReady(resultData.getTensorFlowConfidenceReady());

            BudometerApp.getDaoSession().getAnalysisDao().update(analysis);

            progressDialog.dismiss();
            launchFragment(2);
        }
    }


    @Override
    public void selectionChanged(List<Image> imageList) {
    }

    /* --------------------------------------------------- */
    /* > View Methods  */
    /* --------------------------------------------------- */

    @Override
    public void showLoading(boolean isLoading) {
        if (fragment != null && fragment instanceof ImagePickerFragment)
            ((ImagePickerFragment) fragment).showLoading(isLoading);
    }

    @Override
    public void showFetchCompleted(List<Image> images, List<Folder> folders) {
        if (fragment != null && fragment instanceof ImagePickerFragment)
            ((ImagePickerFragment) fragment).showFetchCompleted(images, folders);
    }

    @Override
    public void showError(Throwable throwable) {
        if (fragment != null && fragment instanceof ImagePickerFragment)
            ((ImagePickerFragment) fragment).showError(throwable);
    }

    @Override
    public void showEmpty() {
        if (fragment != null && fragment instanceof ImagePickerFragment)
            ((ImagePickerFragment) fragment).showEmpty();
    }

    @Override
    public void showCapturedImage() {
        if (fragment != null && fragment instanceof ImagePickerFragment)
            ((ImagePickerFragment) fragment).showCapturedImage();
    }

    @Override
    public void finishPickImages(List<Image> images) {
        if (fragment != null && fragment instanceof ImagePickerFragment)
            ((ImagePickerFragment) fragment).finishPickImages(images);
    }

    public SnackBarView getSnackBarView() {
        return snackBarView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BudometerSP.init(this).putInt(BudometerConfig.CAMERA_FLASH_SETTING, -1);
    }
}
