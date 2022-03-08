// Author: Raymart
// Contact me Through discord for any questions

package com.example.quirky;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CommentActivity extends AppCompatActivity {
    private Button Save;
    private Button Cancel;

    ListView commentList;
    ArrayList<AddComment> commentDataList;
    CommentList commentAdapter;

    FirebaseDatabase firebaseDatabase; // Suspect Code will need to review how the Firebase database works.

    // Need to retrieve the Comments that were made for that QR code.
    // Also still need to show the comments.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance(); // Need to review how the firebase database works.

        // Need to extract comment values from database.

        commentList = findViewById(R.id.listView_user_comment);
        commentDataList = new ArrayList<>();

        String[] sampleComments = {"cool", "nice", "rad"};
        String[] sampleNames = {"Akhmetov", "GoldenBear", "Panda"};

        for (int i = 0; i < sampleComments.length; i++) {
            commentDataList.add(new AddComment(sampleComments[i], sampleNames[i]));
        }

        commentAdapter = new CommentList(this, commentDataList);
        commentList.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();

        // initialized intent
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

        // How to write a comment section
    }

    // Write Intent for this, and also launch into their Activity.
    public void save() {
        // Get information of the person who wanted to comment.
        // and then store it into the firestore database
        // Then close the Activity

        // Information needed
        // - User name
        // - QR Code information - how would I retrieve the information of a QR Code?

        String uName; // Should be retrieved from profile
        uName = "John Doe"; // Default Variable for now

        String qrCodeHashValue; // Should retrieve the QR code hash value from somewhere
        qrCodeHashValue = "beauty"; // Default value for now.

        EditText editTextComment = (EditText) findViewById(R.id.editText_comment);
        String content = editTextComment.getText().toString();

        // this should be stored in the firestore
        AddComment comment = new AddComment(content, uName);

        // Suspect Code
        DatabaseReference commentReference = firebaseDatabase.getReference("Comment").child(PostKey).push();
        commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Comment Added");
                editTextComment.setText("");
            }
        }).addOnFailtureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("fail to add comment : "+e.getMessage());
            }
        });
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;
    }

    public void cancel() {
        finish(); // This should go back to the previous Activity.
    }
}
