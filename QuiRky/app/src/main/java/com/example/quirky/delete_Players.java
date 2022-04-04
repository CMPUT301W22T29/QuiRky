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
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
/**
 * This class deletes player and it is one of the functionalities of the Owner
 */
public class delete_Players extends AppCompatActivity {
    private RecyclerView set_Player_list;
    private QRAdapter set_PlayerAdapter;
    private RecyclerClickerListener set_recyclerListener;
    private LinearLayout linearLayout1;
    private Button yes;
    private Button no;
    private DatabaseController dc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_players);
        dc = new DatabaseController(this);
        set_Player_list = findViewById(R.id.players);
        linearLayout1 = findViewById(R.id.linearLayout1);
        yes = findViewById(R.id.confirm);
        no = findViewById(R.id.cancel);
        ArrayList<Drawable> photos = new ArrayList<>();
        ArrayList<String> Usernames = new ArrayList<>();
        dc.readAllProfiles().addOnCompleteListener(task -> {
            ArrayList<Profile> profiles = new ArrayList<>();
            profiles = dc.getAllProfiles(task);
            for(int i = 0; i < profiles.size(); i ++){
                Usernames.add(profiles.get(i).getUname());
            }
            set_PlayerAdapter = new QRAdapter(Usernames, photos, this, set_recyclerListener);

            set_Player_list.setAdapter(set_PlayerAdapter);

            set_Player_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        });
        set_recyclerListener = new RecyclerClickerListener(){
            @Override
            public void OnClickListItem(int position) {
                linearLayout1.setVisibility(View.VISIBLE);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dc.deleteProfile(Usernames.get(position));
                        Usernames.remove(position);
                        linearLayout1.setVisibility(View.INVISIBLE);
                        set_PlayerAdapter.notifyDataSetChanged();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        linearLayout1.setVisibility(View.INVISIBLE);
                        set_PlayerAdapter.notifyDataSetChanged();
                    }
                });
            }
        };

    }


}