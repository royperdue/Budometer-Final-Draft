package com.app.budometer.util;


import android.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BudometerPalette {
    private static String[] BUD_COLORS = {
            "#dcbcb4", "#cf878b", "#c9576a", "#853745",
            "#d6c6e6", "#c3ade4", "#91809d", "#5e4b67",
            "#92a170", "#99985e", "#93aa56", "#4f583a",
            "#e0b850", "#d0a840", "#c8a038", "#c4761d",
            "#a77840", "#a75c2d", "#85502a", "#624527",
            "#fafbfa", "#e6e5e1", "#b5b0a1", "#a1ae9e"};

    public static void generate(int colorInt, OnColorRoundingDone onColorRoundingDone) {
        onColorRoundingDone.onDone(Color.parseColor(getSimilarColor(String.format("#%06X", (0xFFFFFF & colorInt)), BUD_COLORS)));
    }

    private static String getSimilarColor(String color, String[] baseColors) {
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

        if (hexColor.equals("#a77840"))
            hexColor = "#e0b850";
        else if (hexColor.equals("#a75c2d"))
            hexColor = "#d0a840";
        else if (hexColor.equals("#85502a"))
            hexColor = "#c8a038";
        else if (hexColor.equals("#624527"))
            hexColor = "#c4761d";

        return hexColor;
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
