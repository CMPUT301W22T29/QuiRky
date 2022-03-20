/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * View QR Fragment page which allows users to switch to different Activities.
 *
 * @author Raymart Bless C. Datuin
 */
public class ViewQRFragment extends Fragment {
    private Button Comment;
    private Button ElseQRCode;
    private Button SetPrivate;
    private Button Delete;
    private Button Location; // But may not be a button because the location description has to
    // change depending on where the location is.

    public static final String EXTRA_MESSAGE = "com.example.QuiRky.MESSAGE";

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_view_q_r, container, false);

        Comment = view.findViewById(R.id.commentsButtonView);
        ElseQRCode = view.findViewById(R.id.elseButtonView);
        SetPrivate = view.findViewById(R.id.privateButtonView);
        Delete = view.findViewById(R.id.deleteButtonView);
        Location = view.findViewById(R.id.locationButtonView);

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
        return view;
    }

    /**
     * When the Comments button is clicked this method is called, an this
     * method will launch the ViewCommentsActivity and allow users to comment
     * on a specific QR code.
     * The QRCode ID will need to be retrieved so it can be passed to the
     * Comment activity
     */
    public void comment() {
        Intent intent = new Intent(getActivity(), ViewCommentsActivity.class);
        // Needs to grab a specific QR code somehow
        String message = "Sample QR Code ID"; // This needs to be a specific QR code Id.
        intent.putExtra(message, EXTRA_MESSAGE);
        System.out.println("Launch Comment");
        startActivity(intent);
    }

    // Planning on expanding
    public void elseQRCode() {     }
    public void setPrivate() {     }
    public void delete() {     }
    public void location() {     }
}