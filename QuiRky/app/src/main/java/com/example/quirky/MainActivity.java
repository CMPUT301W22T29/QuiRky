package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    DatabaseManager dm;
    Map<String, Object> user;
    Button button;
    EditText text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = new HashMap<>();
        user.put("first", "JJ");
        user.put("last", "A.");
        user.put("year", "1887");

        dm = new DatabaseManager();
        dm.setCollection("users");

        text1 = findViewById(R.id.input1);
        button = findViewById(R.id.button);
        button.setOnClickListener(view -> writestuff());
    }

    public void writestuff() {
        dm.write(user, "user1");
        dm.read("user1");
    }
}