package com.app.budometer.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.budometer.R;
import com.app.budometer.features.ImagePickerConfig;
import com.app.budometer.features.IpCons;
import com.app.budometer.listener.ImagePickerView;
import com.app.budometer.model.Folder;
import com.app.budometer.model.Image;
import com.app.budometer.util.BudometerConfig;
import com.app.budometer.util.BudometerSP;
import com.app.budometer.views.CircleMenuView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class MainFragment extends BaseFragment {
    public static final String TAG = "Home";
    private static MainFragment fragment = null;
    private OnMainFragmentInteractionListener mListener;
    private LinearLayout firstUseInstructionsLayout;
    private Button gotItButton;
    private CircleMenuView circleMenuView;

    public interface OnMainFragmentInteractionListener {
        void onMainFragmentInteraction(int index);
    }

    public MainFragment() {
    }

    public static Fragment newInstance() {
        if (fragment == null)
            fragment = new MainFragment();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMainFragmentInteractionListener) {
            mListener = (OnMainFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMainFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        firstUseInstructionsLayout = view.findViewById(R.id.firstUseInstructionsLayout);

        gotItButton = view.findViewById(R.id.gotItButton);
        gotItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BudometerSP.init(getActivity()).putBoolean(BudometerConfig.GOT_IT, true);
                firstUseInstructionsLayout.setVisibility(View.GONE);
            }
        });

        if (BudometerSP.init(getActivity()).getBoolean(BudometerConfig.GOT_IT))
            firstUseInstructionsLayout.setVisibility(View.GONE);

        circleMenuView = view.findViewById(R.id.circle_menu);
        circleMenuView.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onMenuOpenAnimationStart(@NonNull CircleMenuView view) {
            }

            @Override
            public void onMenuOpenAnimationEnd(@NonNull CircleMenuView view) {
            }

            @Override
            public void onMenuCloseAnimationStart(@NonNull CircleMenuView view) {
            }

            @Override
            public void onMenuCloseAnimationEnd(@NonNull CircleMenuView view) {
            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int index) {
                Log.d("D", "onButtonClickAnimationStart| index: " + index);

                if (index == 0) {
                    mListener.onMainFragmentInteraction(index);
                } else if (index == 1) {
                    mListener.onMainFragmentInteraction(index);
                } else if (index == 2) {
                    mListener.onMainFragmentInteraction(index);
                } else if (index == 3) {
                    mListener.onMainFragmentInteraction(index);
                }
            }

            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
            }

            @Override
            public boolean onButtonLongClick(@NonNull CircleMenuView view, int index) {
                return true;
            }

            @Override
            public void onButtonLongClickAnimationStart(@NonNull CircleMenuView view, int index) {
            }

            @Override
            public void onButtonLongClickAnimationEnd(@NonNull CircleMenuView view, int index) {
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
