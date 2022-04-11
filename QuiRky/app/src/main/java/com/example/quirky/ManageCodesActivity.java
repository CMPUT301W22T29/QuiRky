/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Activity to view a list of QRCodes a Profile has scanned
 */
public class ManageCodesActivity extends AppCompatActivity {
    private final String TAG = "ManageCodesActivity says";

    private ToggleButton arrangementOrder;
    private RecyclerView qr_list;

    private QRAdapter QRCodeAdapter;
    private ArrayList<String> codes;
    private ArrayList<String> points;
    private RecyclerClickerListener recyclerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_codes);

        Profile p = (Profile) getIntent().getSerializableExtra("profile");
        if(p == null)
            ExitWithError();

        TextView title = findViewById(R.id.manage_codes_title);
        String text = p.getUname() + "'s Codes";
        title.setText(text);

        codes = p.getScanned();
        points = new ArrayList<>();
        for(String id : codes) {
            String score = String.valueOf( QRCodeController.score(id) );
            points.add(score);
        }

        arrangementOrder = findViewById(R.id.toggleButton);
        qr_list = findViewById(R.id.qr_list);

        recyclerListener = new RecyclerClickerListener(){
            @Override
            public void OnClickListItem(int position){
                startViewQRActivity(position);
            }
        };
        QRCodeAdapter = new QRAdapter(points, new ArrayList<>(),this, recyclerListener);
        qr_list.setAdapter(QRCodeAdapter);
        qr_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        arrangementOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setOrder(isChecked);
            }
        });
    }

    /**
     * Sets the order to display the QRCodes
     * @param isChecked The state of the ordering switch
     */
    private void setOrder(boolean isChecked) {
        Comparator<String> c = new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return (Integer.valueOf(s) - Integer.valueOf(t1));
            }
        };
        Comparator<String> d = new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return Integer.valueOf(t1) - Integer.valueOf(s);
            }
        };

        if(isChecked) {
            points.sort(c);
            QRCodeAdapter.notifyDataSetChanged();
        } else {
            points.sort(d);
            QRCodeAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Start the activity to view a QRCode. Determines which QRCode to view with the given item the user clicked on
     * @param position The position in the recycler that the user clicked on
     */
    private void startViewQRActivity(int position) {
        String id = codes.get(position);
        ListeningList<QRCode> readResult = new ListeningList<>();
        readResult.setOnAddListener(new OnAddListener<QRCode>() {
            @Override
            public void onAdd(ListeningList<QRCode> listeningList) {
                QRCode qr = listeningList.get(0);
                Intent i = new Intent(getApplicationContext(), ViewQRActivity.class);
                i.putExtra("qr", qr);
                startActivity(i);
            }
        });
        DatabaseController dc = new DatabaseController();
        dc.readQRCode(id, readResult);
    }

    /**
     * Method called when data is passed to this activity incorrectly, or when there is an issue reading the data from FireStore.
     * Makes a toast and then finishes the activity.
     */
    private void ExitWithError() {
        Toast.makeText(this, "User was not found!", Toast.LENGTH_SHORT).show();
        finish();
    }
}