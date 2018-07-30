package com.projectbox.uploadfile.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projectbox.uploadfile.R;
import com.projectbox.uploadfile.locationmodule.LocationStringModel;

import java.util.ArrayList;
import java.util.List;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.ViewHolder> {

    private List<LocationStringModel> results = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LocationStringModel detailsModel = results.get(position);
        holder.title.setText(detailsModel.getLocationupdateresult());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void setData(List<LocationStringModel> detailsModels) {
        this.results = detailsModels;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text);
        }
    }
}
