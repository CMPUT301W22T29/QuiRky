package com.example.quirky;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class is used to make a scrollable list, and used in the Recycler Adapter.
 *
 * @author Raymart Bless C. Datuin
 *
 */
public class CommentList extends RecyclerView.Adapter<CommentList.CommentViewHolder> {

    private ArrayList<Comment> comments;
    private Context context;

    public CommentList(Context context, ArrayList<Comment> comments) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment_list, parent, false); // List of comments
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.commentText.setText(comments.get(position).getContent());
        holder.uNameText.setText(comments.get(position).getUname());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentText, uNameText;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.commentTextView);
            uNameText = itemView.findViewById(R.id.userNameText);
        }
    }
}