package com.app.budometer.fragment;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.budometer.R;
import com.app.budometer.activity.MainActivity;
import com.app.budometer.model.Analysis;
import com.app.budometer.model.AnalysisDao;
import com.app.budometer.util.BudometerApp;
import com.app.budometer.util.BudometerConfig;
import com.app.budometer.util.BudometerUtils;
import com.app.budometer.views.PieChartView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;


public class DetailsFragment extends BaseFragment {
    private DetailsFragment.OnDetailsFragmentInteractionListener mListener;
    private static DetailsFragment fragment = null;
    private PieChartView pieChartView;
    private TextView redTextView;
    private TextView purpleTextView;
    private TextView greenTextView;
    private TextView orangeTextView;
    private TextView greyTextView;
    private TextView turnedTextView;
    private TextView resultTextView;
    private TextView strainTextView;
    private TextView cropIdTextView;
    private TextView dateTextView;
    private TextView notesTextView;

    public interface OnDetailsFragmentInteractionListener {
        void onDetailsFragmentInteraction();
    }

    public DetailsFragment() {
    }

    public static Fragment newInstance(final long analysisId) {
        if (fragment == null)
            fragment = new DetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putLong(BudometerConfig.BUNDLE_ANALYSIS_ID, analysisId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DetailsFragment.OnDetailsFragmentInteractionListener) {
            mListener = (DetailsFragment.OnDetailsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDetailsFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        pieChartView = view.findViewById(R.id.pieChartView);
        pieChartView.setInnerRadius(0.4f);
        pieChartView.setBackGroundColor(0xffFFFFE0);
        pieChartView.setItemTextSize(20);
        pieChartView.setTextPadding(10);

        redTextView = view.findViewById(R.id.redTextView);
        purpleTextView = view.findViewById(R.id.purpleTextView);
        greenTextView = view.findViewById(R.id.greenTextView);
        orangeTextView = view.findViewById(R.id.orangeTextView);
        greyTextView = view.findViewById(R.id.greyTextView);
        turnedTextView = view.findViewById(R.id.turnedTextView);
        resultTextView = view.findViewById(R.id.resultTextView);
        strainTextView = view.findViewById(R.id.strainTextView);
        cropIdTextView = view.findViewById(R.id.cropIdTextView);
        dateTextView = view.findViewById(R.id.dateTextView);
        notesTextView = view.findViewById(R.id.notesTextView);

        Analysis analysis = getAnalysis(getArguments().getLong(BudometerConfig.BUNDLE_ANALYSIS_ID));

        populateChart(analysis);

        LinearLayout deleteButton = view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.CustomAlertDialogTheme))
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (deleteImage(analysis.getCombinedImageName())) {
                                    BudometerApp.getDaoSession().getAnalysisDao().deleteByKey(analysis.getAnalysisId());
                                    onBackPressed();
                                }
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setMessage(R.string.confirm_deletion)
                        .create().show();
            }
        });

        LinearLayout linearLayout1 = view.findViewById(R.id.linearLayout1);
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ((ImageView) view.findViewById(R.id.resultImageView)).setImageBitmap(loadImageInternalStorage(analysis.getCombinedImagePath(), analysis.getCombinedImageName()));

        return view;
    }

    private void populateChart(Analysis analysis) {
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

        turnedTextView.setText(analysis.getPercentageTurnedPistils());
        resultTextView.setText(analysis.getTensorFlowResult());
        strainTextView.setText(analysis.getStrain());
        cropIdTextView.setText(analysis.getCropId());
        dateTextView.setText(new SimpleDateFormat("EEEE, MMMM d, yyyy").format(new Date(analysis.getDate())));
        notesTextView.setText(analysis.getNotes());

        pieChartView.invalidate();
    }

    private String toPercentage(float n) {
        return String.format("%." + 1 + "f", n) + "%";
    }

    private boolean deleteImage(String imageName) {
        boolean deleted = false;
        ContextWrapper contextWrapper = new ContextWrapper(getActivity().getApplicationContext());
        File directory = contextWrapper.getDir("budometerImageDirectory", Context.MODE_PRIVATE);
        File file = new File(directory, imageName);
        if (file.exists()) {
            if (file.delete()) {
                ((MainActivity) getActivity()).showSnackBar(R.string.file_deleted, R.drawable.success_background);
                deleted = true;
            } else {
                ((MainActivity) getActivity()).showSnackBar(R.string.error_deleting_file, R.drawable.error_background);
                deleted = false;
            }
        }

        return deleted;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private Bitmap loadImageInternalStorage(String path, String imageName) {
        Bitmap bitmap = null;
        try {
            File file = new File(path, imageName);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Analysis getAnalysis(long analysisId) {
        return BudometerApp.getDaoSession().getAnalysisDao().queryBuilder()
                .where(AnalysisDao.Properties.AnalysisId.eq(analysisId))
                .list().get(0);
    }
}
