/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quirky.ListeningList;
import com.example.quirky.OnAddListener;
import com.example.quirky.AdapterText;
import com.example.quirky.models.Profile;
import com.example.quirky.models.QRCode;
import com.example.quirky.controllers.QRCodeController;
import com.example.quirky.R;
import com.example.quirky.RecyclerClickerListener;
import com.example.quirky.controllers.DatabaseController;
import com.google.rpc.Code;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Activity to view a list of QRCodes a Profile has scanned
 */
public class ManageCodesActivity extends AppCompatActivity {
    private final String TAG = "ManageCodesActivity says";

    private ListeningList<QRCode> codes;
    private DatabaseController dc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_codes);

        Profile p = (Profile) getIntent().getSerializableExtra("profile");
        if(p == null)
            ExitWithError();

        dc = new DatabaseController();

        TextView title = findViewById(R.id.manage_codes_title);
        String text = p.getUname() + "'s Codes";
        title.setText(text);

        ArrayList<String> ids = p.getScanned();
        codes = new ListeningList<>();
        codes.setOnAddListener(listeningList -> {
            if(listeningList.size() == ids.size())
                doneReading();
        });

        for(String id : ids) {
            dc.readQRCode(id, codes);
        }
    }

    /**
     * Called once the activity is done reading all of the QRCodes from the database.
     * Finishes setting up the views, including the recycler view.
     */
    private void doneReading() {
        RecyclerView qr_list = findViewById(R.id.qr_list);

        ArrayList<String> CodeData = new ArrayList<>();
        for(QRCode qr : codes) {
            CodeData.add(qr.getContent());
        }

        RecyclerClickerListener recyclerListener = this::startViewQRActivity;   // This is an onClickListener for the Recycler's Items, it looks weird because Android Studio wants me to simplify it.

        AdapterText QRCodeAdapter = new AdapterText(CodeData, this, recyclerListener);
        qr_list.setAdapter(QRCodeAdapter);
        qr_list.setLayoutManager(QRCodeAdapter.getLayoutManager());
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
}