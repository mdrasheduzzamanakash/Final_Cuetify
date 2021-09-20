package com.example.final_cuetify.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_cuetify.R;
import com.example.final_cuetify.models.User;
import com.example.final_cuetify.utilities.Constants;
import com.example.final_cuetify.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    ArrayList<User> data = new ArrayList<>();

    public StudentAdapter(ArrayList<User> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item_layouts, parent, false    );
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(data.get(position).name);
        holder.uni_id.setText(data.get(position).uni_id);
        holder.status.setText(data.get(position).status);
        holder.user_key.setText(data.get(position).id);
        PreferenceManager preferenceManager = new PreferenceManager(holder.itemView.getContext());
        if(preferenceManager.getString(holder.user_key.getText().toString()) != null) {
            holder.addFriend.setVisibility(View.INVISIBLE);
            holder.req_button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{


        private RoundedImageView profileImage;
        private TextView name, uni_id, status, addFriend, user_key, req_button, remove_btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.image_st_item);
            name = itemView.findViewById(R.id.st_item_name);
            uni_id = itemView.findViewById(R.id.st_item_id);
            addFriend = itemView.findViewById(R.id.btn_add_friend);
            status = itemView.findViewById(R.id.st_status);
            user_key = itemView.findViewById(R.id.user_key);
            req_button = itemView.findViewById(R.id.btn_requested);
            addFriend.setOnClickListener(this);
            req_button.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add_friend:
                    addFriend.setVisibility(View.INVISIBLE);
                    itemView.findViewById(R.id.progressBarAddFriend).setVisibility(View.VISIBLE);
                    funcionAddFriend();
                    break;
                case R.id.image_st_item:
                    break;
                case R.id.btn_requested:
                    req_button.setVisibility(View.INVISIBLE);
                    itemView.findViewById(R.id.progressBarAddFriend).setVisibility(View.VISIBLE);
                    funcionCancelRequest();
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }




        // aditional function
        public void funcionCancelRequest() {
            PreferenceManager preferenceManager = new PreferenceManager(itemView.getContext());
            preferenceManager.putString(user_key.getText().toString(), null);
            String key = preferenceManager.getString(user_key.getText().toString() + user_key.getText().toString());

//            Toast.makeText(itemView.getContext(), key+"fd", Toast.LENGTH_SHORT).show();
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            if(key != null){

                database.collection(Constants.KEY_COLLECTION_FRIEND_REQUEST)
                        .document(key)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                itemView.findViewById(R.id.progressBarAddFriend).setVisibility(View.INVISIBLE);
                                addFriend.setVisibility(View.VISIBLE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            } else {
                itemView.findViewById(R.id.progressBarAddFriend).setVisibility(View.INVISIBLE);
                addFriend.setVisibility(View.VISIBLE);
            }
        }

        public void funcionAddFriend() {
            PreferenceManager preferenceManager = new PreferenceManager(itemView.getContext());
            HashMap<String, Object> request = new HashMap<>();
            request.put(Constants.KEY_FR_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
            request.put(Constants.KEY_FR_RECEIVER_NAME, name.getText().toString());
            request.put(Constants.KEY_FR_UNI_ID, preferenceManager.getString(Constants.KEY_UNI_ID));
            request.put(Constants.KEY_FR_SENDER_KEY, preferenceManager.getString(Constants.KEY_USER_ID));
            request.put(Constants.KEY_FR_RECEIVER_KEY, user_key.getText().toString());
            request.put(Constants.KEY_FR_IMAGE, preferenceManager.getString(Constants.KEY_IMAGE));
            request.put(Constants.KEY_FR_STATUS, preferenceManager.getString(Constants.KEY_STATUS));

            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(Constants.KEY_COLLECTION_FRIEND_REQUEST)
                    .add(request)
                    .addOnSuccessListener(documentReference -> {
                        String to_whom_key = user_key.getText().toString();
                        String friend_request_id = to_whom_key + to_whom_key;
                        preferenceManager.putString(to_whom_key, Constants.KEY_FR_ALREADY_SENT);
                        preferenceManager.putString(friend_request_id, documentReference.getId());
                        itemView.findViewById(R.id.progressBarAddFriend).setVisibility(View.INVISIBLE);
                        addFriend.setVisibility(View.VISIBLE);
                        addFriend.setText("Friend Request Sent");
                        addFriend.setBackgroundColor(Color.BLUE);
                        Toast.makeText(itemView.getContext(),   "You can cancel the request from 'Following'", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(exception-> {
                        itemView.findViewById(R.id.progressBarAddFriend).setVisibility(View.INVISIBLE);
                        addFriend.setVisibility(View.VISIBLE);
                        Toast.makeText(itemView.getContext(), "Unable to send fried request", Toast.LENGTH_SHORT).show();
                    });
        }

    }
}
