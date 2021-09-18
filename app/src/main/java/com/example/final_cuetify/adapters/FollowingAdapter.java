package com.example.final_cuetify.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_cuetify.R;
import com.example.final_cuetify.models.Following;
import com.example.final_cuetify.models.User;
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
        holder.name.setText(data.get(holder.getAdapterPosition()).name);
        holder.cancel_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView pro_file_image;
        private TextView name, cancel_req;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.following_name);
            cancel_req = itemView.findViewById(R.id.btn_cancel_request);
        }
    }
}
