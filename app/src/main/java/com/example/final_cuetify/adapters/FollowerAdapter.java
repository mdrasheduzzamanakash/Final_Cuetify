package com.example.final_cuetify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_cuetify.R;
import com.example.final_cuetify.models.Friend;
import com.example.final_cuetify.models.User;
import com.example.final_cuetify.utilities.Constants;
import com.example.final_cuetify.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {

    ArrayList<User> data = new ArrayList<>();

    public FollowerAdapter(ArrayList<User> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_follower, parent, false    );
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(data.get(position).name);
        holder.uni_id.setText(data.get(position).uni_id);
        holder.status.setText(data.get(position).status);
        holder.user_key.setText(data.get(position).sender);
        holder.accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.accept_btn.setVisibility(View.INVISIBLE);
                holder.progressBar.setVisibility(View.VISIBLE);
                PreferenceManager preferenceManager = new PreferenceManager(v.getContext()  );
                Friend friend = new Friend();
                friend.friend_imege = data.get(holder.getAdapterPosition()).image;
                friend.my_image = preferenceManager.getString(Constants.KEY_IMAGE);
                friend.myName = preferenceManager.getString(Constants.KEY_NAME);
                friend.friend_name = data.get(holder.getAdapterPosition()).name;
                friend.my_id = preferenceManager.getString(Constants.KEY_UNI_ID);
                friend.friend_id = data.get(holder.getAdapterPosition()).uni_id;
                friend.friend_key = data.get(holder.getAdapterPosition()).sender;
                friend.my_key = data.get(holder.getAdapterPosition()).receiver;
//                Toast.makeText(v.getContext(), data.get(holder.getAdapterPosition()).id, Toast.LENGTH_SHORT).show();

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection(Constants.KEY_FOR_FRIENDS_COLLECTION)
                        .add(friend)
                        .addOnSuccessListener(documentReference -> {
                            // delete from request collection
                            database.collection(Constants.KEY_COLLECTION_FRIEND_REQUEST)
                                    .document(data.get(holder.getAdapterPosition()).id)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            holder.progressBar.setVisibility(View.VISIBLE);
                                            holder.friends_now.setVisibility(View.VISIBLE);
                                            holder.delete.setVisibility(View.INVISIBLE);
                                        }
                                    })
                                    .addOnFailureListener(exception-> {
                                        Toast.makeText(v.getContext(), "Something gone wrong! but you are friend now!", Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .addOnFailureListener(exception-> {
                            holder.accept_btn.setVisibility(View.VISIBLE);
                            holder.progressBar.setVisibility(View.VISIBLE);
                            Toast.makeText(v.getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        });
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

        private RoundedImageView profileImage;
        private TextView name, uni_id, status, accept_btn, delete, user_key,friends_now, unfriend;
        private ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.follower_profile_image);
            name = itemView.findViewById(R.id.follower_name);
            uni_id = itemView.findViewById(R.id.follower_uni_id);
            accept_btn = itemView.findViewById(R.id.btn_accept_req);
            delete = itemView.findViewById(R.id.btn_delete);
            status = itemView.findViewById(R.id.follower_status);
            user_key = itemView.findViewById(R.id.user_key_follower);
            progressBar = itemView.findViewById(R.id.progressBarAcceptBtn);
            friends_now = itemView.findViewById(R.id.btn_friends_now);
            unfriend = itemView.findViewById(R.id.btn_unfriend);
        }

    }
}
