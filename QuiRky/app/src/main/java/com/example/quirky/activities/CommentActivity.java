
/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky.activities;

import android.content.Intent;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quirky.CommentAdapter;
import com.example.quirky.R;
import com.example.quirky.controllers.DatabaseController;
import com.example.quirky.controllers.MemoryController;
import com.example.quirky.models.Comment;
import com.example.quirky.models.QRCode;

import java.util.ArrayList;

/**
 * Comment Activity page which allows users to scroll through comments of a
 * Specific QR code and also add comments.
 *
 * @author Raymart Bless C. Datuin
 * @author Jonathen Adsit
 * */
public class CommentActivity extends AppCompatActivity {
    private Button Save;
    private Button Cancel;
    private ImageView QRCodeImage;

    RecyclerView commentList;
    ArrayList<Comment> commentDataList;
    CommentAdapter commentAdapter;
    QRCode qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        qr = intent.getParcelableExtra("qr");
        if(qr == null)
            ExitWithError();

        Save = findViewById(R.id.button_save);
        Cancel = findViewById(R.id.button_cancel);
        QRCodeImage = findViewById(R.id.QRCodeImmage_Comment);
        commentList =  findViewById(R.id.recycleListView_user_comment);

        QRCodeImage.setImageResource(R.drawable.temp); // This is going to have to be something in order to view Image.

        commentDataList = qr.getComments();
        commentAdapter = new CommentAdapter(this, commentDataList);
        commentList.setAdapter(commentAdapter);
        commentList.setLayoutManager(new LinearLayoutManager(this));

        Save.setOnClickListener(view -> save());
        Cancel.setOnClickListener(view -> finish());
    }

    /**
     * Save a written comment to the QRCode
     */
    public void save() {
        DatabaseController dc = new DatabaseController();
        MemoryController mc = new MemoryController(this);
        String uName = mc.readUser();

        EditText editTextComment = (EditText) findViewById(R.id.editText_comment);
        String content = editTextComment.getText().toString();

        Comment comment = new Comment(content, uName);

        qr.addComment(comment);
        dc.writeQRCode(qr);

        commentDataList.add(comment);
        commentAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Comment Saved", Toast.LENGTH_SHORT).show();
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
