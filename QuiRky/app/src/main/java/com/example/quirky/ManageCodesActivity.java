/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.gms.common.util.Strings;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quirky.databinding.ActivityManageCodesBinding;

import java.util.ArrayList;

public class ManageCodesActivity extends AppCompatActivity {
    ToggleButton arrangementOrder;
    RecyclerView qr_list;
    QRAdapter QRCodeAdapter;
    ArrayList<QRCode> QRCodeDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_codes);
        arrangementOrder = findViewById(R.id.toggleButton);
        qr_list = findViewById(R.id.qr_list);
        ArrayList<String> strings = new ArrayList<>();
        ArrayList<Drawable> photos = new ArrayList<>();
        QRCodeAdapter = new QRAdapter(photos,strings,this);
        QRCodeDataList = new ArrayList<>();
        qr_list.setAdapter(QRCodeAdapter);

        arrangementOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled, display lowest to highest
                } else {
                    // The toggle is disabled, display highest to lowest
                }
            }
        });


    }
}