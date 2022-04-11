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

/**
 * Activity to delete players from the database
 */
public class delete_Players extends AppCompatActivity {

    private RecyclerView playersList;
    private QRAdapter adapter;
    private RecyclerClickerListener listener;

    private LinearLayout confirmBox;
    private Button yes;
    private Button no;
    private DatabaseController dc;

    private ArrayList<String> Usernames;
    private ListeningList<Profile> readResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_players);

        dc = new DatabaseController();
        Usernames = new ArrayList<>();


        playersList = findViewById(R.id.players);
        confirmBox = findViewById(R.id.linearLayout1);
        yes = findViewById(R.id.confirm);
        no = findViewById(R.id.cancel);

        listener = new RecyclerClickerListener(){
            @Override
            public void OnClickListItem(int position) {
                confirmBox.setVisibility(View.VISIBLE);

                yes.setOnClickListener(view -> delete(position));

                no.setOnClickListener(view -> confirmBox.setVisibility(View.INVISIBLE) );
            }
        };

        readResults = new ListeningList<>();
        readResults.setOnAddListener(new OnAddListener<Profile>() {
            @Override
            public void onAdd(ListeningList<Profile> listeningList) {
                doneReading();
            }
        });

        dc.readUsers("", readResults);
    }

    /**
     * Called once DatabaseController is done reading all the users from Firestore. Finishes setting up the Recycler.
     */
    private void doneReading() {
        for(int i = 0; i < readResults.size(); i ++){
            Usernames.add(readResults.get(i).getUname());
        }
        adapter = new QRAdapter(Usernames, new ArrayList<>(), this, listener);

        playersList.setAdapter(adapter);

        playersList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void delete(int position) {
        dc.deleteProfile(Usernames.get(position));
        Usernames.remove(position);
        confirmBox.setVisibility(View.INVISIBLE);
        adapter.notifyDataSetChanged();
    }
}