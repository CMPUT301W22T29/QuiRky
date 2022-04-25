/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quirky.AdapterComment;
import com.example.quirky.AdapterPhoto;
import com.example.quirky.AdapterText;
import com.example.quirky.ListeningList;
import com.example.quirky.R;
import com.example.quirky.controllers.DatabaseController;
import com.example.quirky.controllers.MemoryController;
import com.example.quirky.models.Comment;
import com.example.quirky.models.Profile;
import com.example.quirky.models.QRCode;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * Activity to view a QRCode. Holds two fragments: one fragment to see the QRCode location, & start up the comments activity
 * Another fragment to see all other players who have scanned the QRCode
 * */
public class ViewQRActivity extends AppCompatActivity {

    private final String TAG = "ViewQRActivity says";

    DatabaseController dc;
    ListeningList<Bitmap> photos;

    ArrayList<Comment> comments;
    AdapterComment commentAdapter;

    Button deleteButton, saveButton;
    EditText commentText;

    Intent i;
    QRCode qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qr);

        dc = new DatabaseController();

        i = getIntent();
        qr = i.getParcelableExtra("qr");
        if(qr == null)
            ExitWithError();

        // Set the titles list
        ArrayList<String> titles = qr.getTitles();
        titles.add("Player written title");
        RecyclerView titlesList = findViewById(R.id.player_titles_text);
        AdapterText titlesAdapter = new AdapterText(titles, this);
        titlesList.setAdapter(titlesAdapter);
        titlesList.setLayoutManager( titlesAdapter.getLayoutManager() );

        // Set the scanners list
        ArrayList<String> players = qr.getScanners();
        players.add("user1");
        RecyclerView playersList = findViewById(R.id.scanners_list);
        AdapterText scannersAdapter = new AdapterText(players, this);
        playersList.setAdapter(scannersAdapter);
        playersList.setLayoutManager( scannersAdapter.getLayoutManager() );

        // Set the comments list
        comments = qr.getComments();
        comments.add( new Comment("words, bruh", "user1"));
        RecyclerView commentsList = findViewById(R.id.comments_list);
        commentAdapter = new AdapterComment(this, comments);
        commentsList.setAdapter( commentAdapter );
        commentsList.setLayoutManager( commentAdapter.getLayoutManager() );

        // Set the score text
        String temp;
        temp = qr.getScore() + " pts.";
        TextView score = findViewById(R.id.text_showScore);
        score.setText(temp);

        // Set the content text
        TextView contentText = findViewById(R.id.qrcode_content_text);
        contentText.setText( qr.getContent() );

        temp = qr.getScanners().size() + " Players have found this code";
        TextView scannersText = findViewById(R.id.x_players_text);
        scannersText.setText(temp);

        photos = new ListeningList<>();
        photos.setOnAddListener(listeningList -> {
            Toast.makeText(this, "Done reading", Toast.LENGTH_SHORT).show();
            setPhotos();
        });
        dc.readPhotos( qr.getId(), photos, 3);

        deleteButton = findViewById(R.id.remove_from_acc_button);
        saveButton = findViewById(R.id.save_comment_button);
        commentText = findViewById(R.id.comment_input_field);

        deleteButton.setOnClickListener(view -> RemoveFromAcc());
        saveButton.setOnClickListener(view -> writeComment());
    }



    /**
     * Called once the DatabaseController is done reading the images from Firebase. Sets up the recyclerview with images
     */
    public void setPhotos() {
        RecyclerView photo_list = findViewById(R.id.code_photo_list);
        AdapterPhoto adapterPhoto = new AdapterPhoto(photos, this);
        photo_list.setAdapter(adapterPhoto);
        photo_list.setLayoutManager( adapterPhoto.getLayoutManager() );
        ProgressBar bar = findViewById(R.id.progressBar3);
        bar.setVisibility(View.GONE);
    }

    private void writeComment() {
        MemoryController mc = new MemoryController(this);
        String uName = mc.readUser();

        String content = commentText.getText().toString();

        Comment comment = new Comment(content, uName);

        qr.addComment(comment);
        dc.writeQRCode(qr);

        commentAdapter.notifyItemInserted( comments.size() - 1);
        Toast.makeText(this, "Comment Saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Deletes the QRCode from the list of codes the user has scanned. Exits the activity.
     */
    public void RemoveFromAcc() {
        MemoryController mc = new MemoryController(this);
        Profile p = mc.read();

        if(p.removeScanned(qr.getId())) {
            mc.writeUser(p);
            dc.writeProfile(p);

            qr.removeScanner(p.getUname());
            dc.writeQRCode(qr);

            Toast.makeText(this, "Removed from your scanned codes!", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, HubActivity.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "You did not have that code anyways!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method called when data is passed to this activity incorrectly, or when there is an issue reading the data from FireStore.
     * Makes a toast and then finishes the activity.
     */
    private void ExitWithError() {
        Toast.makeText(this, "QRCode was passed incorrectly, or not found in FireStore!", Toast.LENGTH_SHORT).show();
        finish();
    }
}