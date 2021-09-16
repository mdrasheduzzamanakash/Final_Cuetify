package com.example.final_cuetify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_cuetify.R;
import com.example.final_cuetify.models.Comments;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolderComment> {
    private ArrayList<Comments> data;

    public CommentAdapter(ArrayList<Comments> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolderComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        return new ViewHolderComment(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderComment holder, int position) {
        holder.comment_message.setText(data.get(position).comment_message);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    static class ViewHolderComment extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener{
        private TextView comment_message;
        private ImageView delete_button, like_button;
        private RoundedImageView profileImage;

        public ViewHolderComment(@NonNull View itemView) {
            super(itemView);
            this.comment_message = itemView.findViewById(R.id.comment_message);
            this.delete_button = itemView.findViewById(R.id.comment_delete);
            this.like_button = itemView.findViewById(R.id.comment_like);
            this.profileImage = itemView.findViewById(R.id.comment_profile_image);

            itemView.setOnLongClickListener(this);
            comment_message.setOnClickListener(this);
            delete_button.setOnClickListener(this);
            like_button.setOnClickListener(this);
            profileImage.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Hello", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }
}
