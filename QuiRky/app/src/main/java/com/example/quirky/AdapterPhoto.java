/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * An Adapter for RecyclerViews that takes in a list of Bitmap objects
 * and maps them to the layout file for the QRCode adapter in qr_recycler_items.xml
 */
public class AdapterPhoto extends RecyclerView.Adapter<AdapterPhoto.QRViewHolder> {
    private final ArrayList<Bitmap> photos;
    private final Context ct;
    private final RecyclerClickerListener listener;

    /**
     * Constructor with an item listener.
     * @param photos The array of Bitmap objects
     * @param ct Context because it does things that helps the adapter do stuff. Duh.
     * @param listener An instance of a recycler clicker listener that is called when a photo is clicked on
     */
    public AdapterPhoto(ArrayList<Bitmap> photos, Context ct, RecyclerClickerListener listener) {
        this.photos = photos;
        this.ct = ct;
        this.listener = listener;
    }

    /**
     * Constructor without a listener. If this constructor is used, the photos will not be clickable
     * @param photos An array of Bitmap objects
     * @param ct Context
     */
    public AdapterPhoto(ArrayList<Bitmap> photos, Context ct) {
        this.photos = photos;
        this.ct = ct;
        this.listener = null;
    }

    public LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(ct, LinearLayoutManager.HORIZONTAL, false);
    }

    public static class QRViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public QRViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_for_recycler);
        }
    }

    @NonNull
    @Override
    public QRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.recycler_image, parent, false);
        return new QRViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QRViewHolder holder, int position) {
        holder.image.setImageBitmap(photos.get(position));

        if(listener != null)
            holder.image.setOnClickListener(view -> {
                Log.d("Adapter says:", "Clicked on item " + position);
                listener.OnClickListItem(holder.getAdapterPosition());
            });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}