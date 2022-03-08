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

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {
    private Button Save;
    private Button Cancel;

    ListView commentList;
    ArrayList<Comment> commentDataList;
    CommentList commentAdapter;

//    FirebaseDatabase firebaseDatabase; // Suspect Code will need to review how the Firebase database works.

    DatabaseManager DM;

    // Need to retrieve the Comments that were made for that QR code.
    // Also still need to show the comments.

    // A message which contains the QR Code Information from the previous intent wll need to be passed. And then the
    // The QR Code information will be used to open the specific collection. So that the comment for that QR code will be
    // read. And be displayed in the list.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        firebaseDatabase = FirebaseDatabase.getInstance(); // Need to review how the firebase database works.

        String comment = "whdfsk"; // sample comment
        Map<String, String> a; // The comment must be stored with a random key value pair, but doesn't really matter what the name is
        a.put("comment", comment); // Key matches the Field that you want to store it in.

        DM.setCollection("comments"); // Opens the collection Comments

        DM.write(a, "comment1"); // Writes a into the document "comment1"

        // Need to extract comment values from database.

        commentList = findViewById(R.id.listView_user_comment);
        commentDataList = new ArrayList<>();

        String[] sampleComments = {"cool", "nice", "rad"};
        String[] sampleNames = {"Akhmetov", "GoldenBear", "Panda"};


        for (int i = 0; i < sampleComments.length; i++) {
            commentDataList.add(new Comment(sampleComments[i], sampleNames[i], new Date()));
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
        Comment comment = new Comment(content, uName, new Date());

        // Suspect Code
//        DatabaseReference commentReference = firebaseDatabase.getReference("Comment").child(PostKey).push();

//        commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                showMessage("Comment Added");
//                editTextComment.setText("");
//            }
//        }).addOnFailtureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                showMessage("fail to add comment : "+e.getMessage());
//            }
//        });
//        finish();
//    }

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
