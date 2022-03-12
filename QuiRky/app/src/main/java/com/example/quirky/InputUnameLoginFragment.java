package com.example.quirky;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class InputUnameLoginFragment extends DialogFragment {
    private EditText input;
    private LoginFragListener listener;

    public interface LoginFragListener {
        void confirm(String uname);
    }

    @Override
    public void onAttach(Context ct) {
        super.onAttach(ct);
        if(ct instanceof LoginFragListener) {
            listener = (LoginFragListener) ct;
        } else {
            throw new RuntimeException("Activity " + ct.toString() + " does not implement interface LoginFragListener!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.input_uname_login_fragment, null);
        input = view.findViewById(R.id.login_frag_input_field);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Username for Your Profile")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String uname = input.getText().toString();
                        listener.confirm(uname);
                    }
                })
                .create();
    }
}
