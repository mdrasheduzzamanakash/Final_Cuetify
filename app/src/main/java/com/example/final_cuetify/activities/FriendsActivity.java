package com.example.final_cuetify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.final_cuetify.R;
import com.example.final_cuetify.adapters.FriendsAdapter;
import com.example.final_cuetify.models.Friend;
import com.example.final_cuetify.utilities.Constants;
import com.example.final_cuetify.utilities.PreferenceManager;
import com.example.final_cuetify.utilities.Variables;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        recyclerView = findViewById(R.id.rec_view_friends);

        ArrayList<Friend> data = new ArrayList<>();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.getInstance().collection(Constants.KEY_FOR_FRIENDS_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot documentSnapshot : task.getResult()) {
                            Friend friend = new Friend();
                            friend.my_id = documentSnapshot.getString(Variables.KEY_MY_ID);
                            friend.myName = documentSnapshot.getString(Variables.KEY_MY_NAME);
                            friend.my_image = documentSnapshot.getString(Variables.KEY_MY_IMAGE);
                            friend.friend_id = documentSnapshot.getString(Variables.KEY_FRIEND_ID);
                            friend.friend_key = documentSnapshot.getString(Variables.KEY_FRIEND_KEY);
                            friend.friend_name = documentSnapshot.getString(Variables.KEY_FRIEND_NAME);
                            friend.friend_key = documentSnapshot.getString(Variables.KEY_FRIEND_KEY);
                            friend.my_key = documentSnapshot.getString(Variables.KEY_MY_KEY);
                            friend.type = documentSnapshot.getString(Variables.KEY_TYPE);

                            String my_key = new PreferenceManager(getApplicationContext()).getString(Constants.KEY_USER_ID);
//                            Log.d("------------", friend.toString());
                            if(friend.my_key.equals(my_key) || friend.friend_key.equals(my_key)) {
                                data.add(friend);
                            }
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(new FriendsAdapter(data));
                    }
                })
                .addOnFailureListener(e->{
                    Toast.makeText(getApplicationContext(), "Unable to load!", Toast.LENGTH_SHORT).show();
                });

    }
}