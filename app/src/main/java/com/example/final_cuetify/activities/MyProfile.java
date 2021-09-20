package com.example.final_cuetify.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.final_cuetify.R;
import com.example.final_cuetify.adapters.FriendsAdapter;
import com.example.final_cuetify.adapters.NewsFeedAdapter;
import com.example.final_cuetify.databinding.ActivityMyProfileBinding;
import com.example.final_cuetify.models.Friend;
import com.example.final_cuetify.models.NewsFeed;
import com.example.final_cuetify.utilities.Constants;
import com.example.final_cuetify.utilities.PreferenceManager;
import com.example.final_cuetify.utilities.Variables;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyProfile extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_profile:
                binding.editProfile.setVisibility(View.INVISIBLE);
                binding.save.setVisibility(View.VISIBLE);
                binding.profileDelete.setVisibility(View.VISIBLE);
                binding.profileEditPanel.setVisibility(View.VISIBLE);
                binding.isPrivateFriend.setVisibility(View.VISIBLE);
                binding.isPrivateFeed.setVisibility(View.VISIBLE);
                break;
            case R.id.expanding_friend:
                binding.expandingFriend.setVisibility(View.INVISIBLE);
                binding.profileFriendsRecView.setVisibility(View.VISIBLE);
                binding.collapsingFriend.setVisibility(View.VISIBLE);
                binding.progressBarFriendRecView.setVisibility(View.VISIBLE);
                expandingFriend();
                break;
            case R.id.collapsingFriend:
                binding.collapsingFriend.setVisibility(View.INVISIBLE);
                binding.expandingFriend.setVisibility(View.VISIBLE);
                binding.profileFriendsRecView.setVisibility(View.GONE);
                break;
            case R.id.expanding_feed:
                binding.expandingFeed.setVisibility(View.INVISIBLE);
                binding.collapsingFeed.setVisibility(View.VISIBLE);
                binding.profileFeedsRecView.setVisibility(View.VISIBLE);
                binding.progressBarFeedsRecView.setVisibility(View.VISIBLE);
                expandingFeed();
                break;
            case R.id.collapsingFeed:
                binding.expandingFeed.setVisibility(View.VISIBLE);
                binding.profileFeedsRecView.setVisibility(View.GONE);
                binding.collapsingFeed.setVisibility(View.INVISIBLE);
                break;
            case R.id.save:
                binding.deactivatePanelId.setVisibility(View.GONE);
                editPofileSave();
                // extra function
                break;
            case R.id.profile_delete:
                binding.save.setVisibility(View.INVISIBLE);
                binding.editProfile.setVisibility(View.VISIBLE);
                binding.profileEditPanel.setVisibility(View.GONE);
                binding.deactivatePanelId.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_delete:
                deleteProfile();
                break;
            case R.id.deactivation_confirm:
                doDeactivation();
                break;
            case R.id.delete_confirm:
                doDelete();
                break;
            default:
                break;
        }
    }

    private void doDelete() {
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID))
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        binding.deactivatingWarning.setText("Deleted your Profile \n");
                        database.collection(Constants.KEY_FEEDS)
                                .whereEqualTo(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        binding.deactivatingWarning.append("Feeds and comments are being deleted...\n");
                                        ArrayList<String> allFeeds = new ArrayList<>();
                                        if (task.getResult() != null && task.getResult().size() > 0) {
                                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                                allFeeds.add(documentSnapshot.getId());
                                            }
                                            ArrayList<String> allComments = new ArrayList<>();
                                            for (String s : allFeeds) {
                                                database.collection(Constants.KEY_FEEDS)
                                                        .document(s)
                                                        .delete();


                                                // have to delete all the comments of this feed
                                                allComments.clear();
                                                database.collection(Constants.KEY_COMMENTS_COLLECTION)
                                                        .whereEqualTo(Constants.KEY_COMMENT_FEED_ID, s)
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.getResult() != null && task.getResult().size() > 0) {

                                                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                                                        allComments.add(documentSnapshot.getId());
                                                                    }

                                                                }
                                                            }
                                                        });
                                                for (String c : allComments) {
                                                    database.collection(Constants.KEY_COMMENTS_COLLECTION)
                                                            .document(c)
                                                            .delete();
                                                }
                                            }
                                            binding.deactivatingWarning.append("Feeds and Comments Deletion Completed\n");


                                            binding.deactivatingWarning.append("Friend Request are being removed....");

                                            String my_key = preferenceManager.getString(Constants.KEY_USER_ID);
                                            database.collection(Constants.KEY_COLLECTION_FRIEND_REQUEST)
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            ArrayList<String> allFR = new ArrayList<>();
                                                            if (task.getResult() != null && task.getResult().size() > 0) {
                                                                for (DocumentSnapshot documentSnapshot : task.getResult()) {

                                                                    if (documentSnapshot.getString(Constants.KEY_FR_SENDER_KEY) != null && documentSnapshot.getString(Constants.KEY_FR_RECEIVER_KEY) != null) {
                                                                        if (documentSnapshot.getString(Constants.KEY_FR_SENDER_KEY).equals(my_key) || documentSnapshot.getString(Constants.KEY_FR_RECEIVER_KEY).equals(my_key)) {
                                                                            allFR.add(documentSnapshot.getId());
                                                                        }
                                                                    }

                                                                }
                                                                for (String fr : allFR) {
                                                                    database.collection(Constants.KEY_COLLECTION_FRIEND_REQUEST)
                                                                            .document(fr)
                                                                            .delete();
                                                                }
                                                                binding.deactivatingWarning.append("All friends request are deleted\nDeleting all Friends you made...");
                                                                ArrayList<String> allFriends = new ArrayList<>();
                                                                database.collection(Constants.KEY_FOR_FRIENDS_COLLECTION)
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if (task.getResult() != null && task.getResult().size() > 0) {
                                                                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {

                                                                                        if (documentSnapshot.getString("my_key") != null && documentSnapshot.getString("my_key").equals(my_key)) {
                                                                                            allFriends.add(documentSnapshot.getId());
                                                                                        }

                                                                                    }
                                                                                    for (String f : allFriends) {
                                                                                        database.collection(Constants.KEY_FOR_FRIENDS_COLLECTION)
                                                                                                .document(f)
                                                                                                .delete();
                                                                                    }

                                                                                    binding.deactivatingWarning.append("Deleted all of your friends..\n Done....\n Please fell free to open an another account.");
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });


                                        } else {
                                            binding.deactivatingWarning.setText("You had no posted feeds to delete\n");
                                        }
                                    }
                                });
                    }
                });
    }

    private void doDeactivation() {
        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID))
                .update(Constants.KEY_DEACTIVATION, Constants.KEY_DEACTIVATED)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        findViewById(R.id.imageSignOut).callOnClick();
                    }
                });
    }

    private void deleteProfile() {
        binding.mainView.setVisibility(View.INVISIBLE);
        binding.deactivatePanelId.setVisibility(View.VISIBLE);
    }

    private void editPofileSave() {
        PreferenceManager preferenceManager = new PreferenceManager(this);
        String my_key = preferenceManager.getString(Constants.KEY_USER_ID);

        String new_name, new_uni_id, new_status, new_bio, is_privae_friends, is_private_feeds;
        if (binding.profileEdittextName.getText().toString().length() == 0) {
            binding.profileEdittextName.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_red));
            binding.profileEditPanel.setVisibility(View.VISIBLE);
        } else if (binding.profileEdittextUniversityID.getText().toString().length() == 0) {
            binding.profileEdittextUniversityID.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_red));
            binding.profileEditPanel.setVisibility(View.VISIBLE);
        } else if (binding.profileEdittextStatus.getText().toString().length() == 0) {
            binding.profileEdittextStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_red));
        } else if (binding.profileEdittextMoto.getText().toString().length() == 0) {
            binding.profileEdittextMoto.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_red));
        } else {
            binding.save.setVisibility(View.INVISIBLE);
            binding.editProfile.setVisibility(View.INVISIBLE);
            binding.profileDelete.setVisibility(View.INVISIBLE);
            binding.progressBarSave.setVisibility(View.VISIBLE);

            new_name = binding.profileEdittextName.getText().toString();
            new_uni_id = binding.profileEdittextUniversityID.getText().toString();
            new_status = binding.profileEdittextStatus.getText().toString();
            new_bio = binding.profileEdittextMoto.getText().toString();
            String friend_privacy, feed_privacy;
            if (binding.isPrivateFriend.isChecked()) {
                friend_privacy = Constants.KEY_PRIVATE;
            } else {
                friend_privacy = Constants.KEY_PUBLIC;
            }
            if (binding.isPrivateFeed.isChecked()) {
                feed_privacy = Constants.KEY_PRIVATE;
            } else {
                feed_privacy = Constants.KEY_PUBLIC;
            }


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(Constants.KEY_COLLECTION_USERS)
                    .document(my_key)
                    .update(Constants.KEY_NAME, new_name)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection(Constants.KEY_COLLECTION_USERS)
                                    .document(my_key)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            binding.profileEdittextName.setText(documentSnapshot.getString(Constants.KEY_NAME));
                                            binding.profileEdittextName.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_green));

                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            db.collection(Constants.KEY_COLLECTION_USERS)
                                                    .document(my_key)
                                                    .update(Constants.KEY_UNI_ID, new_uni_id)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                            db.collection(Constants.KEY_COLLECTION_USERS)
                                                                    .document(my_key)
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                            DocumentSnapshot documentSnapshot = task.getResult();
                                                                            binding.profileEdittextUniversityID.setText(documentSnapshot.getString(Constants.KEY_UNI_ID));
                                                                            binding.profileEdittextUniversityID.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_green));


                                                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                            db.collection(Constants.KEY_COLLECTION_USERS)
                                                                                    .document(my_key)
                                                                                    .update(Constants.KEY_STATUS, new_status)
                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                                            db.collection(Constants.KEY_COLLECTION_USERS)
                                                                                                    .document(my_key)
                                                                                                    .get()
                                                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                        @Override
                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                            DocumentSnapshot documentSnapshot = task.getResult();
                                                                                                            binding.profileEdittextStatus.setText(documentSnapshot.getString(Constants.KEY_STATUS));
                                                                                                            binding.profileEdittextStatus.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_green));

                                                                                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                                                            db.collection(Constants.KEY_COLLECTION_USERS)
                                                                                                                    .document(my_key)
                                                                                                                    .update(Constants.KEY_BIO, new_bio)
                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                                                                            db.collection(Constants.KEY_COLLECTION_USERS)
                                                                                                                                    .document(my_key)
                                                                                                                                    .get()
                                                                                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                        @Override
                                                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                            DocumentSnapshot documentSnapshot = task.getResult();
                                                                                                                                            binding.profileEdittextMoto.setText(documentSnapshot.getString(Constants.KEY_BIO));
                                                                                                                                            binding.textBio.setText(documentSnapshot.getString(Constants.KEY_BIO));
                                                                                                                                            binding.profileEdittextMoto.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background_green));
                                                                                                                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                                                                                            db.collection(Constants.KEY_COLLECTION_USERS)
                                                                                                                                                    .document(my_key)
                                                                                                                                                    .update(Constants.KEY_IS_PRIVATE_FRIENDS, friend_privacy)
                                                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                                                                                                            db.collection(Constants.KEY_COLLECTION_USERS)
                                                                                                                                                                    .document(my_key)
                                                                                                                                                                    .get()
                                                                                                                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                                                        @Override
                                                                                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                                                            if (friend_privacy.equals(Constants.KEY_PRIVATE)) {
                                                                                                                                                                                binding.isPrivateFriend.toggle();
                                                                                                                                                                            }
                                                                                                                                                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                                                                                                                            db.collection(Constants.KEY_COLLECTION_USERS)
                                                                                                                                                                                    .document(my_key)
                                                                                                                                                                                    .update(Constants.KEY_IS_PRIVATE_FEED, feed_privacy)
                                                                                                                                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                                        @Override
                                                                                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                                                                                                                                            db.collection(Constants.KEY_COLLECTION_USERS)
                                                                                                                                                                                                    .document(my_key)
                                                                                                                                                                                                    .get()
                                                                                                                                                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                                                                                        @Override
                                                                                                                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                                                                                            if (feed_privacy.equals(Constants.KEY_PRIVATE)) {
                                                                                                                                                                                                                binding.isPrivateFeed.toggle();
                                                                                                                                                                                                            }
                                                                                                                                                                                                            binding.editProfile.setVisibility(View.VISIBLE);
                                                                                                                                                                                                            binding.profileDelete.setVisibility(View.VISIBLE);
                                                                                                                                                                                                            binding.progressBarSave.setVisibility(View.INVISIBLE);
                                                                                                                                                                                                            binding.cloudSaved.setVisibility(View.VISIBLE);
                                                                                                                                                                                                            binding.profileEditPanel.setVisibility(View.GONE);
                                                                                                                                                                                                        }
                                                                                                                                                                                                    });
                                                                                                                                                                                        }
                                                                                                                                                                                    });
                                                                                                                                                                        }
                                                                                                                                                                    });
                                                                                                                                                        }
                                                                                                                                                    });


                                                                                                                                        }
                                                                                                                                    });
                                                                                                                        }
                                                                                                                    });

                                                                                                        }
                                                                                                    });
                                                                                        }
                                                                                    });

                                                                        }
                                                                    });
                                                        }
                                                    });
                                        }
                                    });
                        }
                    });


        }
    }


    private void expandingFeed() {
        PreferenceManager preferenceManager = new PreferenceManager(this);
        String my_key = preferenceManager.getString(Constants.KEY_USER_ID);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_FEEDS)
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<NewsFeed> data = new ArrayList<>();

                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
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
                            if (newsFeed.user_key.equals(my_key)) {
                                data.add(newsFeed);
                            }
                        }
                        NewsFeedAdapter adapter = new NewsFeedAdapter(data, "P");
                        binding.profileFeedsRecView.setLayoutManager(new LinearLayoutManager(this));
                        binding.profileFeedsRecView.setAdapter(adapter);
                        binding.progressBarFeedsRecView.setVisibility(View.INVISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Unable to load feeds", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void expandingFriend() {
        ArrayList<Friend> data = new ArrayList<>();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.getInstance().collection(Constants.KEY_FOR_FRIENDS_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
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
                            if (friend.my_key.equals(my_key) || friend.friend_key.equals(my_key)) {
                                data.add(friend);
                            }
                        }
                        binding.profileFriendsRecView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        binding.profileFriendsRecView.setAdapter(new FriendsAdapter(data));
                        binding.progressBarFriendRecView.setVisibility(View.INVISIBLE);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Unable to load!", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadProfileData() {
        PreferenceManager preferenceManager = new PreferenceManager(this);
        String my_key = preferenceManager.getString(Constants.KEY_USER_ID);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(my_key)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        binding.profileEdittextName.setText(documentSnapshot.getString(Constants.KEY_NAME));
                        binding.profileEdittextUniversityID.setText(documentSnapshot.getString(Constants.KEY_UNI_ID));
                        binding.profileEdittextStatus.setText(documentSnapshot.getString(Constants.KEY_STATUS));
                        String bio = documentSnapshot.getString(Constants.KEY_BIO);
                        if (bio != null) {
                            binding.textBio.setText(bio);
                        }


                        if (documentSnapshot.getString(Constants.KEY_IS_PRIVATE_FRIENDS) != null && documentSnapshot.getString(Constants.KEY_IS_PRIVATE_FRIENDS).equals(Constants.KEY_PRIVATE)) {
                            binding.isPrivateFriend.toggle();
                        }

                        if (documentSnapshot.getString(Constants.KEY_IS_PRIVATE_FEED) != null && documentSnapshot.getString(Constants.KEY_IS_PRIVATE_FEED).equals(Constants.KEY_PRIVATE)) {
                            binding.isPrivateFeed.toggle();
                        }
                    }
                });
    }


    private ActivityMyProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMyProfileBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding.profileEditPanel.setVisibility(View.GONE);
        binding.profileFriendsRecView.setVisibility(View.GONE);
        binding.profileFeedsRecView.setVisibility(View.GONE);
        binding.progressBarSave.setVisibility(View.GONE);
        binding.progressBarFeedsRecView.setVisibility(View.INVISIBLE);
        binding.progressBarFriendRecView.setVisibility(View.INVISIBLE);


        binding.editProfile.setOnClickListener(this);
        binding.collapsingFeed.setOnClickListener(this);
        binding.collapsingFriend.setOnClickListener(this);
        binding.expandingFeed.setOnClickListener(this);
        binding.expandingFriend.setOnClickListener(this);
        binding.save.setOnClickListener(this);
        binding.cloudSaved.setOnClickListener(this);
        binding.profileDelete.setOnClickListener(this);
        binding.isPrivateFeed.setOnClickListener(this);
        binding.isPrivateFriend.setOnClickListener(this);
        binding.progressBarSave.setOnClickListener(this);
        binding.profileDelete.setOnClickListener(this);
        binding.deactivationConfirm.setOnClickListener(this);
        binding.deleteConfirm.setOnClickListener(this);

        loadProfileData();
    }
}