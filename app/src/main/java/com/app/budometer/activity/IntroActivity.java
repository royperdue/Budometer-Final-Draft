package com.app.budometer.activity;

import android.os.Bundle;

import com.app.budometer.fragment.HelpFragmentCamera;
import com.app.budometer.fragment.HelpFragmentCarousel;
import com.app.budometer.fragment.HelpFragmentCrop;
import com.app.budometer.fragment.HelpFragmentMain;
import com.app.budometer.fragment.HelpFragmentChart;
import com.app.budometer.fragment.HelpFragmentGallery;


public class IntroActivity extends HelpActivity implements HelpFragmentMain.OnHelpMainFragmentInteractionListener,
        HelpFragmentGallery.OnHelpGalleryFragmentInteractionListener, HelpFragmentChart.OnHelpChartFragmentInteractionListener,
        HelpFragmentCarousel.OnHelpCarouselFragmentInteractionListener, HelpFragmentCamera.OnHelpCameraFragmentInteractionListener,
        HelpFragmentCrop.OnHelpCropFragmentInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFinishButtonTitle("Finish");

        setHelpPagesReady();
    }

    @Override
    public void onSkipButtonPressed() {
        super.onSkipButtonPressed();
    }

    @Override
    public void onFinishButtonPressed() {
        finish();
    }

    @Override
    public void onHelpMainFragmentInteraction() {

    }

    @Override
    public void onHelpGalleryFragmentInteraction() {

    }

    @Override
    public void onHelpChartFragmentInteraction() {

    }

    @Override
    public void onHelpCameraFragmentInteraction() {

    }

    @Override
    public void onHelpCarouselFragmentInteraction() {

    }

    @Override
    public void onHelpCropFragmentInteraction() {

    }
}
