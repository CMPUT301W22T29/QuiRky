/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;

public class ManageCodesActivity extends AppCompatActivity {
    private ToggleButton arrangementOrder;
    private RecyclerView qr_list;

    private QRAdapter QRCodeAdapter;
    private ArrayList<String> codes;
    private ArrayList<String> points;
    private RecyclerClickerListener recyclerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_codes);

        Profile p = (Profile) getIntent().getSerializableExtra("profile");
        codes = p.getScanned();

        arrangementOrder = findViewById(R.id.toggleButton);
        qr_list = findViewById(R.id.qr_list);
        
        recyclerListener = new RecyclerClickerListener(){
            @Override
            public void OnClickListItem(int position){
                startViewQRActivity(position);
            }
        };
        QRCodeAdapter = new QRAdapter(points, new ArrayList<>(),this, recyclerListener);
        qr_list.setAdapter(QRCodeAdapter);
        qr_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        arrangementOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setOrder(isChecked);
            }
        });
    }

    private void setOrder(boolean isChecked) {
        Comparator<String> c = new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return (Integer.valueOf(s) - Integer.valueOf(t1));
            }
        };
        Comparator<String> d = new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return Integer.valueOf(t1) - Integer.valueOf(s);
            }
        };

        if(isChecked) {
            points.sort(c);
            QRCodeAdapter.notifyDataSetChanged();
        } else {
            points.sort(d);
            QRCodeAdapter.notifyDataSetChanged();
        }
    }

    private void startViewQRActivity(int position) {
        Intent i = new Intent(this, ViewQRActivity.class);
        i.putExtra("code", codes.get(position));
        startActivity(i);
    }
}