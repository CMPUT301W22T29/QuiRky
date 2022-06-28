/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quirky.AdapterTextSubtext;
import com.example.quirky.ListeningList;
import com.example.quirky.R;
import com.example.quirky.RecyclerClickerListener;
import com.example.quirky.controllers.DatabaseController;
import com.example.quirky.models.Profile;
import com.example.quirky.models.QRCode;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Activity to view a list of QRCodes a Profile has scanned
 */
public class ManageCodesActivity extends AppCompatActivity {
    private final String TAG = "ManageCodesActivity says";

    private ListeningList<QRCode> codes;
    private ArrayList<String> content;
    private ArrayList<String> points;

    private DatabaseController dc;

    private AdapterTextSubtext adapter;
    private final RecyclerClickerListener recyclerListener = this::startViewQRActivity;

    private ToggleButton sortButton;


    private int codes_read_counter = 0; // This counts the number of codes read from the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_codes);

        Profile p = (Profile) getIntent().getSerializableExtra("profile");
        if(p == null)
            ExitWithError();

        String text = p.getUname() + "'s Codes";

        TextView title = findViewById(R.id.manage_codes_title);
        title.setText(text);

        sortButton = findViewById(R.id.sort_by_score_toggle);
        sortButton.setTextOff("Low to High");
        sortButton.setTextOn("High to Low");

        ArrayList<String> ids = p.getScanned();
        codes = new ListeningList<>();
        codes.setOnAddListener(listeningList -> {
            // Each time a QRCode is read from the database, this listener is called
            // However, we only want to call doneReading() once. Therefore, a counter is used
            codes_read_counter++;
            if(codes_read_counter == ids.size())
                doneReading();
        });

        dc = new DatabaseController();
        for(String id : ids) {
            Log.d(TAG, "User's QRCode ID: |" + id + "|");
            dc.readQRCode(id, codes);
        }
    }

    /**
     * Called once the activity is done reading all of the QRCodes from the database.
     * Finishes setting up the views, including the recycler view.
     */
    private void doneReading() {
        RecyclerView qr_list = findViewById(R.id.qr_list);

        content = new ArrayList<>();
        points = new ArrayList<>();


        adapter = new AdapterTextSubtext(content, points, this, recyclerListener);
        qr_list.setAdapter(adapter);
        qr_list.setLayoutManager(adapter.getLayoutManager());

        sortButton.setOnClickListener(v -> sort());
        sort();
    }

    /**
     * Start the activity to view a QRCode. Determines which QRCode to view with the given item the user clicked on
     * @param position The position in the recycler that the user clicked on
     */
    private void startViewQRActivity(int position) {
        QRCode qr = codes.get(position);
        Intent i = new Intent(this, ViewQRActivity.class);
        i.putExtra("qr", qr);
        startActivity(i);
    }

    /**
     * Method called when data is passed to this activity incorrectly, or when there is an issue reading the data from FireStore.
     * Makes a toast and then finishes the activity.
     */
    private void ExitWithError() {
        Toast.makeText(this, "User was not found!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void sort() {
        boolean isChecked = sortButton.isChecked();

        Comparator<QRCode> sorter;

        if(isChecked) {
            sorter = (code1, code2) -> (code2.getScore() - code1.getScore()); // Android studio would like to simplify this statement, but I have chosen not to.
        } else {
            sorter = (code1, code2) -> (code1.getScore() - code2.getScore());
        }

        codes.sort(sorter);
        content.clear();
        points.clear();
        for(QRCode qr : codes) {
            if(qr == null) continue;
            content.add(qr.getContent());
            points.add(qr.getScore() + " pts");
        }

        adapter.sortData(content, points);
        adapter.notifyDataSetChanged();
    }
}
