/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * Interface to allow ViewQRActivity and it's fragments to communicate
 */
public interface ViewQRFragmentListener {
    void changeFragment(Fragment frag);
    void commentsButton();
    void deleteButton();
    ArrayList<String> getPlayers();
}
