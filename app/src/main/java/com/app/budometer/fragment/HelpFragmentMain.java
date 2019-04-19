package com.app.budometer.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.budometer.R;


public class HelpFragmentMain extends BaseFragment {
    private OnHelpMainFragmentInteractionListener mListener;
    private static HelpFragmentMain fragment = null;

    public interface OnHelpMainFragmentInteractionListener {
        void onHelpMainFragmentInteraction();
    }

    public HelpFragmentMain() {
    }

    public static HelpFragmentMain newInstance() {
        if (fragment == null)
            fragment = new HelpFragmentMain();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHelpMainFragmentInteractionListener) {
            mListener = (OnHelpMainFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHelpMainFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_help_main, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

