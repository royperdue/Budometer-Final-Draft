package com.app.budometer.util;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.budometer.R;
import com.app.budometer.model.Analysis;
import com.otaliastudios.autocomplete.RecyclerViewPresenter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public class AnalysisPresenter extends RecyclerViewPresenter<Analysis> {
    protected Adapter adapter;

    public AnalysisPresenter(Context context) {
        super(context);
    }

    @Override
    protected PopupDimensions getPopupDimensions() {
        PopupDimensions dims = new PopupDimensions();
        dims.width = 600;
        dims.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        return dims;
    }

    @Override
    protected RecyclerView.Adapter instantiateAdapter() {
        adapter = new Adapter();
        return adapter;
    }

    @Override
    protected void onQuery(@Nullable CharSequence query) {
        List<Analysis> analysisList = BudometerApp.getDaoSession().getAnalysisDao().queryBuilder().list();
        List<Analysis> list = new ArrayList<>();

        if (TextUtils.isEmpty(query)) {
            adapter.setData(list);
        } else {
            query = query.toString().toLowerCase();
            for (int i = 0; i < analysisList.size(); i++) {
                if (analysisList.size() > 0 && analysisList.get(i).getCropId() != null) {
                    if (analysisList.get(i).getCropId().toLowerCase().contains(query)) {
                        list.add(analysisList.get(i));
                    }
                }
            }

            adapter.setData(list);
            Log.e("AnalysisPresenter", "found " + list.size() + " analysis for query " + query);
        }
        adapter.notifyDataSetChanged();
    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {
        private List<Analysis> data;

        public class Holder extends RecyclerView.ViewHolder {
            private View root;
            private TextView cropId;
            private TextView strain;

            public Holder(View itemView) {
                super(itemView);
                root = itemView;
                cropId = itemView.findViewById(R.id.cropId);
                strain = itemView.findViewById(R.id.strain);
            }
        }

        public void setData(List<Analysis> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return (isEmpty()) ? 1 : data.size();
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.item_analysis, parent, false));
        }

        private boolean isEmpty() {
            return data == null || data.isEmpty();
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            if (isEmpty()) {
                holder.cropId.setText("...");
                holder.strain.setText("...");
                holder.root.setOnClickListener(null);
                return;
            }
            final Analysis analysisEntry = data.get(position);
            holder.cropId.setText(analysisEntry.getCropId());
            holder.strain.setText(analysisEntry.getStrain());
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchClick(analysisEntry);
                }
            });
        }
    }
}
