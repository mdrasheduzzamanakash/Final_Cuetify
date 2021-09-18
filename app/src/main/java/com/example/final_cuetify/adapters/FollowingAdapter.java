package com.example.final_cuetify.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_cuetify.R;
import com.example.final_cuetify.models.Following;
import com.example.final_cuetify.models.User;
import com.example.final_cuetify.utilities.Constants;
import com.example.final_cuetify.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    ArrayList<Following> data = new ArrayList<>();

    public FollowingAdapter(ArrayList<Following> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_item_view, parent, false    );
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(data.get(holder.getAdapterPosition()).receiver);




        holder.cancel_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager preferenceManager = new PreferenceManager(holder.itemView.getContext());
                preferenceManager.putString(data.get(holder.getAdapterPosition()).receiver, null);

                holder.cancel_req.setVisibility(View.INVISIBLE);
                holder.progressBar.setVisibility(View.VISIBLE);

                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection(Constants.KEY_COLLECTION_FRIEND_REQUEST)
                        .document(data.get(holder.getAdapterPosition()).doc_id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                holder.status_message.setText("Request Canceled");
                                holder.status_message.setTextColor(Color.GREEN);
                                holder.progressBar.setVisibility(View.INVISIBLE);
                                holder.status_message.setVisibility(View.VISIBLE);
                            }
                        })
                        .addOnFailureListener(e -> {
                           holder.cancel_req.setVisibility(View.VISIBLE);
                           holder.progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(v.getContext(), "Unable to cancel! Chack internet connection", Toast.LENGTH_SHORT).show();
                        });
            }
        });


    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView pro_file_image;
        private TextView name, cancel_req, status_message;
        private ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.following_name);
            cancel_req = itemView.findViewById(R.id.btn_cancel_request);
            progressBar = itemView.findViewById(R.id.progressBarCancelReq);
            status_message = itemView.findViewById(R.id.status_message);
        }
    }
}
