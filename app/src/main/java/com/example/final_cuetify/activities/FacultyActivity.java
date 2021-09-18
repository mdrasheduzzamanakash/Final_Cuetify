package com.example.final_cuetify.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.final_cuetify.R;
import com.example.final_cuetify.adapters.FacultyAdapter;
import com.example.final_cuetify.models.Faculty;
import com.example.final_cuetify.models.Friend;
import com.example.final_cuetify.models.User;
import com.example.final_cuetify.utilities.Constants;
import com.example.final_cuetify.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FacultyActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        recyclerView = findViewById(R.id.rec_view_faculty);

        ArrayList<Faculty> data = new ArrayList<>();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_STATUS, "faculty")
                .get()
                .addOnCompleteListener(task -> {
                    PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
                    String my_key = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        data.clear();
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            Faculty faculty = new Faculty();
                            faculty.image = documentSnapshot.getString(Constants.KEY_IMAGE);
                            faculty.name = documentSnapshot.getString(Constants.KEY_NAME);
                            faculty.receiver_id = documentSnapshot.getId();
                            faculty.sender_id = my_key;

                            if (!my_key.equals(documentSnapshot.getId())) {
                                data.add(faculty);
                            }
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(new FacultyAdapter(data));
                    }
                });
    }
}