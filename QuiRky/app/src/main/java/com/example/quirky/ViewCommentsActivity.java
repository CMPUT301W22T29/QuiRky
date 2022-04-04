

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

    private void readDone() {
        commentAdapter = new CommentList(this, commentDataList);
        commentList = (RecyclerView) findViewById(R.id.recycleListView_user_comment);
        commentList.setAdapter(commentAdapter);
        commentList.setLayoutManager(new LinearLayoutManager(this));

        Save = (Button) findViewById(R.id.button_save);
        Cancel = (Button) findViewById(R.id.button_cancel);

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
     * When clicked the comment, uname and time will be saved to the database
     * under a document field of a specific QR Code
     */
    public void save() {
        MemoryController mc = new MemoryController(this);
        String uName = mc.readUser();

        EditText editTextComment = (EditText) findViewById(R.id.editText_comment);
        String content = editTextComment.getText().toString();

        Comment comment = new Comment(content, uName, new Date());
        DM.addComment(comment, qrCodeId);

        commentDataList.add(comment);
        commentAdapter.notifyDataSetChanged();
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
}
