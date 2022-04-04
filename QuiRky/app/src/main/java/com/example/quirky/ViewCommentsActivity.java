
package com.example.quirky;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Comment Activity page which allows users to scroll through comments of a
 * Specific QR code and also add comments.
 *
 * @author Raymart Bless C. Datuin
 * @author Jonathen Adsit
 * */
public class ViewCommentsActivity extends AppCompatActivity {
    private Button Save;
    private Button Cancel;

    RecyclerView commentList;
    ArrayList<Comment> commentDataList;
    CommentList commentAdapter;
    DatabaseController DM;
    String qrCodeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comment);

        ImageView QRCodeImage = findViewById(R.id.QRCodeImmage_Comment);
        QRCodeImage.setImageResource(R.drawable.temp); // This is going to have to be something in order to view Image.

        Intent intent = getIntent();
        qrCodeId = intent.getStringExtra("qrId");

        DM = new DatabaseController(this);

        DM.readComments(qrCodeId).addOnCompleteListener(task -> {
            commentDataList = DM.getComments(task);
            readDone();
        });
    }

    /**
     * Called when done reading the QRCode's comments from the database. Finishes setting up the list of comments
     */
    private void readDone() {
        commentAdapter = new CommentList(this, commentDataList);
        commentList =  findViewById(R.id.recycleListView_user_comment);
        commentList.setAdapter(commentAdapter);
        commentList.setLayoutManager(new LinearLayoutManager(this));

        Save = findViewById(R.id.button_save);
        Cancel = findViewById(R.id.button_cancel);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Save a written comment to the QRCode
     */
    public void save() {
        MemoryController mc = new MemoryController(this);
        String uName = mc.readUser();

        EditText editTextComment = (EditText) findViewById(R.id.editText_comment);
        String content = editTextComment.getText().toString();

        Comment comment = new Comment(content, uName);
        DM.addComment(comment, qrCodeId);

        commentDataList.add(comment);
        commentAdapter.notifyDataSetChanged();
    }
}
