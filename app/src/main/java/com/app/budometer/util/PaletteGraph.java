package com.app.budometer.util;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class PaletteGraph {
    private Map<String, List<Palette.Swatch>> adjacencyList = new TreeMap<String, List<Palette.Swatch>>();

    public void addSwatchData(Palette.Swatch swatch, String keyColor) {
        List<Palette.Swatch> swatchList;
        if (adjacencyList.size() == 0) {
            swatchList = new ArrayList<>();
            swatchList.add(swatch);
            adjacencyList.put(keyColor, swatchList);
        }else if (!adjacencyList.containsKey(keyColor)) {
            swatchList = new ArrayList<>();
            swatchList.add(swatch);
            adjacencyList.put(keyColor, swatchList);
        } else {
            swatchList = adjacencyList.get(keyColor);
            swatchList.add(swatch);
        }
    }

    public Map<String, List<Palette.Swatch>> getAdjacencyList() {
        return adjacencyList;
    }
}
