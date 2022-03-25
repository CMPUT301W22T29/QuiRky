/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PlayerSearchActivity extends AppCompatActivity implements QRAdapter.RecyclerListener {

    QRAdapter adapter;
    DatabaseController dc;
    ArrayList<Drawable> photos;
    ArrayList<String> usernames;
    RecyclerView list;

    ImageButton button;
    EditText input;
    ProgressBar circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_search);

        button = findViewById(R.id.commit_search_button);
        input = findViewById(R.id.search_input_field);
        circle = findViewById(R.id.search_progress_bar);
        list = findViewById(R.id.search_user_list);

        dc = new DatabaseController(FirebaseFirestore.getInstance(), this);
        photos = new ArrayList<>(); usernames = new ArrayList<>();
        adapter = new QRAdapter( usernames, photos,this);

        button.setOnClickListener(view -> {
            circle.setVisibility(View.VISIBLE);
            String username = input.getText().toString();
            if(username.length() == 0)
                return;
            // TODO: make a unique read function in dc that queries for similar usernames
            dc.readProfile(username).addOnCompleteListener(task -> {
                Profile p = dc.getProfile(task);
                usernames.add(p.getUname());
                adapter.notifyDataSetChanged();

                circle.setVisibility(View.INVISIBLE);
            });
        });
    }

    @Override
    public void OnClickListItem(int position) {
        String user = usernames.get(position);
        dc.readProfile(user).addOnCompleteListener(task -> {
            Profile p = dc.getProfile(task);
            assert p != null : "Something went wrong in the profile read!";
            startViewProfileActivity(p);
        });
    }

    private void startViewProfileActivity(Profile p) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("profile", p);
        startActivity(i);
    }
}