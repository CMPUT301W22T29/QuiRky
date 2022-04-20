/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.quirky.ListeningList;
import com.example.quirky.OnAddListener;
import com.example.quirky.AdapterTextPhoto;
import com.example.quirky.models.QRCode;
import com.example.quirky.R;
import com.example.quirky.RecyclerClickerListener;
import com.example.quirky.controllers.DatabaseController;

import java.util.ArrayList;

/**
 * Activity to delete QRCodes from the Database
 */
public class DeleteCodesActivity extends AppCompatActivity {

    private RecyclerView qrList;
    private AdapterTextPhoto adapter;
    private RecyclerClickerListener listener;

    private ConstraintLayout confirmBox;
    private Button yes;
    private Button no;
    private DatabaseController dc;

    private ArrayList<String> scores;
    private ListeningList<QRCode> readResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_qrcodes);

        dc = new DatabaseController();
        scores = new ArrayList<>();

        qrList = findViewById(R.id.qrCodes);
        confirmBox = findViewById(R.id.prompt_delete_qrcode);
        yes = findViewById(R.id.confirm_delete_qrcode);
        no = findViewById(R.id.cancel_delete_qrcode);

        listener = new RecyclerClickerListener(){
            @Override
            public void OnClickListItem(int position) {
                confirmBox.setVisibility(View.VISIBLE);

                yes.setOnClickListener(view -> delete(position));

                no.setOnClickListener(view -> confirmBox.setVisibility(View.INVISIBLE));
            }
        };

        readResults = new ListeningList<>();
        readResults.setOnAddListener(new OnAddListener<QRCode>() {
            @Override
            public void onAdd(ListeningList<QRCode> listeningList) {
                doneReading();
            }
        });

        dc.readAllQRCodes(readResults);
    }

    /**
     * Called once DatabaseController is done reading all the users from Firestore. Finishes setting up the Recycler.
     */
    private void doneReading() {
        for(int i = 0; i < readResults.size(); i ++){
            scores.add(String.valueOf(readResults.get(i).getScore()));
        }
        adapter = new AdapterTextPhoto(scores, new ArrayList<>(), this, listener);

        qrList.setAdapter(adapter);

        qrList.setLayoutManager(adapter.getLayoutManager());
    }

    private void delete(int position) {
        String id = readResults.get(position).getId();
        dc.deleteQRCode(id);
        scores.remove(position);
        confirmBox.setVisibility(View.INVISIBLE);
        adapter.notifyDataSetChanged();
    }
}


