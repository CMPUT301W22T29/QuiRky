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
public class AdapterTextSubtext extends RecyclerView.Adapter<AdapterTextSubtext.QRViewHolder> {
    private ArrayList<String> data;
    private ArrayList<String> subdata;
    private final Context ct;
    private final RecyclerClickerListener listener;

    /**
     * Constructor for the adapter.
     * @param data The array of string objects
     * @param subdata A list of arrays to display in the bottom-right corner of the recycler item
     * @param ct The calling context/activity
     * @param listener An optional listener to be called when a list item is clicked on
     */
    public AdapterTextSubtext(ArrayList<String> data, ArrayList<String> subdata, Context ct, RecyclerClickerListener listener) {
        this.data = data;
        this.subdata = subdata;
        this.ct = ct;
        this.listener = listener;
    }

    /**
     * Constructor without a listener. Objects in the recycler will not be clickable as a result.
     */
    public AdapterTextSubtext(ArrayList<String> data, ArrayList<String> subdata, Context ct) {
        this.data = data;
        this.subdata = subdata;
        this.ct = ct;
        this.listener = null;
    }

    public LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(ct, LinearLayoutManager.VERTICAL, false);
    }

    public void sortData(ArrayList<String> data, ArrayList<String> subdata) {
        this.data = data;
        this.subdata = subdata;
    }

    /**
     * Helper subclass. It does things that help the other things do the things.
     * Hey I don't know how it works! I just know how to do it.
     */
    public static class QRViewHolder extends RecyclerView.ViewHolder {
        TextView text, subtext;
        ConstraintLayout background;
        public QRViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.recycler_main_text);
            subtext = itemView.findViewById(R.id.recycler_subtext);
            background = itemView.findViewById(R.id.recycler_item_background);
        }
    }

    @NonNull
    @Override
    public QRViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.recycler_text_subtext, parent, false);
        return new QRViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QRViewHolder holder, int position) {

        holder.text.setText(data.get(position));

        if(position < subdata.size())
            holder.subtext.setText( subdata.get( position ));

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
