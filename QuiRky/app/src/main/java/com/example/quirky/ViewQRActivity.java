package com.example.quirky;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * This class is of the Activity for when you're clicking on one of the listed items in "Your QR Codes" or
 * "nearby QR code" in "MAP" Activity, as seen on the Project Part 2 Miro.
 * */
public class ViewQRActivity extends AppCompatActivity implements ViewQRFragmentListener {

    ImageView image;
    TextView scoreText;
    Fragment buttonsFrag, playersFrag;
    Bitmap photo;

    Intent i;
    QRCode qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qr);

        i = getIntent();
        qr = i.getParcelableExtra("code");

        image = findViewById(R.id.imageView2);
        scoreText = findViewById(R.id.text_showScore);

        buttonsFrag = new ViewQRButtonsFragment();
        playersFrag = new ViewQRScannersFragment();

        scoreText.setText(String.valueOf(qr.getScore()));

        photo = BitmapFactory.decodeResource(getResources(), R.drawable.temp);
        image.setImageBitmap(photo);

        changeFragment(buttonsFrag);
    }

    /**
     * Changes the fragment in the FrameLayout
     * @param frag The fragment to place in the layout
     */
    @Override
    public void changeFragment(Fragment frag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.view_qr_frame, frag);
        ft.commit();
    }

    /**
     * Opens the Comment Activity. This will be called when the user clicks the Comments button in the fragment
     */
    @Override
    public void commentsButton() {
        Intent intent = new Intent(this, ViewCommentsActivity.class);

        String message = "Sample QR Code ID"; // This needs to be a specific QR code Id.
        intent.putExtra("comment", message);

        startActivity(intent);
    }

    @Override
    public void privateButton() {
        // Reconsider the Set Private button in this UI. Why does it exists? What does it do?
        // Perhaps it removes the GeoLocation and Image associated with the displayed QRCode?
    }

    @Override
    public void deleteButton() {
        // Delete button will remove the QRCode from the list of QRCodes the player has scanned.
        // It will also remove the GeoLocation and Image from the database
        // This button should also probably start up another activity, like StartingPageActivity
    }
}