/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Fragment to view all the other users who have scanned the same QRCode
 */
public class ViewQRScannersFragment extends Fragment {

    private ViewQRFragmentListener fragmentListener;
    private RecyclerClickerListener recyclerListener;
    private Button back;
    private RecyclerView list;
    private ArrayList<String> players;
    private final ArrayList<Bitmap> images = new ArrayList<>();
    private AdapterTextPhoto adapter;


    @Override
    public void onAttach(Context ct) {
        super.onAttach(ct);

        if(ct instanceof ViewQRFragmentListener)
            fragmentListener = (ViewQRFragmentListener) ct;
        else
            throw new RuntimeException(ct + " must implement ViewQRFragmentListener");
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

        players = fragmentListener.getPlayers();

        recyclerListener = position -> startViewPlayerActivity(players.get(position));

        adapter = new AdapterTextPhoto(players, images, getActivity(), recyclerListener);
        list.setAdapter(adapter);
        list.setLayoutManager( adapter.getLayoutManager() );

        back.setOnClickListener(view1 -> fragmentListener.changeFragment(new ViewQRButtonsFragment()));
    }

    /**
     * Start viewing another player's profile
     * @param username The username of the player to view
     */
    private void startViewPlayerActivity(String username) {
        Log.d("ViewQRCodes Fragment Says", "You clicked on this username: " + username);
        fragmentListener.viewProfile(username);
    }
}
