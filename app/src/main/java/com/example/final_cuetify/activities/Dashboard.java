package com.example.final_cuetify.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.final_cuetify.R;
import com.example.final_cuetify.adapters.NewsFeedAdapter;
import com.example.final_cuetify.models.NewsFeed;
import com.example.final_cuetify.models.User;
import com.example.final_cuetify.utilities.Constants;
import com.example.final_cuetify.utilities.PreferenceManager;
import com.example.final_cuetify.utilities.SpacingItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity implements View.OnClickListener , View.OnLongClickListener {

    private RecyclerView news_feed_rec_view;
    private PreferenceManager preferenceManager;
    private Button feed_cancel_button;
    private ScrollView feeds_posting;
    private Button post_comfirm;
    private EditText post_writting_field;


    LottieAnimationView menuID, menuCancelID, myProfile, myFriends, myFaculty, myStudents,myfollower, following;
    LinearLayout hidden_layout_menu;



    private String user_unique_id;
    private String encode_image;
    private String status;
    private String phone;
    private String email;
    private String university_id;
    private String name;
    private String message;

    // aditional function for listener

    private Boolean isValidInput() {
        String s = post_writting_field.getText().toString();
        if(s.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    private void postTheFeed() {
        HashMap<String, Object> feed = new HashMap<>();
        feed.put(Constants.KEY_NAME, name);
        feed.put(Constants.KEY_EMAIL, email);
        feed.put(Constants.KEY_USER_ID, user_unique_id);
        feed.put(Constants.KEY_UNI_ID, university_id);
        feed.put(Constants.KEY_IMAGE, encode_image);
        feed.put(Constants.KEY_PHONE, phone);
        feed.put(Constants.KEY_MESSAGE, post_writting_field.getText().toString());
        feed.put(Constants.KEY_STATUS, status);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_FEEDS)
                .add(feed)
                .addOnSuccessListener(documentReference -> {
                    feeds_posting.setVisibility(View.INVISIBLE);
                    news_feed_rec_view.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                    post_writting_field.setText("");
                    fetchDataForNewFeed();
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                });
    }

    // all listener for dashboard is located here
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_add_feed_button:
                feeds_posting.setVisibility(View.INVISIBLE);
                news_feed_rec_view.setVisibility(View.VISIBLE);
                menuID.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_post:
                if(isValidInput()) {
                    postTheFeed();
                    feeds_posting.setVisibility(View.INVISIBLE);
                    news_feed_rec_view.setVisibility(View.VISIBLE);
                    menuID.setVisibility(View.VISIBLE);
                    break;
                } else {
                    Toast.makeText(getApplicationContext(), "Please write first", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.menu_id:
                feeds_posting.setVisibility(View.VISIBLE);
                news_feed_rec_view.setVisibility(View.INVISIBLE);
                menuID.setVisibility(View.INVISIBLE);
                break;
            case R.id.menu_id_cancel:
                hidden_layout_menu.setVisibility(View.INVISIBLE);
                menuID.setVisibility(View.VISIBLE);
                menuCancelID.setVisibility(View.INVISIBLE);
                feeds_posting.setVisibility(View.INVISIBLE);
                news_feed_rec_view.setVisibility(View.VISIBLE);
                break;
            case R.id.myProfile:
                Intent intent = new Intent(getApplicationContext(), MyProfile.class);
                startActivity(intent);
                break;
            case R.id.allStudents:
                Intent intent1 = new Intent(getApplicationContext(), StudentsActivity.class);
                startActivity(intent1);
                break;
            case R.id.allFaculty:
                Intent intent2 = new Intent(getApplicationContext(), FacultyActivity.class);
                startActivity(intent2);
                break;
            case R.id.allFriends:
                Intent intent3 = new Intent(getApplicationContext(), FriendsActivity.class);
                startActivity(intent3);
                break;
            case R.id.wantsToConnect:
                Intent intent4 = new Intent(getApplicationContext(), FollowerActivity.class);
                startActivity(intent4);
                break;
            case R.id.folowing:
                Intent intent5 = new Intent(getApplicationContext(), FollowingActivity.class);
                startActivity(intent5);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.menu_id:
                hidden_layout_menu.setVisibility(View.VISIBLE);
                news_feed_rec_view.setVisibility(View.INVISIBLE);
                menuID.setVisibility(View.INVISIBLE);
                menuCancelID.setVisibility(View.VISIBLE);
                feeds_posting.setVisibility(View.INVISIBLE);
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        post_writting_field = findViewById(R.id.post_edittext);
        feeds_posting = findViewById(R.id.news_feed_new_post_field);
        feed_cancel_button = findViewById(R.id.back_add_feed_button);
        post_comfirm = findViewById(R.id.btn_post);
        news_feed_rec_view = findViewById(R.id.recycler_view_newFeed);
        SpacingItem spacingItem = new SpacingItem(60);
        news_feed_rec_view.addItemDecoration(spacingItem);


        preferenceManager = new PreferenceManager(getApplicationContext());
        user_unique_id = preferenceManager.getString(Constants.KEY_USER_ID);
        encode_image = preferenceManager.getString(Constants.KEY_IMAGE);
        status = preferenceManager.getString(Constants.KEY_STATUS);
        phone = preferenceManager.getString(Constants.KEY_PHONE);
        email = preferenceManager.getString(Constants.KEY_EMAIL);
        university_id = preferenceManager.getString(Constants.KEY_UNI_ID);
        name = preferenceManager.getString(Constants.KEY_NAME);
        message = post_writting_field.getText().toString();



        myFaculty = findViewById(R.id.allFaculty);
        myFriends = findViewById(R.id.allFriends);
        myStudents = findViewById(R.id.allStudents);
        myProfile = findViewById(R.id.myProfile);
        myfollower = findViewById(R.id.wantsToConnect);
        following = findViewById(R.id.folowing);
        menuCancelID = findViewById(R.id.menu_id_cancel);
        menuID = findViewById(R.id.menu_id);
        hidden_layout_menu = findViewById(R.id.hiddenMenuID);

        feed_cancel_button.setOnClickListener(this);
        post_comfirm.setOnClickListener(this);
        myFaculty.setOnClickListener(this);
        myFriends.setOnClickListener(this);
        myStudents.setOnClickListener(this);
        myProfile.setOnClickListener(this);
        menuID.setOnClickListener(this);
        menuCancelID.setOnClickListener(this);
        menuID.setOnLongClickListener(this);
        following.setOnClickListener(this);
        myfollower.setOnClickListener(this);

        fetchDataForNewFeed();
    }


    private void fetchDataForNewFeed() {
        news_feed_rec_view.setVisibility(View.INVISIBLE);
        findViewById(R.id.progressBarNewsFeed).setVisibility(View.VISIBLE);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_FEEDS)
            .get()
            .addOnCompleteListener(task -> {
                ArrayList<NewsFeed> data = new ArrayList<>();

               if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                   for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                       NewsFeed newsFeed = new NewsFeed();
                       newsFeed.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                       newsFeed.uni_id = queryDocumentSnapshot.getString(Constants.KEY_UNI_ID);
                       newsFeed.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                       newsFeed.message = queryDocumentSnapshot.getString(Constants.KEY_MESSAGE);
                       newsFeed.status = queryDocumentSnapshot.getString(Constants.KEY_STATUS);
                       newsFeed.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                       newsFeed.comments = queryDocumentSnapshot.getString(Constants.KEY_COMMENT);
                       newsFeed.user_key = queryDocumentSnapshot.getString(Constants.KEY_USER_ID);
                       newsFeed.feed_id = queryDocumentSnapshot.getId();
                       data.add(newsFeed);
                   }
                   findViewById(R.id.progressBarNewsFeed).setVisibility(View.INVISIBLE);
                   news_feed_rec_view.setVisibility(View.VISIBLE);

                   NewsFeedAdapter adapter = new NewsFeedAdapter(data);
                   adapter.notifyDataSetChanged();
                   news_feed_rec_view = findViewById(R.id.recycler_view_newFeed);
                   news_feed_rec_view.setLayoutManager(new LinearLayoutManager(this));
                   news_feed_rec_view.setAdapter(adapter);
               } else {
                   findViewById(R.id.progressBarNewsFeed).setVisibility(View.INVISIBLE);
                   Toast.makeText(getApplicationContext(), "Unable to load feeds", Toast.LENGTH_SHORT).show();
               }
            });
    }
}