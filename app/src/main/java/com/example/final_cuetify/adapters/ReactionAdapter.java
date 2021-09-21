package com.example.final_cuetify.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.final_cuetify.R;
import com.example.final_cuetify.models.Reaction;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ReactionAdapter extends RecyclerView.Adapter<ReactionAdapter.ViewHolder> {
    ArrayList<Reaction> data = new ArrayList<>();

    public ReactionAdapter(ArrayList<Reaction> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reaction_layout, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        byte[] bytes = Base64.decode(data.get(holder.getAdapterPosition()).reacter_image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        holder.image.setImageBitmap(bitmap);
        if(data.get(holder.getAdapterPosition()).isLiked.equals("yes")) {
            holder.like.setVisibility(View.VISIBLE);
        } else if(data.get(holder.getAdapterPosition()).isLoved.equals("yes")) {
            holder.love.setVisibility(View.VISIBLE);
        } else if(data.get(holder.getAdapterPosition()).isAngry.equals("yes")) {
            holder.angry.setVisibility(View.VISIBLE);
        } else {
            holder.sad.setVisibility(View.VISIBLE);
        }

        holder.name.setText(data.get(holder.getAdapterPosition()).reacter_name);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LottieAnimationView like, love, angry, sad;
        RoundedImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_in_reaction);
            like = itemView.findViewById(R.id.like_in_reac);
            love = itemView.findViewById(R.id.love_in_reac);
            angry = itemView.findViewById(R.id.angry_in_reac);
            sad = itemView.findViewById(R.id.sad_in_reac);
            image = itemView.findViewById(R.id.image_in_reaction);
        }
    }
}
