package com.app.budometer.model;


import android.graphics.Bitmap;


public class ResultData {
    private String tensorFlowTitleGrowing;
    private float tensorFlowConfidenceGrowing = 0.0f;
    private String tensorFlowTitleClose;
    private float tensorFlowConfidenceClose = 0.0f;
    private String tensorFlowTitleReady;
    private float tensorFlowConfidenceReady = 0.0f;
    private Bitmap bitmap;

    public ResultData() {}

    public ResultData(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ResultData(String tensorFlowTitleGrowing, float tensorFlowConfidenceGrowing, String tensorFlowTitleClose, float tensorFlowConfidenceClose, String tensorFlowTitleReady, float tensorFlowConfidenceReady, Bitmap bitmap) {
        this.tensorFlowTitleGrowing = tensorFlowTitleGrowing;
        this.tensorFlowConfidenceGrowing = tensorFlowConfidenceGrowing;
        this.tensorFlowTitleClose = tensorFlowTitleClose;
        this.tensorFlowConfidenceClose = tensorFlowConfidenceClose;
        this.tensorFlowTitleReady = tensorFlowTitleReady;
        this.tensorFlowConfidenceReady = tensorFlowConfidenceReady;
        this.bitmap = bitmap;
    }

    public String getTensorFlowTitleGrowing() {
        return tensorFlowTitleGrowing;
    }

    public void setTensorFlowTitleGrowing(String tensorFlowTitleGrowing) {
        this.tensorFlowTitleGrowing = tensorFlowTitleGrowing;
    }

    public float getTensorFlowConfidenceGrowing() {
        return tensorFlowConfidenceGrowing;
    }

    public void setTensorFlowConfidenceGrowing(float tensorFlowConfidenceGrowing) {
        this.tensorFlowConfidenceGrowing = tensorFlowConfidenceGrowing;
    }

    public String getTensorFlowTitleClose() {
        return tensorFlowTitleClose;
    }

    public void setTensorFlowTitleClose(String tensorFlowTitleClose) {
        this.tensorFlowTitleClose = tensorFlowTitleClose;
    }

    public float getTensorFlowConfidenceClose() {
        return tensorFlowConfidenceClose;
    }

    public void setTensorFlowConfidenceClose(float tensorFlowConfidenceClose) {
        this.tensorFlowConfidenceClose = tensorFlowConfidenceClose;
    }

    public String getTensorFlowTitleReady() {
        return tensorFlowTitleReady;
    }

    public void setTensorFlowTitleReady(String tensorFlowTitleReady) {
        this.tensorFlowTitleReady = tensorFlowTitleReady;
    }

    public float getTensorFlowConfidenceReady() {
        return tensorFlowConfidenceReady;
    }

    public void setTensorFlowConfidenceReady(float tensorFlowConfidenceReady) {
        this.tensorFlowConfidenceReady = tensorFlowConfidenceReady;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}