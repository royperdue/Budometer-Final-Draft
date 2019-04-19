package com.app.budometer.util;


import com.app.budometer.model.Analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class CropsGraph {
    private Map<String, List<Analysis>> adjacencyList = new TreeMap<String, List<Analysis>>();

    public void addAnalysisData(Analysis analysis, String keyCropId) {
        List<Analysis> analysisList;
        if (adjacencyList.size() == 0) {
            analysisList = new ArrayList<>();
            analysisList.add(analysis);
            adjacencyList.put(keyCropId, analysisList);
        }else if (!adjacencyList.containsKey(keyCropId)) {
            analysisList = new ArrayList<>();
            analysisList.add(analysis);
            adjacencyList.put(keyCropId, analysisList);
        } else {
            analysisList = adjacencyList.get(keyCropId);
            analysisList.add(analysis);
        }
    }

    public Map<String, List<Analysis>> getAdjacencyList() {
        return adjacencyList;
    }
}
