package com.example.quirky;

//import static com.google.firebase.firestore.FieldValue.delete;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * This class is of the Activity for when you're clicking on one of the listed items in "Your QR Codes" or
 * "nearby QR code" in "MAP" Activity, as seen on the Project Part 2 Miro.
 * */
public class ViewQRActivity extends AppCompatActivity{

    private ViewQRFragment optionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qr);

        optionFragment = new ViewQRFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.qr_fragment, optionFragment).commit();
    }
}