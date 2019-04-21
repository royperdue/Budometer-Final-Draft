package com.app.budometer.fragment;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.budometer.R;
import com.app.budometer.activity.MainActivity;
import com.app.budometer.helper.CombineBitmap;
import com.app.budometer.layout.DLayoutManager;
import com.app.budometer.listener.OnProgressListener;
import com.app.budometer.model.Analysis;
import com.app.budometer.model.AnalysisDao;
import com.app.budometer.model.ResultData;
import com.app.budometer.util.AnalysisPresenter;
import com.app.budometer.util.BudometerApp;
import com.app.budometer.util.BudometerConfig;
import com.app.budometer.util.BudometerSP;
import com.app.budometer.util.BudometerUtils;
import com.app.budometer.util.Palette;
import com.app.budometer.util.PaletteGraph;
import com.app.budometer.views.PieChartView;
import com.otaliastudios.autocomplete.Autocomplete;
import com.otaliastudios.autocomplete.AutocompleteCallback;
import com.otaliastudios.autocomplete.AutocompletePresenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.constraintlayout.widget.ConstraintLayout;
import dmax.dialog.SpotsDialog;


public class ResultFragment extends BaseFragment {
    private OnResultFragmentInteractionListener mListener;
    private static ResultFragment fragment = null;
    private String tensorFlowTitleGrowing = "G";
    private float tensorFlowConfidenceGrowing = 0.0f;
    private String tensorFlowTitleReady = "R";
    private float tensorFlowConfidenceReady = 0.0f;

    private int lightRedPixelCount = 0;
    private int lightPurplePixelCount = 0;
    private int lightGreenPixelCount = 0;
    private int lightOrangePixelCount = 0;
    private int lightGreyPixelCount = 0;

    private int mediumRedPixelCount = 0;
    private int mediumPurplePixelCount = 0;
    private int mediumGreenPixelCount = 0;
    private int mediumOrangePixelCount = 0;
    private int mediumGreyPixelCount = 0;

    private int redPixelCount = 0;
    private int purplePixelCount = 0;
    private int greenPixelCount = 0;
    private int orangePixelCount = 0;
    private int greyPixelCount = 0;

    private int darkRedPixelCount = 0;
    private int darkPurplePixelCount = 0;
    private int darkGreenPixelCount = 0;
    private int darkOrangePixelCount = 0;
    private int darkGreyPixelCount = 0;

    private int lightRed = 0;
    private int lightPurple = 0;
    private int lightGreen = 0;
    private int lightOrange = 0;
    private int lightGrey = 0;

    private int mediumRed = 0;
    private int mediumPurple = 0;
    private int mediumGreen = 0;
    private int mediumOrange = 0;
    private int mediumGrey = 0;

    private int red = 0;
    private int purple = 0;
    private int green = 0;
    private int orange = 0;
    private int grey = 0;

    private int darkRed = 0;
    private int darkPurple = 0;
    private int darkGreen = 0;
    private int darkOrange = 0;
    private int darkGrey = 0;

    private ConstraintLayout relativeLayout;
    private SpotsDialog progressDialog;
    private PieChartView pieChartView;
    private EditText resultEditText;
    private Autocomplete analysisAutocomplete;
    private EditText strainEditText;
    private EditText cropIdEditText;
    private EditText dateEditText;
    private EditText notesEditText;
    private EditText turnedEditText;
    private ImageView resultImageView;
    private TextView redTextView;
    private TextView purpleTextView;
    private TextView greenTextView;
    private TextView orangeTextView;
    private TextView greyTextView;
    private LinearLayout saveButton;
    private List<Palette.Swatch> swatches = null;
    private List<Color> colors = new ArrayList<>();

    public interface OnResultFragmentInteractionListener {
        void onResultFragmentInteraction();
    }

    public ResultFragment() {
    }

