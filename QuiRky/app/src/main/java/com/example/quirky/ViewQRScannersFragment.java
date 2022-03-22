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

import androidx.fragment.app.Fragment;

public class ViewQRScannersFragment extends Fragment {

    ViewQRFragmentListener listener;

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
        return inflater.inflate(R.layout.fragment_view_qr_buttons, container, false);
    }
}
