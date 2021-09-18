package com.example.final_cuetify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.final_cuetify.R;
import com.example.final_cuetify.adapters.CommentAdapter;
import com.example.final_cuetify.adapters.FollowingAdapter;
import com.example.final_cuetify.models.Comments;
import com.example.final_cuetify.models.Following;
import com.example.final_cuetify.utilities.Constants;
import com.example.final_cuetify.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FollowingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        recyclerView = findViewById(R.id.rec_view_following);
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_FRIEND_REQUEST)
                .whereEqualTo(Constants.KEY_FR_SENDER_KEY, preferenceManager.getString(Constants.KEY_USER_ID))
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<Following> data = new ArrayList<>();
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Following following = new Following();
                            following.image = queryDocumentSnapshot.getString(Constants.KEY_FR_IMAGE);
                            following.name = queryDocumentSnapshot.getString(Constants.KEY_FR_NAME);
                            data.add(following);
                        }
                        Toast.makeText(getApplicationContext(), Integer.toString(data.size()), Toast.LENGTH_SHORT).show();

                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(new FollowingAdapter(data));
                    }
                })
                .addOnFailureListener(exception-> {
                    Toast.makeText(getApplicationContext(), "Unable to load data", Toast.LENGTH_SHORT).show();
                });
    }
}