    public static ResultFragment newInstance() {
        if (fragment == null)
            fragment = new ResultFragment();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnResultFragmentInteractionListener) {
            mListener = (OnResultFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnResultFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = (SpotsDialog) new SpotsDialog.Builder()
                .setContext(getActivity())
                .setTheme(R.style.CustomProgressDialog)
                .setMessage(R.string.analyzing).build();
        progressDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        relativeLayout = view.findViewById(R.id.mainLayout);

        pieChartView = view.findViewById(R.id.pieChartView);
        pieChartView.setInnerRadius(0.4f);
        pieChartView.setBackGroundColor(0xffFFFFE0);
        pieChartView.setItemTextSize(20);
        pieChartView.setTextPadding(10);

        turnedEditText = view.findViewById(R.id.turnedEditText);
        resultImageView = view.findViewById(R.id.resultImageView);
        resultEditText = view.findViewById(R.id.resultEditText);
        strainEditText = view.findViewById(R.id.strainEditText);

        cropIdEditText = view.findViewById(R.id.cropIdEditText);
        cropIdEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    analysisAutocomplete.showPopup(cropIdEditText.getText().toString());
                else
                    analysisAutocomplete.dismissPopup();
            }
        });

        setupCropIdAutocomplete();

        dateEditText = view.findViewById(R.id.dateEditText);
        notesEditText = view.findViewById(R.id.notesEditText);

        saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BudometerUtils.hideKeyboard(((MainActivity) getActivity()));
                saveResults();
            }
        });

        LinearLayout cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BudometerUtils.hideKeyboard(((MainActivity) getActivity()));
                ((MainActivity) getActivity()).recreate();
            }
        });

        redTextView = view.findViewById(R.id.redTextView);
        purpleTextView = view.findViewById(R.id.purpleTextView);
        greenTextView = view.findViewById(R.id.greenTextView);
        orangeTextView = view.findViewById(R.id.orangeTextView);
        greyTextView = view.findViewById(R.id.greyTextView);

        return view;
    }

    private void setupCropIdAutocomplete() {
        float elevation = 6f;
        Drawable backgroundDrawable = new ColorDrawable(Color.WHITE);
        AutocompletePresenter<Analysis> presenter = new AnalysisPresenter(getActivity());
        AutocompleteCallback<Analysis> callback = new AutocompleteCallback<Analysis>() {
            @Override
            public boolean onPopupItemClicked(Editable editable, Analysis item) {
                editable.clear();
                editable.append(item.getCropId());
                strainEditText.setText(item.getStrain());
                notesEditText.setFocusableInTouchMode(true);
                notesEditText.requestFocus();
                return true;
            }

            public void onPopupVisibilityChanged(boolean shown) {
            }
        };

        analysisAutocomplete = Autocomplete.<Analysis>on(cropIdEditText)
                .with(elevation)
                .with(backgroundDrawable)
                .with(presenter)
                .with(callback)
                .build();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Analysis analysis = getAnalysis(BudometerSP.init(getActivity()).getLong(BudometerConfig.GREEN_DAO_ANALYSIS_ID));
        String[] paths = new String[4];
        paths[0] = analysis.getImagePath1();
        paths[1] = analysis.getImagePath2();
        paths[2] = analysis.getImagePath3();
        paths[3] = analysis.getImagePath4();

        evaluateTensorFlowResults(analysis.getTensorFlowConfidenceGrowing(), analysis.getTensorFlowConfidenceReady());

        loadBitmap(getBitmaps(paths));
    }

    private Bitmap[] getBitmaps(String[] paths) {
        Bitmap[] bitmaps = new Bitmap[paths.length];
        try {
            for (int i = 0; i < paths.length; i++) {
                File file = new File(paths[i]);
                bitmaps[i] = BitmapFactory.decodeStream(new FileInputStream(file));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmaps;
    }

    private void loadBitmap(Bitmap[] bitmaps) {
        CombineBitmap.init(getActivity())
                .setLayoutManager(new DLayoutManager())
                .setSize(224)
                .setGap(0)
                .setBitmaps(bitmaps)
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(Bitmap bitmap) {
                        resultImageView.setImageBitmap(bitmap);
                        segmentImageColors(bitmap);
                    }
                })
                .build();
    }

    @Override
    public void onPause() {
        super.onPause();

        Analysis analysis = getAnalysis(BudometerSP.init(getActivity()).getLong(BudometerConfig.GREEN_DAO_ANALYSIS_ID));

        if (analysis != null)
            if (!analysis.getSaved())
                BudometerApp.getDaoSession().getAnalysisDao().deleteByKey(BudometerSP.init(getActivity()).getLong(BudometerConfig.GREEN_DAO_ANALYSIS_ID));
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onResultFragmentInteraction();
        mListener = null;
        colors.clear();
    }

    private void segmentImageColors(Bitmap bitmap) {
        dateEditText.setText(new SimpleDateFormat("EEEE, MMMM d, yyyy").format(new Date(System.currentTimeMillis())));
        Palette.from(bitmap).maximumColorCount(300).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                PaletteGraph paletteGraph = new PaletteGraph();
                swatches = getSwatchesFromPalette(p);
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
                        int color = swatchList.get(i).getRgb();
                        Palette.Swatch swatch = swatchList.get(i);

                        if (swatch.getColorName().equals("redLight")) {
                            lightRedPixelCount += swatch.getPopulation();
                            lightRed = color;
                        } else if (swatch.getColorName().equals("redMedium")) {
                            mediumRedPixelCount += swatch.getPopulation();
                            mediumRed = color;
                        } else if (swatch.getColorName().equals("red")) {
                            redPixelCount += swatch.getPopulation();
                            red = color;
                        } else if (swatch.getColorName().equals("redDark")) {
                            darkRedPixelCount += swatch.getPopulation();
                            darkRed = color;
                        } else if (swatch.getColorName().equals("purpleLight")) {
                            lightPurplePixelCount += swatch.getPopulation();
                            lightPurple = color;
                        } else if (swatch.getColorName().equals("purpleMedium")) {
                            mediumPurplePixelCount += swatch.getPopulation();
                            mediumPurple = color;
                        } else if (swatch.getColorName().equals("purple")) {
                            purplePixelCount += swatch.getPopulation();
                            purple = color;
                        } else if (swatch.getColorName().equals("purpleDark")) {
                            darkPurplePixelCount += swatch.getPopulation();
                            darkPurple = color;
                        } else if (swatch.getColorName().equals("greenLight")) {
                            lightGreenPixelCount += swatch.getPopulation();
                            lightGreen = color;
                        } else if (swatch.getColorName().equals("greenMedium")) {
                            mediumGreenPixelCount += swatch.getPopulation();
                            mediumGreen = color;
                        } else if (swatch.getColorName().equals("green")) {
                            greenPixelCount += swatch.getPopulation();
                            green = color;
                        } else if (swatch.getColorName().equals("greenDark")) {
                            darkGreenPixelCount += swatch.getPopulation();
                            darkGreen = color;
                        } else if (swatch.getColorName().equals("orangeLight")) {
                            lightOrangePixelCount += swatch.getPopulation();
                            lightOrange = color;
                        } else if (swatch.getColorName().equals("orangeMedium")) {
                            mediumOrangePixelCount += swatch.getPopulation();
                            mediumOrange = color;
                        } else if (swatch.getColorName().equals("orange")) {
                            orangePixelCount += swatch.getPopulation();
                            orange = color;
                        } else if (swatch.getColorName().equals("orangeDark")) {
                            darkOrangePixelCount += swatch.getPopulation();
                            darkOrange = color;
                        } else if (swatch.getColorName().equals("greyLight")) {
                            lightGreyPixelCount += swatch.getPopulation();
                            lightGrey = color;
                        } else if (swatch.getColorName().equals("greyMedium")) {
                            mediumGreyPixelCount += swatch.getPopulation();
                            mediumGrey = color;
                        } else if (swatch.getColorName().equals("grey")) {
                            greyPixelCount += swatch.getPopulation();
                            grey = color;
                        } else if (swatch.getColorName().equals("greyDark")) {
                            darkGreyPixelCount += swatch.getPopulation();
                            darkGrey = color;
                        }
                    }
                }

                updateAnalysis(lightRedPixelCount, mediumRedPixelCount, redPixelCount, darkRedPixelCount,
                        lightPurplePixelCount, mediumPurplePixelCount, purplePixelCount, darkPurplePixelCount,
                        lightGreenPixelCount, mediumGreenPixelCount, greenPixelCount, darkGreenPixelCount,
                        lightOrangePixelCount, mediumOrangePixelCount, orangePixelCount, darkOrangePixelCount,
                        lightGreyPixelCount, mediumGreyPixelCount, greyPixelCount, darkGreyPixelCount,
                        lightRed, mediumRed, red, darkRed,
                        lightPurple, mediumPurple, purple, darkPurple,
                        lightGreen, mediumGreen, green, darkGreen,
                        lightOrange, mediumOrange, orange, darkOrange,
                        lightGrey, mediumGrey, grey, darkGrey);

                clearPixelCounters();
                adjacencyList.clear();
                adjacencyList = null;
                populateChart();
                turnedEditText.setText("Percent pistil curling: " + calculatePixelsTurned());
                progressDialog.dismiss();
            }
        });
    }

    private void clearPixelCounters() {
        lightRedPixelCount = 0;
        mediumRedPixelCount = 0;
        redPixelCount = 0;
        darkRedPixelCount = 0;
        lightPurplePixelCount = 0;
        mediumPurplePixelCount = 0;
        purplePixelCount = 0;
        darkPurplePixelCount = 0;
        lightGreenPixelCount = 0;
        mediumGreenPixelCount = 0;
        greenPixelCount = 0;
        darkGreenPixelCount = 0;
        lightOrangePixelCount = 0;
        mediumOrangePixelCount = 0;
        orangePixelCount = 0;
        darkOrangePixelCount = 0;
        lightGreyPixelCount = 0;
        mediumGreyPixelCount = 0;
        greyPixelCount = 0;
        darkGreyPixelCount = 0;
        lightRed = 0;
        mediumRed = 0;
        red = 0;
        darkRed = 0;
        lightPurple = 0;
        mediumPurple = 0;
        purple = 0;
        darkPurple = 0;
        lightGreen = 0;
        mediumGreen = 0;
        green = 0;
        darkGreen = 0;
        lightOrange = 0;
        mediumOrange = 0;
        orange = 0;
        darkOrange = 0;
        lightGrey = 0;
        mediumGrey = 0;
        grey = 0;
        darkGrey = 0;
    }

    private void evaluateTensorFlowResults(float tensorFlowConfidenceGrowing, float tensorFlowConfidenceReady) {
        resultEditText.setText("Test text.");
        if (tensorFlowConfidenceGrowing > tensorFlowConfidenceReady)
            resultEditText.setText(R.string.result_message_growing);
        else
            resultEditText.setText(R.string.result_message_ready);
    }

    private void populateChart() {
        Analysis analysis = getAnalysis(BudometerSP.init(getActivity()).getLong(BudometerConfig.GREEN_DAO_ANALYSIS_ID));

        if (analysis.getLightRedPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getLightRedPixelCount(), analysis.getLightRed()));
        if (analysis.getMediumRedPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getMediumRedPixelCount(), analysis.getMediumRed()));
        if (analysis.getRedPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getRedPixelCount(), analysis.getRed()));
        if (analysis.getDarkRedPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getDarkRedPixelCount(), analysis.getDarkRed()));
        if (analysis.getLightPurplePixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getLightPurplePixelCount(), analysis.getLightPurple()));
        if (analysis.getMediumPurplePixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getMediumPurplePixelCount(), analysis.getMediumPurple()));
        if (analysis.getPurplePixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getPurplePixelCount(), analysis.getPurple()));
        if (analysis.getDarkPurplePixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getDarkPurplePixelCount(), analysis.getDarkPurple()));
        if (analysis.getLightGreenPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getLightGreenPixelCount(), analysis.getLightGreen()));
        if (analysis.getMediumGreenPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getMediumGreenPixelCount(), analysis.getMediumGreen()));
        if (analysis.getGreenPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getGreenPixelCount(), analysis.getGreen()));
        if (analysis.getDarkGreenPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getDarkGreenPixelCount(), analysis.getDarkGreen()));
        if (analysis.getLightOrangePixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getLightOrangePixelCount(), analysis.getLightOrange()));
        if (analysis.getMediumOrangePixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getMediumOrangePixelCount(), analysis.getMediumOrange()));
        if (analysis.getOrangePixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getOrangePixelCount(), analysis.getOrange()));
        if (analysis.getDarkOrangePixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getDarkOrangePixelCount(), analysis.getDarkOrange()));
        if (analysis.getLightGreyPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getLightGreyPixelCount(), analysis.getLightGrey()));
        if (analysis.getMediumGreyPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getMediumGreyPixelCount(), analysis.getMediumGrey()));
        if (analysis.getGreyPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getGreyPixelCount(), analysis.getGrey()));
        if (analysis.getDarkGreyPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getDarkGreyPixelCount(), analysis.getDarkGrey()));

        int redPixelTotal = analysis.getLightRedPixelCount() + analysis.getMediumRedPixelCount() + analysis.getRedPixelCount() + analysis.getDarkRedPixelCount();
        redTextView.setText(toPercentage((redPixelTotal * 100.0f) / analysis.getTotalPixelCount()));
        redTextView.setBackground(BudometerUtils.getRedColor(getActivity()));

        int purplePixelTotal = analysis.getLightPurplePixelCount() + analysis.getMediumPurplePixelCount() + analysis.getPurplePixelCount() + analysis.getDarkPurplePixelCount();
        purpleTextView.setText(toPercentage((purplePixelTotal * 100.0f) / analysis.getTotalPixelCount()));
        purpleTextView.setBackground(BudometerUtils.getPurpleColor(getActivity()));

        int greenPixelTotal = analysis.getLightGreenPixelCount() + analysis.getMediumGreenPixelCount() + analysis.getGreenPixelCount() + analysis.getDarkGreenPixelCount();
        greenTextView.setText(toPercentage((greenPixelTotal * 100.0f) / analysis.getTotalPixelCount()));
        greenTextView.setBackground(BudometerUtils.getGreenColor(getActivity()));

        int orangePixelTotal = analysis.getLightOrangePixelCount() + analysis.getMediumOrangePixelCount() + analysis.getOrangePixelCount() + analysis.getDarkOrangePixelCount();
        orangeTextView.setText(toPercentage((orangePixelTotal * 100.0f) / analysis.getTotalPixelCount()));
        orangeTextView.setBackground(BudometerUtils.getOrangeColor(getActivity()));

        int greyPixelTotal = analysis.getLightGreyPixelCount() + analysis.getMediumGreyPixelCount() + analysis.getGreyPixelCount() + analysis.getDarkGreyPixelCount();
        greyTextView.setText(toPercentage((greyPixelTotal * 100.0f) / analysis.getTotalPixelCount()));
        greyTextView.setBackground(BudometerUtils.getGreyColor(getActivity()));

        pieChartView.invalidate();
    }

    private String toPercentage(float n) {
        return String.format("%." + 1 + "f", n) + "%";
    }

    private List<Palette.Swatch> getSwatchesFromPalette(Palette palette) {
        if (palette != null) {
            return palette.getSwatches();
        } else {
            return null;
        }
    }

    private String calculatePixelsTurned() {
        Analysis analysis = getAnalysis(BudometerSP.init(getActivity()).getLong(BudometerConfig.GREEN_DAO_ANALYSIS_ID));

        int totalPixelsNotTurned =
                analysis.getLightRedPixelCount() +
                        analysis.getMediumRedPixelCount() +
                        analysis.getRedPixelCount() +
                        analysis.getDarkRedPixelCount() +
                        analysis.getLightPurplePixelCount() +
                        analysis.getMediumPurplePixelCount() +
                        analysis.getPurplePixelCount() +
                        analysis.getDarkPurplePixelCount() +
                        analysis.getMediumGreyPixelCount() +
                        analysis.getGreyPixelCount() +
                        analysis.getDarkGreyPixelCount();

        int totalPixelsTurned =
                analysis.getLightOrangePixelCount() +
                        analysis.getMediumOrangePixelCount() +
                        analysis.getOrangePixelCount() +
                        analysis.getDarkOrangePixelCount();

        String percentageTurned = toPercentage((totalPixelsTurned * 100.0f) / (totalPixelsTurned + totalPixelsNotTurned));

        analysis.setPercentageTurnedPistils(percentageTurned);
        BudometerApp.getDaoSession().getAnalysisDao().update(analysis);

        return percentageTurned;
    }

    public void saveResults() {
        if (cropIdEditText.getText().toString().length() == 0) {
            new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.CustomAlertDialogTheme))
                    .setMessage(R.string.crop_id_required)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create().show();


        } else
            saveData();
    }

    private void saveData() {
        Analysis analysis = getAnalysis(BudometerSP.init(getActivity()).getLong(BudometerConfig.GREEN_DAO_ANALYSIS_ID));

        if (resultEditText.getText().toString().length() > 0)
            analysis.setTensorFlowResult(resultEditText.getText().toString());

        if (cropIdEditText.getText().toString().length() > 0)
            analysis.setCropId(cropIdEditText.getText().toString());

        if (strainEditText.getText().toString().length() > 0)
            analysis.setStrain(strainEditText.getText().toString());
        else analysis.setStrain("Not Specified");

        if (notesEditText.getText().toString().length() > 0)
            analysis.setNotes(notesEditText.getText().toString());

        Bitmap bitmap = ((BitmapDrawable) resultImageView.getDrawable()).getBitmap();
        String imageName = BudometerUtils.createFileName();

        String path = saveImageInternalStorage(bitmap, imageName);

        if (path != null && imageName != null) {
            analysis.setCombinedImagePath(path);
            analysis.setCombinedImageName(imageName);
        }

        analysis.setSaved(true);

        BudometerApp.getDaoSession().getAnalysisDao().update(analysis);

        resultEditText.setText("");
        strainEditText.setText("");
        cropIdEditText.setText("");
        notesEditText.setText("");
        ((MainActivity) getActivity()).showSnackBar(R.string.save_successful, R.drawable.success_background);
    }

    private void updateAnalysis(
            int lightRedPixelCount, int mediumRedPixelCount, int redPixelCount, int darkRedPixelCount,
            int lightPurplePixelCount, int mediumPurplePixelCount, int purplePixelCount, int darkPurplePixelCount,
            int lightGreenPixelCount, int mediumGreenPixelCount, int greenPixelCount, int darkGreenPixelCount,
            int lightOrangePixelCount, int mediumOrangePixelCount, int orangePixelCount, int darkOrangePixelCount,
            int lightGreyPixelCount, int mediumGreyPixelCount, int greyPixelCount, int darkGreyPixelCount,
            int lightRed, int mediumRed, int red, int darkRed,
            int lightPurple, int mediumPurple, int purple, int darkPurple,
            int lightGreen, int mediumGreen, int green, int darkGreen,
            int lightOrange, int mediumOrange, int orange, int darkOrange,
            int lightGrey, int mediumGrey, int grey, int darkGrey) {
        Analysis analysis = getAnalysis(BudometerSP.init(getActivity()).getLong(BudometerConfig.GREEN_DAO_ANALYSIS_ID));

        analysis.setLightRedPixelCount(lightRedPixelCount);
        analysis.setMediumRedPixelCount(mediumRedPixelCount);
        analysis.setRedPixelCount(redPixelCount);
        analysis.setDarkRedPixelCount(darkRedPixelCount);

        analysis.setLightPurplePixelCount(lightPurplePixelCount);
        analysis.setMediumPurplePixelCount(mediumPurplePixelCount);
        analysis.setPurplePixelCount(purplePixelCount);
        analysis.setDarkPurplePixelCount(darkPurplePixelCount);

        analysis.setLightGreenPixelCount(lightGreenPixelCount);
        analysis.setMediumGreenPixelCount(mediumGreenPixelCount);
        analysis.setGreenPixelCount(greenPixelCount);
        analysis.setDarkGreenPixelCount(darkGreenPixelCount);

        analysis.setLightOrangePixelCount(lightOrangePixelCount);
        analysis.setMediumOrangePixelCount(mediumOrangePixelCount);
        analysis.setOrangePixelCount(orangePixelCount);
        analysis.setDarkOrangePixelCount(darkOrangePixelCount);

        analysis.setLightGreyPixelCount(lightGreyPixelCount);
        analysis.setMediumGreyPixelCount(mediumGreyPixelCount);
        analysis.setGreyPixelCount(greyPixelCount);
        analysis.setDarkGreyPixelCount(darkGreyPixelCount);

        analysis.setLightRed(lightRed);
        analysis.setMediumRed(mediumRed);
        analysis.setRed(red);
        analysis.setDarkRed(darkRed);

        analysis.setLightPurple(lightPurple);
        analysis.setMediumPurple(mediumPurple);
        analysis.setPurple(purple);
        analysis.setDarkPurple(darkPurple);

        analysis.setLightGreen(lightGreen);
        analysis.setMediumGreen(mediumGreen);
        analysis.setGreen(green);
        analysis.setDarkGreen(darkGreen);

        analysis.setLightOrange(lightOrange);
        analysis.setMediumOrange(mediumOrange);
        analysis.setOrange(orange);
        analysis.setDarkOrange(darkOrange);

        analysis.setLightGrey(lightGrey);
        analysis.setMediumGrey(mediumGrey);
        analysis.setGrey(grey);
        analysis.setDarkGrey(darkGrey);

        BudometerApp.getDaoSession().getAnalysisDao().update(analysis);
        addTotalPixelCount();
    }

    private void addTotalPixelCount() {
        Analysis analysis = getAnalysis(BudometerSP.init(getActivity()).getLong(BudometerConfig.GREEN_DAO_ANALYSIS_ID));

        int totalPixelCount = analysis.getLightRedPixelCount() +
                analysis.getMediumRedPixelCount() + analysis.getRedPixelCount() + analysis.getDarkRedPixelCount() +
                analysis.getLightPurplePixelCount() + analysis.getMediumPurplePixelCount() + analysis.getPurplePixelCount() +
                analysis.getDarkPurplePixelCount() + analysis.getLightGreenPixelCount() + analysis.getMediumGreenPixelCount() +
                analysis.getGreenPixelCount() + analysis.getDarkGreenPixelCount() + analysis.getLightOrangePixelCount() +
                analysis.getMediumOrangePixelCount() + analysis.getOrangePixelCount() + analysis.getDarkOrangePixelCount() +
                analysis.getLightGreyPixelCount() + analysis.getMediumGreyPixelCount() + analysis.getGreyPixelCount() +
                analysis.getDarkGreyPixelCount();

        analysis.setTotalPixelCount(totalPixelCount);
        BudometerApp.getDaoSession().getAnalysisDao().update(analysis);
    }

    private String saveImageInternalStorage(Bitmap bitmapImage, String imageName) {
        ContextWrapper contextWrapper = new ContextWrapper(getActivity().getApplicationContext());
        File directory = contextWrapper.getDir("budometerImageDirectory", Context.MODE_PRIVATE);
        // Create budometerImageDirectory
        File directoryPath = new File(directory, imageName);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(directoryPath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private Analysis getAnalysis(long analysisId) {
        return BudometerApp.getDaoSession().getAnalysisDao().queryBuilder()
                .where(AnalysisDao.Properties.AnalysisId.eq(analysisId))
                .list().get(0);
    }
}