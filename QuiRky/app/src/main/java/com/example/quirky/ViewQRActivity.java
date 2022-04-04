package com.example.quirky;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

/**
 * This class is of the Activity for when you're clicking on one of the listed items in "Your QR Codes" or
 * "nearby QR code" in "MAP" Activity, as seen on the Project Part 2 Miro.
 * */
public class ViewQRActivity extends AppCompatActivity implements ViewQRFragmentListener {

    private final String TAG = "ViewQRActivity says";
    ImageView image;
    TextView scoreText;
    Fragment buttonsFrag, playersFrag;
    Bitmap photo;
    ProgressBar loading;

    DatabaseController dc;
    ArrayList<String> players;

    Intent i;
    String qrid;
    QRCode qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qr);

        loading = findViewById(R.id.view_qr_progress_bar);
        dc = new DatabaseController(this);

        i = getIntent();
        qrid = i.getStringExtra("code");

        dc.readQRCode(qrid).addOnCompleteListener(task -> {
            qr = dc.getQRCode(task);
            doneReadingQRCode();
        });

    }

    private void doneReadingQRCode() {
        image = findViewById(R.id.imageView2);
        scoreText = findViewById(R.id.text_showScore);

        buttonsFrag = new ViewQRButtonsFragment();
        playersFrag = new ViewQRScannersFragment();

        scoreText.setText(String.valueOf(qr.getScore()));

        photo = BitmapFactory.decodeResource(getResources(), R.drawable.temp);
        image.setImageBitmap(photo);

        dc.readQRCodeUserData(qrid).addOnCompleteListener(task -> {
            players = dc.getQRCodeScanners(task);
            changeFragment(buttonsFrag);
            loading.setVisibility(View.INVISIBLE);
        });
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

    @Override
    public ArrayList<String> getPlayers() {
        Log.d(TAG, "players is:\t" + players);
        return players;
    }

    /**
     * Opens the Comment Activity. This will be called when the user clicks the Comments button in the fragment
     */
    @Override
    public void commentsButton() {
        Intent intent = new Intent(this, ViewCommentsActivity.class);
        intent.putExtra("qrId", qrid);
        startActivity(intent);
    }

    @Override
    public void deleteButton() {
        MemoryController mc = new MemoryController(this);
        Profile p = mc.read();

        p.removeScanned(qrid);
        mc.write(p);
        dc.writeProfile(p);

        Toast.makeText(this, "Removed from your scanned codes!", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, StartingPageActivity.class);
        startActivity(i);
    }
}