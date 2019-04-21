package com.app.budometer.util;


import android.graphics.Color;

import androidx.core.graphics.ColorUtils;

import com.app.budometer.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class BudometerPalette {
    static enum ColorWheel {
        RED(0.0f, 30.0f), ORANGE(30.0f, 60.0f), YELLOW(60.0f, 90.0f),
        LIME(90.0f, 120.0f), GREEN(120.0f, 150.0f), TEAL(150.0f, 180.0f),
        CYAN(180.0f, 210.0f), AZURE(210.0f, 240.0f), BLUE(240.0f, 270.0f),
        INDIGO(270.0f, 300.0f), PURPLE(300.0f, 330.0f), PINK(330.0f, 360.0f);

        private final float min;
        private final float max;

        private ColorWheel(float min, float max) {
            this.min = min;
            this.max = max;
        }

        public String getRange() {
            return String.format("%d,%d", min, max);
        }

        public static ColorWheel identifyColor(float val) {
            return Arrays.stream(values())
                    .filter(r -> val >= r.min && val <= r.max)
                    .findFirst()
                    .orElse(null);
        }
    }

    private static final float BLACK_MAX_LIGHTNESS = 0.012447455f;
    private static final float WHITE_MIN_LIGHTNESS = 0.55f;

    public static void generate(int colorInt, OnColorRoundingDone onColorRoundingDone) {
        onColorRoundingDone.onDone(Color.parseColor(getSimilarColor(String.format("#%06X", (0xFFFFFF & colorInt)))));
    }

    private static String getSimilarColor(String color) {
        int c = Color.parseColor(color);
        float[] hsv = new float[3];
        Color.colorToHSV(c, hsv);

        String[] baseColors = null;

        if (isBlack(hsv) || isWhite(hsv)) {
            baseColors = BudometerApp.getAppContext().getResources().getStringArray(R.array.image_extraction_colors_grey);
        } else if (String.valueOf(ColorWheel.identifyColor(hsv[0])).equals("LIME") || String.valueOf(ColorWheel.identifyColor(hsv[0])).equals("GREEN") ||
                String.valueOf(ColorWheel.identifyColor(hsv[0])).equals("TEAL") || String.valueOf(ColorWheel.identifyColor(hsv[0])).equals("CYAN") || String.valueOf(ColorWheel.identifyColor(hsv[0]))
                .equals("AZURE") || String.valueOf(ColorWheel.identifyColor(hsv[0])).equals("BLUE")) {
            baseColors = BudometerApp.getAppContext().getResources().getStringArray(R.array.image_extraction_colors_green);
        } else if (String.valueOf(ColorWheel.identifyColor(hsv[0])).equals("ORANGE")) {
            baseColors = BudometerApp.getAppContext().getResources().getStringArray(R.array.image_extraction_colors_orange);
        } else if (String.valueOf(ColorWheel.identifyColor(hsv[0])).equals("YELLOW")) {
            baseColors = BudometerApp.getAppContext().getResources().getStringArray(R.array.image_extraction_colors_yellow);
        } else if (String.valueOf(ColorWheel.identifyColor(hsv[0])).equals("RED") || String.valueOf(ColorWheel.identifyColor(hsv[0])).equals("PINK")) {
            baseColors = BudometerApp.getAppContext().getResources().getStringArray(R.array.image_extraction_colors_red);
        } else if (String.valueOf(ColorWheel.identifyColor(hsv[0])).equals("INDIGO") || String.valueOf(ColorWheel.identifyColor(hsv[0])).equals("PURPLE")) {
            baseColors = BudometerApp.getAppContext().getResources().getStringArray(R.array.image_extraction_colors_purple);
        }
        //Create an empty List for the difference between the colors
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

        String hexColor = baseColors[difference.indexOf(Collections.min(difference))];

        return hexColor;
    }

    private static boolean isBlack(float[] hslColor) {
        return hslColor[2] <= BLACK_MAX_LIGHTNESS;
    }

    private static boolean isWhite(float[] hslColor) {
        return hslColor[2] >= WHITE_MIN_LIGHTNESS;
    }

    //Function to convert HEX to RGB
    private static String getRGB(String color) {
        return String.valueOf(Integer.parseInt(color.charAt(0) + "" + color.charAt(1), 16))
                + ',' +
                Integer.parseInt(color.charAt(2) + "" + color.charAt(3), 16)
                + ',' +
                Integer.parseInt(color.charAt(4) + "" + color.charAt(5), 16);
    }

    public interface OnColorRoundingDone {
        void onDone(int color);
    }
}
