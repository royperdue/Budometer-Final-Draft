package com.app.budometer.adapter;


import com.app.budometer.fragment.HelpFragmentCamera;
import com.app.budometer.fragment.HelpFragmentCarousel;
import com.app.budometer.fragment.HelpFragmentCrop;
import com.app.budometer.fragment.HelpFragmentMain;
import com.app.budometer.fragment.HelpFragmentChart;
import com.app.budometer.fragment.HelpFragmentGallery;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class HelpAdapter extends FragmentStatePagerAdapter implements HelpFragmentMain.OnHelpMainFragmentInteractionListener,
        HelpFragmentGallery.OnHelpGalleryFragmentInteractionListener, HelpFragmentChart.OnHelpChartFragmentInteractionListener,
HelpFragmentCarousel.OnHelpCarouselFragmentInteractionListener, HelpFragmentCamera.OnHelpCameraFragmentInteractionListener,
HelpFragmentCrop.OnHelpCropFragmentInteractionListener {

    public HelpAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return HelpFragmentMain.newInstance();
        else if (position == 1)
            return HelpFragmentCamera.newInstance();
        else  if (position == 2)
            return HelpFragmentGallery.newInstance();
        else  if (position == 3)
            return HelpFragmentChart.newInstance();
        else  if (position == 4)
            return HelpFragmentCrop.newInstance();
        else
            return HelpFragmentCarousel.newInstance();
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public void onHelpMainFragmentInteraction() {}

    @Override
    public void onHelpGalleryFragmentInteraction() {}

    @Override
    public void onHelpChartFragmentInteraction() {}

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
