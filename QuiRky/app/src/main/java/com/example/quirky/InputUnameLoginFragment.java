package com.example.quirky;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.quirky.activities.CodeScannerActivity;

/**
 * @author Jonathen Adsit
 * Fragment that is attached to LoginActivity, which is the Login Activity.
 * Prompts the user for a username, and provides an option to login through QRCode scanning instead.
 * Does no work on it's own, instead communicates it's input to the calling activity
 * @see CodeScannerActivity
 */
public class InputUnameLoginFragment extends DialogFragment {
    private EditText input;
    private LoginFragListener listener;

    /**
     * The interface used to communicate with the calling activity
     */
    public interface LoginFragListener {
        void OnClickConfirm(String uname);
        void LoginByQR();
    }

    /**
     * Checks that the calling activity implements the listener correctly
     * @param ct The calling activity typecast to Context
     */
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

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_login, null);
        input = view.findViewById(R.id.login_frag_input_field);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Create a new account!")
                .setPositiveButton("Sign up!", (dialogInterface, i) -> {
                    String uname = input.getText().toString();
                    listener.OnClickConfirm(uname);
                })
                .setNeutralButton("Login By QRCode", (dialogInterface, i) -> listener.LoginByQR())
                .create();
    }
}
