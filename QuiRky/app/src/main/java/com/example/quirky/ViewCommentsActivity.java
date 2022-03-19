

package com.example.quirky;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.Locale;
import java.util.Map;

/**
 * Comment Activity page which allows users to scroll through comments of a
 * Specific QR code and also add commments.
 *
 * @author Raymart Bless C. Datuin
 * */
public class ViewCommentsActivity extends AppCompatActivity {
    private Button Save;
    private Button Cancel;

    RecyclerView commentList;
    ArrayList<Comment> commentDataList;
    CommentList commentAdapter;
    DatabaseController DM;
    Context ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comment);
        ImageView QRCodeImage;
        QRCodeImage = findViewById(R.id.QRCodeImmage_Comment);
//      QRCodeImage.setImageResource(0); // This is going to have to be something in order to view Image.

        Intent intent = getIntent();

        ct = getApplicationContext();
        DM = new DatabaseController(FirebaseFirestore.getInstance(), ct);

        // Has to be getting it from Fragment
        String qrCodeId = intent.getStringExtra(MainActivity.EXTRA_MESSAGE); // Needs to be changed
        commentDataList = new ArrayList<>();


        // Reading Comments from the data doesn't really work well rn.
        // But the intention is that it will return an array of comments.
        String message = "";
        commentDataList = readComments(message);

        commentAdapter = new CommentList(this, commentDataList);
        commentList = (RecyclerView) findViewById(R.id.recycleListView_user_comment);
        commentList.setAdapter(commentAdapter);
        commentList.setLayoutManager(new LinearLayoutManager(this));

        Save = (Button) findViewById(R.id.button_save);
        Cancel = (Button) findViewById(R.id.button_cancel);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(message);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }

    // Mock readcomments List until Jonathen has finished the readComments implementation
    // on the DatabaseManager
    public ArrayList<Comment> readComments(String mockID) {
        String[] sampleComments = {"cool", "nice", "rad"};
        String[] sampleNames = {"Akhmetov", "GoldenBear", "Panda"};

        for (int i = 0; i < sampleComments.length; i++) {

            Comment sampleComment1 = new Comment(sampleComments[i], sampleNames[i], new Date());
            commentDataList.add(sampleComment1);
        }
        return commentDataList;
    }

    /**
     * When clicked the comment, uname and time will be saved to the database
     * under a document field of a specific QR Code
     *
     */
    public void save(String qrCodeID) {
        String uName; // Should be retrieved from profile. How would I retrieve the User Name?
        // MemoryManager will return the userName but not rn. It will get fixed in the future.
        uName = "John Doe"; // Default Variable for now

        EditText editTextComment = (EditText) findViewById(R.id.editText_comment);
        String content = editTextComment.getText().toString();
        Comment comment = new Comment(content, uName, new Date());
        DM.addComment(comment, qrCodeID);
        commentAdapter.notifyDataSetChanged();
        finish(); // Prolly don't have to finish right away
    }


    /**
     * This would return the String version of the passed time.
     * @param time - given time
     * @return String version of the date in the format "dd-MM-yyyy"
     */
    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;
    }

    /**
     * Goes back to previous Activity
     */
    public void cancel() {
        finish();
    }
}
