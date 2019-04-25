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
import com.app.budometer.model.Counter;
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
    public static final String TAG = "Result";
    private OnResultFragmentInteractionListener mListener;
    private static ResultFragment fragment = null;
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
    private TextView yellowTextView;
    private TextView orangeTextView;
    private TextView brownTextView;
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

        setRetainInstance(false);
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
                clearBackStack();
            }
        });

        redTextView = view.findViewById(R.id.redTextView);
        purpleTextView = view.findViewById(R.id.purpleTextView);
        greenTextView = view.findViewById(R.id.greenTextView);
        yellowTextView = view.findViewById(R.id.yellowTextView);
        orangeTextView = view.findViewById(R.id.orangeTextView);
        brownTextView = view.findViewById(R.id.brownTextView);
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

        //System.out.println("ANALYSIS-ID: " + analysis.getAnalysisId());
        //System.out.println("GROWING: " + analysis.getTensorFlowConfidenceGrowing());
        //System.out.println("READY: " + analysis.getTensorFlowConfidenceReady());

        String[] paths = new String[4];
        paths[0] = analysis.getImagePath1();
        paths[1] = analysis.getImagePath2();
        paths[2] = analysis.getImagePath3();
        paths[3] = analysis.getImagePath4();

        evaluateTensorFlowResults(analysis.getTensorFlowConfidenceGrowing(), analysis.getTensorFlowConfidenceReady());

        loadBitmap(getBitmaps(paths));
    }

    private void evaluateTensorFlowResults(float tensorFlowConfidenceGrowing, float tensorFlowConfidenceReady) {
        //System.out.println("EVALUATE-RESULTS: ");
        if (tensorFlowConfidenceGrowing > tensorFlowConfidenceReady)
            resultEditText.setText(R.string.result_message_growing);
        else
            resultEditText.setText(R.string.result_message_ready);
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

        BudometerApp.getDaoSession().clear();
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
        Palette.from(bitmap).maximumColorCount(64).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                PaletteGraph paletteGraph = new PaletteGraph();
                Counter counter = new Counter();
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
                            counter.setLightRedPixelCount(counter.getLightRedPixelCount() + swatch.getPopulation());
                            counter.setLightRed(color);
                        } else if (swatch.getColorName().equals("redMedium")) {
                            counter.setMediumRedPixelCount(counter.getMediumRedPixelCount() + swatch.getPopulation());
                            counter.setMediumRed(color);
                        } else if (swatch.getColorName().equals("red")) {
                            counter.setRedPixelCount(counter.getRedPixelCount() + swatch.getPopulation());
                            counter.setRed(color);
                        } else if (swatch.getColorName().equals("redDark")) {
                            counter.setDarkRedPixelCount(counter.getDarkRedPixelCount() + swatch.getPopulation());
                            counter.setDarkRed(color);
                        } else if (swatch.getColorName().equals("purpleLight")) {
                            counter.setLightPurplePixelCount(counter.getLightPurplePixelCount() + swatch.getPopulation());
                            counter.setLightPurple(color);
                        } else if (swatch.getColorName().equals("purpleMedium")) {
                            counter.setMediumPurplePixelCount(counter.getMediumPurplePixelCount() + swatch.getPopulation());
                            counter.setMediumPurple(color);
                        } else if (swatch.getColorName().equals("purple")) {
                            counter.setPurplePixelCount(counter.getPurplePixelCount() + swatch.getPopulation());
                            counter.setPurple(color);
                        } else if (swatch.getColorName().equals("purpleDark")) {
                            counter.setDarkPurplePixelCount(counter.getDarkPurplePixelCount() + swatch.getPopulation());
                            counter.setDarkPurple(color);
                        } else if (swatch.getColorName().equals("greenLight")) {
                            counter.setLightGreenPixelCount(counter.getLightGreenPixelCount() + swatch.getPopulation());
                            counter.setLightGreen(color);
                        } else if (swatch.getColorName().equals("greenMedium")) {
                            counter.setMediumGreenPixelCount(counter.getMediumGreenPixelCount() + swatch.getPopulation());
                            counter.setMediumGreen(color);
                        } else if (swatch.getColorName().equals("green")) {
                            counter.setGreenPixelCount(counter.getGreenPixelCount() + swatch.getPopulation());
                            counter.setGreen(color);
                        } else if (swatch.getColorName().equals("greenDark")) {
                            counter.setDarkGreenPixelCount(counter.getDarkGreenPixelCount() + swatch.getPopulation());
                            counter.setDarkGreen(color);
                        } else if (swatch.getColorName().equals("yellowLight")) {
                            counter.setLightYellowPixelCount(counter.getLightYellowPixelCount() + swatch.getPopulation());
                            counter.setLightYellow(color);
                        } else if (swatch.getColorName().equals("yellowMedium")) {
                            counter.setMediumYellowPixelCount(counter.getMediumYellowPixelCount() + swatch.getPopulation());
                            counter.setMediumYellow(color);
                        } else if (swatch.getColorName().equals("yellow")) {
                            counter.setYellowPixelCount(counter.getYellowPixelCount() + swatch.getPopulation());
                            counter.setYellow(color);
                        } else if (swatch.getColorName().equals("yellowDark")) {
                            counter.setDarkYellowPixelCount(counter.getDarkYellowPixelCount() + swatch.getPopulation());
                            counter.setDarkYellow(color);
                        } else if (swatch.getColorName().equals("orangeLight")) {
                            counter.setLightOrangePixelCount(counter.getLightOrangePixelCount() + swatch.getPopulation());
                            counter.setLightOrange(color);
                        } else if (swatch.getColorName().equals("orangeMedium")) {
                            counter.setMediumOrangePixelCount(counter.getMediumOrangePixelCount() + swatch.getPopulation());
                            counter.setMediumOrange(color);
                        } else if (swatch.getColorName().equals("orange")) {
                            counter.setOrangePixelCount(counter.getOrangePixelCount() + swatch.getPopulation());
                            counter.setOrange(color);
                        } else if (swatch.getColorName().equals("orangeDark")) {
                            counter.setDarkOrangePixelCount(counter.getDarkOrangePixelCount() + swatch.getPopulation());
                            counter.setDarkOrange(color);
                        } else if (swatch.getColorName().equals("brownLight")) {
                            counter.setLightBrownPixelCount(counter.getLightBrownPixelCount() + swatch.getPopulation());
                            counter.setLightBrown(color);
                        } else if (swatch.getColorName().equals("brownMedium")) {
                            counter.setMediumBrownPixelCount(counter.getMediumBrownPixelCount() + swatch.getPopulation());
                            counter.setMediumBrown(color);
                        } else if (swatch.getColorName().equals("brown")) {
                            counter.setBrownPixelCount(counter.getBrownPixelCount() + swatch.getPopulation());
                            counter.setBrown(color);
                        } else if (swatch.getColorName().equals("brownDark")) {
                            counter.setDarkBrownPixelCount(counter.getDarkBrownPixelCount() + swatch.getPopulation());
                            counter.setDarkBrown(color);
                        } else if (swatch.getColorName().equals("greyLight")) {
                            counter.setLightGreyPixelCount(counter.getLightGreyPixelCount() + swatch.getPopulation());
                            counter.setLightGrey(color);
                        } else if (swatch.getColorName().equals("greyMedium")) {
                            counter.setMediumGreyPixelCount(counter.getMediumGreyPixelCount() + swatch.getPopulation());
                            counter.setMediumGrey(color);
                        } else if (swatch.getColorName().equals("grey")) {
                            counter.setGreyPixelCount(counter.getGreyPixelCount() + swatch.getPopulation());
                            counter.setGrey(color);
                        } else if (swatch.getColorName().equals("greyDark")) {
                            counter.setDarkGreyPixelCount(counter.getDarkGreyPixelCount() + swatch.getPopulation());
                            counter.setDarkGrey(color);
                        }
                    }
                }

                updateAnalysis(counter);
                adjacencyList.clear();
                adjacencyList = null;
                populateChart();
                turnedEditText.setText("Percent pistil curling: " + calculatePixelsTurned());
                progressDialog.dismiss();
            }
        });
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
        if (analysis.getLightYellowPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getLightYellowPixelCount(), analysis.getLightYellow()));
        if (analysis.getMediumYellowPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getMediumYellowPixelCount(), analysis.getMediumYellow()));
        if (analysis.getYellowPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getYellowPixelCount(), analysis.getYellow()));
        if (analysis.getDarkYellowPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getDarkYellowPixelCount(), analysis.getDarkYellow()));
        if (analysis.getLightOrangePixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getLightOrangePixelCount(), analysis.getLightOrange()));
        if (analysis.getMediumOrangePixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getMediumOrangePixelCount(), analysis.getMediumOrange()));
        if (analysis.getOrangePixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getOrangePixelCount(), analysis.getOrange()));
        if (analysis.getDarkOrangePixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getDarkOrangePixelCount(), analysis.getDarkOrange()));
        if (analysis.getLightBrownPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getLightBrownPixelCount(), analysis.getLightBrown()));
        if (analysis.getMediumBrownPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getMediumBrownPixelCount(), analysis.getMediumBrown()));
        if (analysis.getBrownPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getBrownPixelCount(), analysis.getBrown()));
        if (analysis.getDarkBrownPixelCount() > 0)
            pieChartView.addItemType(new PieChartView.ItemType("", analysis.getDarkBrownPixelCount(), analysis.getDarkBrown()));
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

        if ((redPixelTotal * 100.0f) / analysis.getTotalPixelCount() < 1.0f)
            redTextView.setVisibility(View.GONE);

        int purplePixelTotal = analysis.getLightPurplePixelCount() + analysis.getMediumPurplePixelCount() + analysis.getPurplePixelCount() + analysis.getDarkPurplePixelCount();
        purpleTextView.setText(toPercentage((purplePixelTotal * 100.0f) / analysis.getTotalPixelCount()));
        purpleTextView.setBackground(BudometerUtils.getPurpleColor(getActivity()));

        if ((purplePixelTotal * 100.0f) / analysis.getTotalPixelCount() < 1.0f)
            purpleTextView.setVisibility(View.GONE);

        int greenPixelTotal = analysis.getLightGreenPixelCount() + analysis.getMediumGreenPixelCount() + analysis.getGreenPixelCount() + analysis.getDarkGreenPixelCount();
        greenTextView.setText(toPercentage((greenPixelTotal * 100.0f) / analysis.getTotalPixelCount()));
        greenTextView.setBackground(BudometerUtils.getGreenColor(getActivity()));

        if ((greenPixelTotal * 100.0f) / analysis.getTotalPixelCount() < 1.0f)
            greenTextView.setVisibility(View.GONE);

        int yellowPixelTotal = analysis.getLightYellowPixelCount() + analysis.getMediumYellowPixelCount() + analysis.getYellowPixelCount() + analysis.getDarkYellowPixelCount();
        yellowTextView.setText(toPercentage((yellowPixelTotal * 100.0f) / analysis.getTotalPixelCount()));
        yellowTextView.setBackground(BudometerUtils.getYellowColor(getActivity()));

        if ((yellowPixelTotal * 100.0f) / analysis.getTotalPixelCount() < 1.0f)
            yellowTextView.setVisibility(View.GONE);

        int orangePixelTotal = analysis.getLightOrangePixelCount() + analysis.getMediumOrangePixelCount() + analysis.getOrangePixelCount() + analysis.getDarkOrangePixelCount();
        orangeTextView.setText(toPercentage((orangePixelTotal * 100.0f) / analysis.getTotalPixelCount()));
        orangeTextView.setBackground(BudometerUtils.getOrangeColor(getActivity()));

        if ((orangePixelTotal * 100.0f) / analysis.getTotalPixelCount() < 1.0f)
            orangeTextView.setVisibility(View.GONE);

        int brownPixelTotal = analysis.getLightBrownPixelCount() + analysis.getMediumBrownPixelCount() + analysis.getBrownPixelCount() + analysis.getDarkBrownPixelCount();
        brownTextView.setText(toPercentage((brownPixelTotal * 100.0f) / analysis.getTotalPixelCount()));
        brownTextView.setBackground(BudometerUtils.getBrownColor(getActivity()));

        if ((brownPixelTotal * 100.0f) / analysis.getTotalPixelCount() < 1.0f)
            brownTextView.setVisibility(View.GONE);

        int greyPixelTotal = analysis.getLightGreyPixelCount() + analysis.getMediumGreyPixelCount() + analysis.getGreyPixelCount() + analysis.getDarkGreyPixelCount();
        greyTextView.setText(toPercentage((greyPixelTotal * 100.0f) / analysis.getTotalPixelCount()));
        greyTextView.setBackground(BudometerUtils.getGreyColor(getActivity()));

        if ((greyPixelTotal * 100.0f) / analysis.getTotalPixelCount() < 1.0f)
            greyTextView.setVisibility(View.GONE);

        resultEditText.setText("");
        strainEditText.setText("");
        cropIdEditText.setText("");
        notesEditText.setText("");
        evaluateTensorFlowResults(analysis.getTensorFlowConfidenceGrowing(), analysis.getTensorFlowConfidenceReady());

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
        int totalPixelsNotTurned = 0;
        int totalPixelsTurned = 0;
        String percentageTurned = "";
        Analysis analysis = getAnalysis(BudometerSP.init(getActivity()).getLong(BudometerConfig.GREEN_DAO_ANALYSIS_ID));

        if (analysis.getTensorFlowConfidenceOrange() > analysis.getTensorFlowConfidenceWhite() && analysis.getTensorFlowConfidenceOrange() > analysis.getTensorFlowConfidencePurple()) {
            totalPixelsNotTurned =
                    analysis.getLightBrownPixelCount() +
                            analysis.getLightOrangePixelCount() +
                            analysis.getLightRedPixelCount() +
                            analysis.getMediumRedPixelCount() +
                            analysis.getLightYellowPixelCount() +
                            analysis.getMediumYellowPixelCount() +
                            analysis.getYellowPixelCount() +
                            analysis.getDarkYellowPixelCount() +
                            analysis.getLightPurplePixelCount() +
                            analysis.getMediumPurplePixelCount() +
                            analysis.getPurplePixelCount() +
                            analysis.getDarkPurplePixelCount() +
                            analysis.getMediumGreyPixelCount() +
                            analysis.getGreyPixelCount() +
                            analysis.getDarkGreyPixelCount();

            totalPixelsTurned =
                    analysis.getRedPixelCount() +
                            analysis.getDarkRedPixelCount() +
                            analysis.getOrangePixelCount() +
                            analysis.getMediumOrangePixelCount() +
                            analysis.getDarkOrangePixelCount() +
                            analysis.getMediumBrownPixelCount() +
                            analysis.getBrownPixelCount() +
                            analysis.getDarkBrownPixelCount();
            percentageTurned = toPercentage((totalPixelsTurned * 100.0f) / (totalPixelsTurned + totalPixelsNotTurned));
        } else if (analysis.getTensorFlowConfidencePurple() > analysis.getTensorFlowConfidenceOrange() &&
                analysis.getTensorFlowConfidencePurple() > analysis.getTensorFlowConfidenceWhite() &&
                analysis.getTensorFlowConfidenceReady() > analysis.getTensorFlowConfidenceGrowing()) {
            totalPixelsNotTurned =
                    analysis.getLightBrownPixelCount() +
                            analysis.getLightOrangePixelCount() +
                            analysis.getMediumOrangePixelCount() +
                            analysis.getLightRedPixelCount() +
                            analysis.getLightYellowPixelCount() +
                            analysis.getMediumYellowPixelCount() +
                            analysis.getYellowPixelCount() +
                            analysis.getDarkYellowPixelCount() +
                            analysis.getLightPurplePixelCount() +
                            analysis.getGreyPixelCount() +
                            analysis.getDarkGreyPixelCount();

            totalPixelsTurned =
                    analysis.getMediumRedPixelCount() +
                            analysis.getRedPixelCount() +
                            analysis.getDarkRedPixelCount() +
                            analysis.getOrangePixelCount() +
                            analysis.getDarkOrangePixelCount() +
                            analysis.getMediumBrownPixelCount() +
                            analysis.getBrownPixelCount() +
                            analysis.getDarkBrownPixelCount() +
                            analysis.getMediumPurplePixelCount() +
                            analysis.getPurplePixelCount() +
                            analysis.getDarkPurplePixelCount() +
                            analysis.getMediumGreyPixelCount();

            percentageTurned = toPercentage((totalPixelsTurned * 100.0f) / (totalPixelsTurned + totalPixelsNotTurned));
        } else {
            percentageTurned = "%0.0";
        }

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
        BudometerApp.getDaoSession().clear();
        ((MainActivity) getActivity()).showSnackBar(R.string.save_successful, R.drawable.success_background);
    }

    private void updateAnalysis(Counter counter) {
        Analysis analysis = getAnalysis(BudometerSP.init(getActivity()).getLong(BudometerConfig.GREEN_DAO_ANALYSIS_ID));

        analysis.setLightRedPixelCount(counter.getLightRedPixelCount());
        analysis.setMediumRedPixelCount(counter.getMediumRedPixelCount());
        analysis.setRedPixelCount(counter.getRedPixelCount());
        analysis.setDarkRedPixelCount(counter.getDarkRedPixelCount());

        analysis.setLightPurplePixelCount(counter.getLightPurplePixelCount());
        analysis.setMediumPurplePixelCount(counter.getMediumPurplePixelCount());
        analysis.setPurplePixelCount(counter.getPurplePixelCount());
        analysis.setDarkPurplePixelCount(counter.getDarkPurplePixelCount());

        analysis.setLightGreenPixelCount(counter.getLightGreenPixelCount());
        analysis.setMediumGreenPixelCount(counter.getMediumGreenPixelCount());
        analysis.setGreenPixelCount(counter.getGreenPixelCount());
        analysis.setDarkGreenPixelCount(counter.getDarkGreenPixelCount());

        analysis.setLightYellowPixelCount(counter.getLightYellowPixelCount());
        analysis.setMediumYellowPixelCount(counter.getMediumYellowPixelCount());
        analysis.setYellowPixelCount(counter.getYellowPixelCount());
        analysis.setDarkYellowPixelCount(counter.getDarkYellowPixelCount());

        analysis.setLightOrangePixelCount(counter.getLightOrangePixelCount());
        analysis.setMediumOrangePixelCount(counter.getMediumOrangePixelCount());
        analysis.setOrangePixelCount(counter.getOrangePixelCount());
        analysis.setDarkOrangePixelCount(counter.getDarkOrangePixelCount());

        analysis.setLightBrownPixelCount(counter.getLightBrownPixelCount());
        analysis.setMediumBrownPixelCount(counter.getMediumBrownPixelCount());
        analysis.setBrownPixelCount(counter.getBrownPixelCount());
        analysis.setDarkBrownPixelCount(counter.getDarkBrownPixelCount());

        analysis.setLightGreyPixelCount(counter.getLightGreyPixelCount());
        analysis.setMediumGreyPixelCount(counter.getMediumGreyPixelCount());
        analysis.setGreyPixelCount(counter.getGreyPixelCount());
        analysis.setDarkGreyPixelCount(counter.getDarkGreyPixelCount());

        analysis.setLightRed(counter.getLightRed());
        analysis.setMediumRed(counter.getMediumRed());
        analysis.setRed(counter.getRed());
        analysis.setDarkRed(counter.getDarkRed());

        analysis.setLightPurple(counter.getLightPurple());
        analysis.setMediumPurple(counter.getMediumPurple());
        analysis.setPurple(counter.getPurple());
        analysis.setDarkPurple(counter.getDarkPurple());

        analysis.setLightGreen(counter.getLightGreen());
        analysis.setMediumGreen(counter.getMediumGreen());
        analysis.setGreen(counter.getGreen());
        analysis.setDarkGreen(counter.getDarkGreen());

        analysis.setLightYellow(counter.getLightYellow());
        analysis.setMediumYellow(counter.getMediumYellow());
        analysis.setYellow(counter.getYellow());
        analysis.setDarkYellow(counter.getDarkYellow());

        analysis.setLightOrange(counter.getLightOrange());
        analysis.setMediumOrange(counter.getMediumOrange());
        analysis.setOrange(counter.getOrange());
        analysis.setDarkOrange(counter.getDarkOrange());

        analysis.setLightBrown(counter.getLightBrown());
        analysis.setMediumBrown(counter.getMediumBrown());
        analysis.setBrown(counter.getBrown());
        analysis.setDarkBrown(counter.getDarkBrown());

        analysis.setLightGrey(counter.getLightGrey());
        analysis.setMediumGrey(counter.getMediumGrey());
        analysis.setGrey(counter.getGrey());
        analysis.setDarkGrey(counter.getDarkGrey());

        BudometerApp.getDaoSession().getAnalysisDao().update(analysis);
        addTotalPixelCount();
    }

    private void addTotalPixelCount() {
        Analysis analysis = getAnalysis(BudometerSP.init(getActivity()).getLong(BudometerConfig.GREEN_DAO_ANALYSIS_ID));

        int totalPixelCount = analysis.getLightRedPixelCount() +
                analysis.getMediumRedPixelCount() + analysis.getRedPixelCount() + analysis.getDarkRedPixelCount() +
                analysis.getLightPurplePixelCount() + analysis.getMediumPurplePixelCount() + analysis.getPurplePixelCount() +
                analysis.getDarkPurplePixelCount() + analysis.getLightGreenPixelCount() + analysis.getMediumGreenPixelCount() +
                analysis.getGreenPixelCount() + analysis.getDarkGreenPixelCount() + analysis.getLightYellowPixelCount() +
                analysis.getMediumYellowPixelCount() + analysis.getYellowPixelCount() + analysis.getDarkYellowPixelCount() +
                analysis.getLightOrangePixelCount() + analysis.getMediumOrangePixelCount() + analysis.getOrangePixelCount() +
                analysis.getDarkOrangePixelCount() + analysis.getLightBrownPixelCount() + analysis.getMediumBrownPixelCount() +
                analysis.getBrownPixelCount() + analysis.getDarkBrownPixelCount() + analysis.getLightGreyPixelCount() +
                analysis.getMediumGreyPixelCount() + analysis.getGreyPixelCount() + analysis.getDarkGreyPixelCount();

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
        Analysis analysis = null;
        List<Analysis> analyses = BudometerApp.getDaoSession().getAnalysisDao().queryBuilder()
                .where(AnalysisDao.Properties.AnalysisId.eq(analysisId))
                .list();
        if (analyses.size() > 0)
            analysis = analyses.get(0);

        return analysis;
    }
}