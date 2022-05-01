package com.example.quirky;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quirky.models.Comment;

import java.util.ArrayList;

/**
 * Adapter class to map a Comment to a Recycler View.
 * Will display a comment's content and user to the recycler
 *
 * @author Raymart Bless C. Datuin
 *
 */
public class AdapterComment extends RecyclerView.Adapter<AdapterComment.CommentViewHolder> {

    private ArrayList<Comment> comments;
    private final Context context;

    public AdapterComment(Context context, ArrayList<Comment> comments) {
        this.comments = comments;
        this.context = context;
    }

    public LinearLayoutManager getLayoutManager() {
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_comment, parent, false); // List of comments
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


    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentText, uNameText;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.commentTextView);
            uNameText = itemView.findViewById(R.id.userNameText);
        }
    }
}