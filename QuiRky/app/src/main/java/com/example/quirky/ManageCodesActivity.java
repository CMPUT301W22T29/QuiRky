/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.gms.common.util.Strings;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quirky.databinding.ActivityManageCodesBinding;

import java.util.ArrayList;
import java.util.Comparator;

public class ManageCodesActivity extends AppCompatActivity {
    private ToggleButton arrangementOrder;
    private RecyclerView qr_list;
    private QRAdapter QRCodeAdapter;
    private ArrayList<String> QRCodeDataList;
    private RecyclerClickerListener recyclerListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_codes);
        arrangementOrder = findViewById(R.id.toggleButton);
        qr_list = findViewById(R.id.qr_list);
        ArrayList<String> points = new ArrayList<>();
        ArrayList<Drawable> photos = new ArrayList<>();
        Intent intent = new Intent(this, ViewQRActivity.class);

        recyclerListener = new RecyclerClickerListener(){
            @Override
            public void OnClickListItem(int position){
                intent.putExtra("code",position);
                startActivity(intent);
            }
        };
        /*/points.add("test1");
        points.add("test1");
        points.add("test1");
        points.add("test1");
        */
        QRCode qr1 = new QRCode("test1");
        QRCode qr2 = new QRCode("test2");
        QRCode qr3 = new QRCode("test3");

        points.add(String.valueOf(qr1.getScore()));
        points.add(String.valueOf(qr2.getScore()));
        points.add(String.valueOf(qr3.getScore()));

        QRCodeAdapter = new QRAdapter(points, photos,this, recyclerListener);

        QRCodeDataList = new ArrayList<>();

        qr_list.setAdapter(QRCodeAdapter);

        qr_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        arrangementOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    // The toggle is enabled, display lowest to highest
                    Log.d("is working","lowest");
                    points.sort(new Comparator<String>() {
                        @Override
                        public int compare(String s, String t1) {
                            return (Integer.valueOf(s) - Integer.valueOf(t1));
                        }
                    });
                    QRCodeAdapter.notifyDataSetChanged();
                } else {
                    // The toggle is disabled, display highest to lowest
                    Log.d("is working","highest");
                    points.sort(new Comparator<String>() {
                        @Override
                        public int compare(String s, String t1) {
                            return (Integer.valueOf(t1) - Integer.valueOf(s));
                        }
                    });
                    QRCodeAdapter.notifyDataSetChanged();
                }

            }
        });


    }
}