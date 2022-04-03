/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class delete_QRCodes extends AppCompatActivity{
    private RecyclerView set_QRCODE_list;
    private QRAdapter set_QRCodeAdapter;
    private RecyclerClickerListener set_recyclerListener;
    private LinearLayout linearLayout;
    private Button yes;
    private Button no;
    private DatabaseController dc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_qrcodes);
        dc = new DatabaseController(this);
        set_QRCODE_list = findViewById(R.id.qrCodes);
        linearLayout = findViewById(R.id.linearLayout);
        yes = findViewById(R.id.confirm);
        no = findViewById(R.id.cancel);
        ArrayList<String> QRIDs = new ArrayList<>();
        ArrayList<Drawable> photos = new ArrayList<>();
        dc.readAllQRCodes().addOnCompleteListener(task -> {
            ArrayList<QRCode> qrCodes = new ArrayList<>();
            qrCodes = dc.getAllQRCodes(task);
            for(int i = 0; i < qrCodes.size(); i ++){
                QRIDs.add(String.valueOf(qrCodes.get(i).getScore()));
            }
            set_QRCodeAdapter = new QRAdapter(QRIDs, photos, this, set_recyclerListener);

            set_QRCODE_list.setAdapter(set_QRCodeAdapter);

            set_QRCODE_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        });
        set_recyclerListener = new RecyclerClickerListener(){
            @Override
            public void OnClickListItem(int position) {
                linearLayout.setVisibility(View.VISIBLE);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dc.deleteQRCode(QRIDs.get(position));
                        QRIDs.remove(position);
                        linearLayout.setVisibility(View.INVISIBLE);
                        set_QRCodeAdapter.notifyDataSetChanged();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        linearLayout.setVisibility(View.INVISIBLE);
                        set_QRCodeAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
    }
}


