/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class delete_QRCodes extends AppCompatActivity {

    private RecyclerView qrList;
    private QRAdapter adapter;
    private RecyclerClickerListener listener;

    private LinearLayout confirmBox;
    private Button yes;
    private Button no;
    private DatabaseController dc;

    private ArrayList<String> QRIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_qrcodes);

        dc = new DatabaseController(this);
        QRIDs = new ArrayList<>();

        qrList = findViewById(R.id.qrCodes);
        confirmBox = findViewById(R.id.linearLayout);
        yes = findViewById(R.id.confirm);
        no = findViewById(R.id.cancel);

        listener = new RecyclerClickerListener(){
            @Override
            public void OnClickListItem(int position) {
                confirmBox.setVisibility(View.VISIBLE);

                yes.setOnClickListener(view -> {
                    dc.deleteQRCode(QRIDs.get(position));
                    QRIDs.remove(position);
                    confirmBox.setVisibility(View.INVISIBLE);
                    adapter.notifyDataSetChanged();
                });

                no.setOnClickListener(view -> confirmBox.setVisibility(View.INVISIBLE));
            }
        };

        dc.readAllQRCodes().addOnCompleteListener(task -> {
            ArrayList<QRCode> qrCodes = dc.getAllQRCodes(task);
            doneReading(qrCodes);
        });


    }

    private void doneReading(ArrayList<QRCode> qrCodes) {
        for(int i = 0; i < qrCodes.size(); i ++){
            QRIDs.add(String.valueOf(qrCodes.get(i).getScore()));
        }
        adapter = new QRAdapter(QRIDs, new ArrayList<>(), this, listener);

        qrList.setAdapter(adapter);

        qrList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}


