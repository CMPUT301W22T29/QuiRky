/*
 * Copyright (c) 2022. CMPUT301W22T29
 * Subject to MIT License
 * See full terms at https://github.com/CMPUT301W22T29/QuiRky/blob/main/LICENSE
 */

package com.example.quirky;

import androidx.fragment.app.Fragment;

public interface ViewQRFragmentListener {
    void changeFragment(Fragment frag);
    void commentsButton();
    void privateButton();
    void deleteButton();
}
