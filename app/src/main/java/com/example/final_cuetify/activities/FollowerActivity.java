package com.example.final_cuetify.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_cuetify.R;
import com.example.final_cuetify.adapters.FollowerAdapter;
import com.example.final_cuetify.adapters.StudentAdapter;
import com.example.final_cuetify.models.User;
import com.example.final_cuetify.utilities.Constants;
import com.example.final_cuetify.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class FollowerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
        recyclerView = findViewById(R.id.rec_view_follower);
        FirebaseFirestore database  = FirebaseFirestore.getInstance();
        ArrayList<User> users = new ArrayList<>();
        FollowerAdapter adapter = new FollowerAdapter(users);

        database.collection(Constants.KEY_COLLECTION_FRIEND_REQUEST)
                .get()
                .addOnCompleteListener(task -> {
                    PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
                    String my_key = preferenceManager.getString(Constants.KEY_USER_ID);
//                    Toast.makeText(getApplicationContext(), my_key, Toast.LENGTH_SHORT).show();

                    if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        users.clear();
                        for(DocumentSnapshot documentSnapshot : task.getResult()) {
                            User user = new User();
                            user.image = documentSnapshot.getString(Constants.KEY_IMAGE);
                            user.name = documentSnapshot.getString(Constants.KEY_NAME);
                            user.id = documentSnapshot.getId(); // it is available in item_follower_layout_text_view
                            user.status = documentSnapshot.getString(Constants.KEY_STATUS);
                            user.uni_id = documentSnapshot.getString(Constants.KEY_UNI_ID);
                            user.sender = documentSnapshot.getString(Constants.KEY_FR_SENDER_KEY);
                            user.receiver = documentSnapshot.getString(Constants.KEY_FR_RECEIVER_KEY);

                            if(my_key.equals(user.receiver)) {
                                users.add(user);
                                adapter.notifyDataSetChanged();
                            }

                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(exception->{

                });

    }

}