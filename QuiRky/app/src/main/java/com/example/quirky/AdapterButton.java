/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * An Adapter for RecyclerViews that takes in a list of drawable objects
 * and maps them to the layout file for the QRCode adapter in qr_recycler_items.xml
 */
public class AdapterButton extends RecyclerView.Adapter<AdapterButton.QRViewHolder> {
    private final ArrayList<String> texts;
    private final Context ct;
    private final RecyclerClickerListener listener;

    /**
     * Constructor with an item listener.
     * @param texts The array of drawable objects
     * @param ct Context because it does things that helps the adapter do stuff. Duh.
     * @param listener An instance of a recycler clicker listener that is called when a photo is clicked on
     */
    public AdapterButton(ArrayList<String> texts, Context ct, RecyclerClickerListener listener) {
        this.texts = texts;
        this.ct = ct;
        this.listener = listener;
    }

    /**
     * Constructor without a listener. If this constructor is used, the photos will not be clickable
     * @param texts An array of drawable objects
     * @param ct Context
     */
    public AdapterButton(ArrayList<String> texts, Context ct) {
        this.texts = texts;
        this.ct = ct;
        this.listener = null;
    }

    public LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(ct, LinearLayoutManager.HORIZONTAL, false);
    }

    public static class QRViewHolder extends RecyclerView.ViewHolder {
        Button button;
        public QRViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.feature_button);
        }
    }

    @NonNull
    @Override
    public QRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.recycler_button, parent, false);
        return new QRViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QRViewHolder holder, int position) {
        holder.button.setText(texts.get(position));

        if(listener != null)
            holder.button.setOnClickListener(view -> {
                Log.d("Adapter says:", "Clicked on item " + position);
                listener.OnClickListItem(holder.getAdapterPosition());
            });
    }

    @Override
    public int getItemCount() {
        return texts.size();
    }
}