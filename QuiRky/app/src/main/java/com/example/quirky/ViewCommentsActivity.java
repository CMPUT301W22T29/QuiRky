

package com.example.quirky;


import android.content.Intent;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


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
        setContentView(R.layout.activity_comment);

        // Get the QRCodeID that got commented on so that the qrcode database collection can be opened in the database
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        
//        String message = intent.getExtras().getParcelable("comments"); // hopefully this works

        System.out.println("It worked."+message);

        String comment = "whdfsk"; // sample comment
        Map<String, String> a = new HashMap<String, String>(); // The comment must be stored with a random key value pair, but doesn't really matter what the name is

        a.put("comment", comment); // Key matches the Field that you want to store it in.

//        DM.setCollection("comments"); // Opens the collection Comments


        Comment sampleComment = new Comment(comment, "Raymart", new Date());
        String id = "12345";

        // (Comment c, String id)
//        DM.writeComment(sampleComment, id); // Writes the comment into the specific qr code with the specific id.

        // Need to extract comment values from database.

        commentDataList = new ArrayList<>();

        String[] sampleComments = {"cool", "nice", "rad"};
        String[] sampleNames = {"Akhmetov", "GoldenBear", "Panda"};


        for (int i = 0; i < sampleComments.length; i++) {

            Comment sampleComment1 = new Comment(sampleComments[i], sampleNames[i], new Date());
            commentDataList.add(sampleComment1);

            // I am hoping that writeComment() doesn't overwrite any of the comments that i am passing.
//            DM.writeComment(sampleComment1, Integer.toString(i)); // This should kind of work.
        }

        // Recycler Adapter instead of list Adapter. Recycler List instead of

        commentAdapter = new CommentList(this, commentDataList);
        commentList = (RecyclerView) findViewById(R.id.recycleListView_user_comment);
        commentList.setAdapter(commentAdapter);

        Save = (Button) findViewById(R.id.button_save);
        Cancel = (Button) findViewById(R.id.button_cancel);

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

    /**
     * When clicked the comment, uname and time will be saved to the database
     * under a document field of a specific QR Code
     *
     * //// Not Finished
     */
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
