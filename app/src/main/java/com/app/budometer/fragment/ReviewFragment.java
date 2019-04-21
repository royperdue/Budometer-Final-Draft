package com.app.budometer.fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.budometer.R;
import com.app.budometer.model.InnerData;
import com.app.budometer.model.InnerItem;
import com.app.budometer.adapter.OuterAdapter;
import com.app.budometer.model.Analysis;
import com.app.budometer.util.CropsGraph;
import com.app.budometer.util.BudometerApp;
import com.app.budometer.views.CircleMenuView;
import com.app.budometer.views.listView.TailLayoutManager;
import com.app.budometer.views.listView.TailRecyclerView;
import com.app.budometer.views.listView.TailSnapHelper;
import com.app.budometer.views.listView.header.HeaderTransformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;


public class ReviewFragment extends BaseFragment {
    public static final String TAG = "Review";
    private static ReviewFragment fragment = null;
    private OnReviewFragmentInteractionListener mListener;
    private OnReviewButtonClickListener mButtonListener;

    public interface OnReviewFragmentInteractionListener {
        void onReviewFragmentInteraction(final long analysisId);
    }

    public interface OnReviewButtonClickListener {
        void onReviewButtonClick(int index);
    }

    public ReviewFragment() {
    }

    public static ReviewFragment newInstance() {
        if (fragment == null)
            fragment = new ReviewFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnReviewFragmentInteractionListener) {
            mListener = (OnReviewFragmentInteractionListener) context;
            mButtonListener = (OnReviewButtonClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnReviewFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        CircleMenuView circleMenuView = view.findViewById(R.id.circle_menu);
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
                Log.d("D", "onMenuCloseAnimationEnd");
            }

            @Override
            public void onButtonClickAnimationStart(@NonNull CircleMenuView view, int index) {
                Log.d("D", "onButtonClickAnimationStart| index: " + index);

                if (index == 0) {
                    mButtonListener.onReviewButtonClick(index);
                } else if (index == 1) {
                    mButtonListener.onReviewButtonClick(index);
                } else if (index == 2) {
                    mButtonListener.onReviewButtonClick(index);
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

        List<Analysis> analysisList = BudometerApp.getDaoSession().getAnalysisDao().queryBuilder().list();
        final List<List<InnerData>> outerData = new ArrayList<>();
        CropsGraph cropsGraph = new CropsGraph();
        Map<String, List<Analysis>> adjacencyList = cropsGraph.getAdjacencyList();

        for (int i = 0; i < analysisList.size(); i++) {
            if (analysisList.get(i).getCropId() != null)
                cropsGraph.addAnalysisData(analysisList.get(i), analysisList.get(i).getCropId());
        }

        Iterator iterator = adjacencyList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry elementData = (Map.Entry) iterator.next();
            List<Analysis> analyses = (List<Analysis>) elementData.getValue();
            final List<InnerData> innerData = new ArrayList<>();

            for (int i = 0; i < analyses.size(); i++) {
                if (i == 0) {
                    innerData.add(new InnerData(analyses.get(i).getCropId(), analyses.get(i).getStrain(),
                            analyses.get(i).getDate(), analyses.get(i).getNotes(), analyses.get(i).getAnalysisId()));
                }

                innerData.add(new InnerData(analyses.get(i).getCropId(), analyses.get(i).getStrain(),
                        analyses.get(i).getDate(), analyses.get(i).getNotes(), analyses.get(i).getAnalysisId()));
            }

            outerData.add(innerData);
        }

        initRecyclerView(outerData);
    }

    private void initRecyclerView(List<List<InnerData>> data) {
        final TailRecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        ((TailLayoutManager) recyclerView.getLayoutManager()).setPageTransformer(new HeaderTransformer());
        recyclerView.setAdapter(new OuterAdapter(data));

        new TailSnapHelper().attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnInnerItemClick(InnerItem item) {
        final InnerData itemData = item.getItemData();
        if (itemData == null) {
            return;
        }

        mListener.onReviewFragmentInteraction(item.analysisId);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}