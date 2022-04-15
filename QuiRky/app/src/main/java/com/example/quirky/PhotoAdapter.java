/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * An Adapter for RecyclerViews that takes in a list of drawable objects
 * and maps them to the layout file for the QRCode adapter in qr_recycler_items.xml
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.QRViewHolder> {
    private ArrayList<Drawable> photos;
    Context ct;
    RecyclerClickerListener listener;

    /**
     * Constructor with an item listener.
     * @param photos The array of drawable objects
     * @param ct Context because it does things that helps the adapter do stuff. Duh.
     * @param listener An instance of a recycler clicker listener that is called when a photo is clicked on
     */
    public PhotoAdapter(ArrayList<Drawable> photos, Context ct, RecyclerClickerListener listener) {
        this.photos = photos;
        this.ct = ct;
        this.listener = listener;
    }

    /**
     * Constructor without a listener. If this constructor is used, the photos will not be clickable
     * @param photos An array of drawable objects
     * @param ct Context
     */
    public PhotoAdapter(ArrayList<Drawable> photos, Context ct) {
        this.photos = photos;
        this.ct = ct;
        this.listener = null;
    }

    /**
     * Helper subclass. It does things that help the other things do the things.
     * Hey I don't know how it works! I just know how to do it.
     */
    public static class QRViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public QRViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.recycler_image_item);
        }
    }

    @NonNull
    @Override
    /**
     * Method that does the uhhh, inflating, and the uhhhh, holding, of uhh, the views..? Nailed it.
     */
    public QRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.qr_recycler_items, parent, false);
        return new QRViewHolder(view);
    }

    @Override
    /**
     * Obviously this method is the binder. Like, who wouldn't understand that?
     */
    public void onBindViewHolder(@NonNull QRViewHolder holder, int position) {
            holder.image.setImageDrawable(photos.get(position));

        if(listener != null)
            holder.image.setOnClickListener(view -> {
                Log.d("Adapter says:", "Clicked on item " + position);
                listener.OnClickListItem(holder.getAdapterPosition());
            });
    }

    @Override
    /**
     * The most advanced method in the class, I understand why you are looking at the javadoc for it.
     * This, intense, advanced, computational algorithm returns the number of elements in the adapter.
     * I know. I don't get it either.
     */
    public int getItemCount() {
        return photos.size();
    }
}