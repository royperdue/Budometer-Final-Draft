package com.app.budometer.util;


import android.graphics.Color;

import androidx.core.graphics.ColorUtils;

import com.app.budometer.R;
import com.app.budometer.model.Analysis;
import com.app.budometer.model.AnalysisDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class BudometerPalette {
    public static void generate(int colorInt, OnColorRoundingDone onColorRoundingDone) {
        onColorRoundingDone.onDone(Color.parseColor(getSimilarColor(String.format("#%06X", (0xFFFFFF & colorInt)))));
    }

    private static String getSimilarColor(String color) {
        Analysis analysis = getAnalysis(BudometerSP.init(BudometerApp.getAppContext()).getLong(BudometerConfig.GREEN_DAO_ANALYSIS_ID));

        String[] baseColors = null;

        if (analysis != null) {
            if (analysis.getTensorFlowConfidenceOrange() > analysis.getTensorFlowConfidenceWhite() && analysis.getTensorFlowConfidenceOrange() > analysis.getTensorFlowConfidencePurple())
                baseColors = BudometerApp.getAppContext().getResources().getStringArray(R.array.image_extraction_colors_orange);
            else if (analysis.getTensorFlowConfidenceWhite() > analysis.getTensorFlowConfidenceOrange() && analysis.getTensorFlowConfidenceWhite() > analysis.getTensorFlowConfidencePurple())
                baseColors = BudometerApp.getAppContext().getResources().getStringArray(R.array.image_extraction_colors_grey);
            else if (analysis.getTensorFlowConfidencePurple() > analysis.getTensorFlowConfidenceOrange() && analysis.getTensorFlowConfidencePurple() > analysis.getTensorFlowConfidenceWhite())
                baseColors = BudometerApp.getAppContext().getResources().getStringArray(R.array.image_extraction_colors_purple);
        } else
            baseColors = BudometerApp.getAppContext().getResources().getStringArray(R.array.image_extraction_colors);
        List<Double> difference = new ArrayList<>();

        color = color.replace("#", "");

        //Convert to RGB, then R, G, B
        String rgb = getRGB(color);
        int r = Integer.parseInt(rgb.split(",")[0]);
        int g = Integer.parseInt(rgb.split(",")[1]);
        int b = Integer.parseInt(rgb.split(",")[2]);

        for (String baseColor : baseColors) {
            baseColor = baseColor.replace("#", "");

            String baseRGB = getRGB(baseColor);
            int baseR = Integer.parseInt(baseRGB.split(",")[0]);
            int baseG = Integer.parseInt(baseRGB.split(",")[1]);
            int baseB = Integer.parseInt(baseRGB.split(",")[2]);

            difference.add(Math.sqrt(
                    (r - baseR) * (r - baseR) +
                            (g - baseG) * (g - baseG) +
                            (b - baseB) * (b - baseB)
            ));
        }

        return baseColors[difference.indexOf(Collections.min(difference))];
    }

    //Function to convert HEX to RGB
    private static String getRGB(String color) {
        return String.valueOf(Integer.parseInt(color.charAt(0) + "" + color.charAt(1), 16))
                + ',' +
                Integer.parseInt(color.charAt(2) + "" + color.charAt(3), 16)
                + ',' +
                Integer.parseInt(color.charAt(4) + "" + color.charAt(5), 16);
    }

    private static Analysis getAnalysis(long analysisId) {
        Analysis analysis = null;
        List<Analysis> analyses = BudometerApp.getDaoSession().getAnalysisDao().queryBuilder()
                .where(AnalysisDao.Properties.AnalysisId.eq(analysisId))
                .list();
        if (analyses.size() > 0)
            analysis = analyses.get(0);

        return analysis;
    }

    public interface OnColorRoundingDone {
        void onDone(int color);
    }
}
