package com.example.final_cuetify.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.final_cuetify.R;
import com.example.final_cuetify.adapters.StudentAdapter;
import com.example.final_cuetify.models.User;
import com.example.final_cuetify.utilities.Constants;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class StudentsActivity extends AppCompatActivity {

    private RecyclerView st_rec_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        st_rec_view = findViewById(R.id.rec_view_students);


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_STATUS, "student")
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<User> users = new ArrayList<>();
                    if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {

                        for(DocumentSnapshot documentSnapshot : task.getResult()) {
                            User user = new User();
                            user.image = documentSnapshot.getString(Constants.KEY_IMAGE);
                            user.name = documentSnapshot.getString(Constants.KEY_NAME);
                            user.id = documentSnapshot.getId();
                            user.status = documentSnapshot.getString(Constants.KEY_STATUS);
                            user.email = documentSnapshot.getString(Constants.KEY_EMAIL);
                            user.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.phone = documentSnapshot.getString(Constants.KEY_PHONE);
                            user.uni_id = documentSnapshot.getString(Constants.KEY_UNI_ID);
                            users.add(user);
                        }

                        StudentAdapter adapter = new StudentAdapter(users);
                        st_rec_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        st_rec_view.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(getApplicationContext(), "Unable to load", Toast.LENGTH_SHORT).show();
                });
    }
}