package com.example.quirky;

import static com.google.firebase.firestore.FieldValue.delete;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * This class is of the Activity for when you're clicking on one of the listed items in "Your QR Codes" or
 * "nearby QR code" in "MAP" Activity, as seen on the Project Part 2 Miro.
 * */
public class QRCodeInfo {
    private Button Comment;
    private Button ElseQRCode;
    private Button SetPrivate;
    private Button Delete;
    private Button Location; // But may not be a button because the location description has to
    // change depending on where the location is.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_info);

        Comment = findViewById(R.id.button_comments);
        ElseQRCode = findViewById(R.id.button_who_else_has_this_code);
        SetPrivate = findViewById(R.id.button_set_private);
        Delete = findViewById(R.id.button_delete);
//        Location = findViewById(R.id.button_location);




        // initialized intent
        Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment();
            }
        });

        ElseQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                elseQRCode();
            }
        });

        SetPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPrivate();
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location();
            }
        });
    }

    public void comment() {
        Intent intent = new Intent(this, CommentActivity.class);
        startActivity(intent);
    }

    // Planning on expanding
    public void elseQRCode() {     }
    public void setPrivate() {     }
    public void delete() {     }
    public void location() {     }

}
