package com.app.budometer.model;

public class InnerData {
    public final String cropId;
    public final String strain;
    public final Long date;
    public final String notes;
    public long analysisId;

    public InnerData(String cropId, String strain, Long date, String notes, long analysisId) {
        this.cropId = cropId;
        this.strain = strain;
        this.date = date;
        this.notes = notes;
        this.analysisId = analysisId;
    }
}
