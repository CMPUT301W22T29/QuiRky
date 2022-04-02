/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// Fragment Code Sourced From:
// https://www.youtube.com/watch?v=Btli00YA1eI
// By User:
// https://www.youtube.com/channel/UC4Gwya_ODul8t9kjxsHm2dw
// Published January 3 2021
public class ViewQRButtonsFragment extends Fragment {

    Button comments, players, setprivate, delete;
    TextView location;

    private ViewQRFragmentListener listener;

    @Override
    public void onAttach(Context ct) {
        super.onAttach(ct);

        if(ct instanceof ViewQRFragmentListener)
            listener = (ViewQRFragmentListener) ct;
        else
            throw new RuntimeException(ct + " must implement ViewQRFragmentListener");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_qr_buttons, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        comments = v.findViewById(R.id.view_comments_button);
        players = v.findViewById(R.id.other_scanners_button);
        setprivate = v.findViewById(R.id.private_qr_button);
        delete = v.findViewById(R.id.delete_qr_button);
        location = v.findViewById(R.id.qr_location_textbox);

        location.setText("Edmonton, lat:9.239486, long: 1/2");

        comments.setOnClickListener(view -> listener.commentsButton());
        players.setOnClickListener(view -> listener.changeFragment(new ViewQRScannersFragment()));
        setprivate.setOnClickListener(view -> listener.privateButton());
        delete.setOnClickListener(view -> listener.deleteButton());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
