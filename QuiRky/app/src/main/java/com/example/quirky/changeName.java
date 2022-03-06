package com.example.quirky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class changeName extends AppCompatActivity {
    EditText userName;  //username that can be changed by user
    Button save,cancel; // save button to save changed username, cancel then cancel the change
    SharedPreferences sp;
    String nameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        userName = findViewById(R.id.editTextTextPersonName);
        cancel = findViewById(R.id.cancelButton);
        save = findViewById(R.id.saveButton);
        sp = getSharedPreferences("UserNamePre", Context.MODE_PRIVATE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToProfile(); //if cancel, go back to My Profile page
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameStr = userName.getText().toString();
                SharedPreferences.Editor editor = sp.edit();
                //if userName is unique in database:
                editor.putString("name",nameStr);  //we need database here to check unique
                editor.commit();
                Toast.makeText(changeName.this,"Username Saved",Toast.LENGTH_LONG).show();

            }
        });



    }
    public void backToProfile(){
        Intent intent1 = new Intent(this, MyProfile1.class);
        startActivity(intent1);
    }
}