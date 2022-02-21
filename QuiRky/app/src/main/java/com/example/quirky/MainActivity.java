package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    DatabaseManager dm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, Object> user = new HashMap<>();
        user.put("first", "JJ");
        user.put("last", "A.");
        user.put("year", "1887");

        dm = new DatabaseManager();

    }
}