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

import com.example.final_cuetify.R;
import com.example.final_cuetify.models.Faculty;
import com.example.final_cuetify.utilities.Constants;
import com.example.final_cuetify.utilities.PreferenceManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.ViewHolder> {

    ArrayList<Faculty> data = new ArrayList<>();

    public FacultyAdapter(ArrayList<Faculty> data) {
        this.data = data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faculty_layout, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        byte[] bytes = Base64.decode(data.get(holder.getAdapterPosition()).image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        holder.fac_profile_image.setImageBitmap(bitmap);
        PreferenceManager preferenceManager = new PreferenceManager(holder.itemView.getContext());
        if(preferenceManager.getString(Constants.KEY_STATUS).equals("student")) {
            holder.erro_message.setVisibility(View.VISIBLE);
        } else {
            holder.erro_message.setVisibility(View.INVISIBLE);
            holder.connect.setVisibility(View.VISIBLE);
        }
        holder.name.setText(data.get(holder.getAdapterPosition()).name);
        holder.email.setText(data.get(holder.getAdapterPosition()).email);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView fac_profile_image;
        private TextView dep_name, name, email, connect, erro_message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dep_name = itemView.findViewById(R.id.department);
            name = itemView.findViewById(R.id.following_name);
            email = itemView.findViewById(R.id.status_message);
            connect = itemView.findViewById(R.id.btn_connect);
            erro_message = itemView.findViewById(R.id.textView);
            fac_profile_image = itemView.findViewById(R.id.fac_profile_image);
        }
    }
}
