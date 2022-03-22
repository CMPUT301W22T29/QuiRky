/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewQRScannersFragment extends Fragment {

    private ViewQRFragmentListener listener;
    private Button back;
    private RecyclerView list;
    private ArrayList<String> players;
    private final ArrayList<Drawable> images = new ArrayList<>();
    private QRAdapter adapter;


    @Override
    public void onAttach(Context ct) {
        super.onAttach(ct);

        if(ct instanceof ViewQRFragmentListener)
            listener = (ViewQRFragmentListener) ct;
        else
            throw new RuntimeException(ct.toString() + " must implement ViewQRFragmentListener");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_qr_scanners, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        back = view.findViewById(R.id.fragment_back_button);
        list = view.findViewById(R.id.other_players_who_have_scanned_this_particular_qr_code_list);

        // Temporary filling of ArrayList
        players = new ArrayList<>();
        players.add("Player1");
        players.add("Player2");
        players.add("player3");

        adapter = new QRAdapter(players, images, getActivity());
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.changeFragment(new ViewQRButtonsFragment());
            }
        });
    }
}
