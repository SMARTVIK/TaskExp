package com.projectbox.uploadfile.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.projectbox.uploadfile.R;
import com.projectbox.uploadfile.activity.ImageUploadActivity;
import com.projectbox.uploadfile.database.Image;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private final Activity context;
    List<Image> images = new ArrayList<>();

    public ImagesAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Image image = images.get(position);

        Picasso.get()
                .load(getFile(image)).resize(100,100)  // resize is used to scroll smooth the list as Images are too heavy
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                        Log.d("result","loaded successfully");

                    }

                    @Override
                    public void onError(Exception e) {

                        Log.d("result","error in loading");

                    }
                });

        if (image.isUploaded()) {
            holder.tickImage.setVisibility(View.VISIBLE);
        } else {
            holder.tickImage.setVisibility(View.GONE);
        }
    }

    private File getFile(Image selectedImage) {
        return new File(selectedImage.getImagePath().contains("/raw") ? selectedImage.getImagePath().replace("/raw", "") : selectedImage.getImagePath());
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private ImageView tickImage;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            tickImage = itemView.findViewById(R.id.tick_image);
        }
    }

    public void setData(List<Image> data) {
        this.images = data;
        notifyDataSetChanged();
    }

}
