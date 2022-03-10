// Author: Raymart
// Contact me Through discord for any questions

package com.example.quirky;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

public class CommentList {

    private ArrayList<Comment> comments;
    private Context context;

    /*
    public CommentList(Context context, ArrayList<Comment> comments) {
        super(context, 0, comments);
        Collections.sort(comments);
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.comment_list, parent, false);
        }

        Comment comment = comments.get(position);
        TextView textComment = view.findViewById(R.id.comment_content_text);
        TextView userName = view.findViewById(R.id.text_user_name);

        textComment.setText(comment.getContent());
        userName.setText(comment.getUname());
        return view;
    }
*/}
