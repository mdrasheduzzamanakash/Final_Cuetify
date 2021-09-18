package com.example.final_cuetify.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_cuetify.R;
import com.example.final_cuetify.models.Friend;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    ArrayList<Friend> data = new ArrayList<>();
    public FriendsAdapter(ArrayList<Friend> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends_layout, parent, false    );
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name_me.setText(data.get(holder.getAdapterPosition()).myName);
        holder.id_me.setText(data.get(holder.getAdapterPosition()).my_id);
        holder.name_friend.setText(data.get(holder.getAdapterPosition()).friend_name);
        holder.id_friend.setText(data.get(holder.getAdapterPosition()).friend_id);

        // adding listerer

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageMe, imageFriend;
        private TextView name_me, name_friend, id_me, id_friend, messageFriend;
        Button seePlus, unFriend, unFollow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageMe = itemView.findViewById(R.id.image_first_one);
            imageFriend = itemView.findViewById(R.id.image_another_one);
            name_me = itemView.findViewById(R.id.name_first);
            name_friend = itemView.findViewById(R.id.name_second);
            id_me = itemView.findViewById(R.id.id_first);
            id_friend = itemView.findViewById(R.id.id_second);
            seePlus = itemView.findViewById(R.id.seePlus);
            messageFriend = itemView.findViewById(R.id.messageFriend);
        }
    }
}
