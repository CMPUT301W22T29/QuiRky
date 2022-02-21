package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText text1, text2;
    FirebaseFirestore db;
    String data1, data2;
    CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Testing");

        text1 = findViewById(R.id.input1);
        text2 = findViewById(R.id.input2);
        button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            if(text1.length() == 0 || text2.length() == 0) {
                data1 = "a"; data2 = "b";
            } else {
                data1 = text1.getText().toString();
                data2 = text2.getText().toString();
            }

            HashMap<String, String> data_table = new HashMap<>();
            data_table.put("data1", data1);

            collectionReference
                    .document(data1)
                    .set(data_table)
                    .addOnSuccessListener(unused -> Log.d("Sample", "Data has been successfully added"))
                    .addOnFailureListener(e -> Log.d("Sample", "Data writing failed!" + e.toString()));
        });
    }
}