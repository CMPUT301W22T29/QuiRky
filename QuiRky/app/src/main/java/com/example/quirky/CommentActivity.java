package com.example.quirky;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CommentActivity {
    private Button Save;
    private Button Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    public void save() { }
    public void cancel() { }
}
