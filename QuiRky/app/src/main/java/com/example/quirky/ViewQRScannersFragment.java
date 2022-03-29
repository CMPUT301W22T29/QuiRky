/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewQRScannersFragment extends Fragment {

    private ViewQRFragmentListener fragmentListener;
    private RecyclerClickerListener recyclerListener;
    private Button back;
    private RecyclerView list;
    private ArrayList<String> players;
    private final ArrayList<Drawable> images = new ArrayList<>();
    private QRAdapter adapter;


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

        // Temporary filling of ArrayList
        players = fragmentListener.getPlayers();

        recyclerListener = position -> startViewPlayerActivity(players.get(position));

        adapter = new QRAdapter(players, images, getActivity(), recyclerListener);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        back.setOnClickListener(view1 -> fragmentListener.changeFragment(new ViewQRButtonsFragment()));
    }

    private void startViewPlayerActivity(String username) {
        Log.d("ViewQRCodes Fragment Says", "You clicked on this username: " + username);
        DatabaseController dc = new DatabaseController(FirebaseFirestore.getInstance(), getActivity());
        dc.readProfile(username).addOnCompleteListener(task -> {
            Profile p = dc.getProfile(task);
            Intent i = new Intent(getContext(), ProfileViewerActivity.class);
            i.putExtra("profile", p);
            startActivity(i);
        });
    }
}
