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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * An Adapter for RecyclerViews that takes in two ArrayLists: one for Bitmap objects & one for strings,
 * and maps them to the layout file for the QRCode adapter in qr_recycler_items.xml
 */
public class AdapterTextPhoto extends RecyclerView.Adapter<AdapterTextPhoto.QRViewHolder> {
    private ArrayList<Bitmap> photos;
    private ArrayList<String> data;
    Context ct;
    RecyclerClickerListener listener;

    /**
     * Constructor for the adapter.
     * @param photos The array of Bitmap objects
     * @param data The array of string objects
     * @param ct Context because it does things that helps the adapter do stuff. Duh.
     */
    public AdapterTextPhoto(ArrayList<String> data, ArrayList<Bitmap> photos, Context ct, RecyclerClickerListener listener) {
        this.photos = photos;
        this.data = data;
        this.ct = ct;
        this.listener = listener;
    }

    public AdapterTextPhoto(ArrayList<String> data, ArrayList<Bitmap> photos, Context ct) {
        this.photos = photos;
        this.data = data;
        this.ct = ct;
        this.listener = null;
    }

    public LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(ct, LinearLayoutManager.VERTICAL, false);
    }

    /**
     * Helper subclass. It does things that help the other things do the things.
     * Hey I don't know how it works! I just know how to do it.
     */
    public static class QRViewHolder extends RecyclerView.ViewHolder {
        ImageView image; TextView text; ConstraintLayout background;
        public QRViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.recycler_image_item);
            text = itemView.findViewById(R.id.recycler_text_item);
            background = itemView.findViewById(R.id.recycler_item_background);
        }
    }

    @NonNull
    @Override
    public QRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.recycler_text_photo, parent, false);
        return new QRViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QRViewHolder holder, int position) {
        if(position < data.size())
            holder.text.setText(data.get(position));
        if(position < photos.size())
            holder.image.setImageBitmap(photos.get(position));

        if(listener != null)
            holder.background.setOnClickListener(view -> {
            Log.d("Adapter says:", "Clicked on item " + position);
            listener.OnClickListItem(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
