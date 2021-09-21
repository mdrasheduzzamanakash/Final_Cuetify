package com.example.final_cuetify.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.final_cuetify.R;
import com.example.final_cuetify.models.Comments;
import com.example.final_cuetify.models.NewsFeed;
import com.example.final_cuetify.models.Reaction;
import com.example.final_cuetify.utilities.Constants;
import com.example.final_cuetify.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> {
    private ArrayList<NewsFeed> data;
    Boolean isLiked, isLoved, is_Sad, is_Angry;
    Boolean isForProfile = false;
    private Context context;
    public NewsFeedAdapter(ArrayList<NewsFeed> data, Context context) {
        this.data = data;
        this.context = context;
    }
    public NewsFeedAdapter(ArrayList<NewsFeed> data, String s, Context context) {
        this.data = data;
        isForProfile = true;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_feed, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(this.data.get(position).name);
        holder.status.setText(this.data.get(position).status);
        holder.email.setText(this.data.get(position).email);
        holder.id.setText(this.data.get(position).uni_id);
        holder.message.setText(this.data.get(position).message);
        holder.cnt.setText(data.get(position).getSum());
        holder.feedID.setText(data.get(position).feed_id);

        if(isForProfile) {
            holder.like.setVisibility(View.INVISIBLE);
        }
        PreferenceManager preferenceManager = new PreferenceManager(context.getApplicationContext());

        byte[] bytes = Base64.decode(data.get(holder.getAdapterPosition()).image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        holder.feed_image.setImageBitmap(bitmap);




        FirebaseDatabase.getInstance().getReference(Constants.KEY_REACTIONS_COLLECTIONS).child(data.get(holder.getAdapterPosition()).feed_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.cnt.setText(Long.toString(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(Constants.KEY_REACTIONS_COLLECTIONS);

                Reaction reaction = new Reaction();
                reaction.reacter_KEY = preferenceManager.getString(Constants.KEY_USER_ID);
                reaction.reacter_id = preferenceManager.getString(Constants.KEY_UNI_ID);
                reaction.reacter_name = preferenceManager.getString(Constants.KEY_NAME);
                reaction.reacter_image = preferenceManager.getString(Constants.KEY_IMAGE);
                reaction.isLiked = "yes";
                myRef.child(data.get(holder.getAdapterPosition()).feed_id)
                        .child(preferenceManager.getString(Constants.KEY_USER_ID))
                        .setValue(reaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        refresh();
                    }

                    private void refresh() {
                        myRef.child(data.get(holder.getAdapterPosition()).feed_id)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        holder.cnt.setText(Long.toString(snapshot.getChildrenCount()));
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                    }
                });
            }
        });

        holder.love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(Constants.KEY_REACTIONS_COLLECTIONS);

                Reaction reaction = new Reaction();
                reaction.reacter_KEY = preferenceManager.getString(Constants.KEY_USER_ID);
                reaction.reacter_id = preferenceManager.getString(Constants.KEY_UNI_ID);
                reaction.reacter_name = preferenceManager.getString(Constants.KEY_NAME);
                reaction.reacter_image = preferenceManager.getString(Constants.KEY_IMAGE);
                reaction.isLoved = "yes";
                myRef.child(data.get(holder.getAdapterPosition()).feed_id)
                        .child(preferenceManager.getString(Constants.KEY_USER_ID))
                        .setValue(reaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        refresh();
                    }

                    private void refresh() {
                        myRef.child(data.get(holder.getAdapterPosition()).feed_id)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        holder.cnt.setText(Long.toString(snapshot.getChildrenCount()));
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                    }
                });
            }
        });


        holder.angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(Constants.KEY_REACTIONS_COLLECTIONS);

                Reaction reaction = new Reaction();
                reaction.reacter_KEY = preferenceManager.getString(Constants.KEY_USER_ID);
                reaction.reacter_id = preferenceManager.getString(Constants.KEY_UNI_ID);
                reaction.reacter_name = preferenceManager.getString(Constants.KEY_NAME);
                reaction.reacter_image = preferenceManager.getString(Constants.KEY_IMAGE);
                reaction.isAngry = "yes";
                myRef.child(data.get(holder.getAdapterPosition()).feed_id)
                        .child(preferenceManager.getString(Constants.KEY_USER_ID))
                        .setValue(reaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        refresh();
                    }

                    private void refresh() {
                        myRef.child(data.get(holder.getAdapterPosition()).feed_id)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        holder.cnt.setText(Long.toString(snapshot.getChildrenCount()));
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                    }
                });
            }
        });


        holder.sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(Constants.KEY_REACTIONS_COLLECTIONS);

                Reaction reaction = new Reaction();
                reaction.reacter_KEY = preferenceManager.getString(Constants.KEY_USER_ID);
                reaction.reacter_id = preferenceManager.getString(Constants.KEY_UNI_ID);
                reaction.reacter_name = preferenceManager.getString(Constants.KEY_NAME);
                reaction.reacter_image = preferenceManager.getString(Constants.KEY_IMAGE);
                reaction.isSad = "yes";
                myRef.child(data.get(holder.getAdapterPosition()).feed_id)
                        .child(preferenceManager.getString(Constants.KEY_USER_ID))
                        .setValue(reaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        refresh();
                    }

                    private void refresh() {
                        myRef.child(data.get(holder.getAdapterPosition()).feed_id)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        holder.cnt.setText(Long.toString(snapshot.getChildrenCount()));
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                    }
                });
            }
        });


        
        holder.see_all_reactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.showCommentView.setVisibility(View.GONE);
                holder.rec_reactions.setVisibility(View.VISIBLE);

                // fatching data from firebase
                FirebaseDatabase.getInstance().getReference(Constants.KEY_REACTIONS_COLLECTIONS)
                        .child(data.get(holder.getAdapterPosition()).feed_id)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                ArrayList<Reaction> reactions = new ArrayList<>();
                                long cnt = snapshot.getChildrenCount();
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Reaction reaction = new Reaction();
                                    reaction.isSad = dataSnapshot.child("isSad").getValue(String.class);
                                    reaction.isLoved = dataSnapshot.child("isLoved").getValue(String.class);
                                    reaction.isAngry = dataSnapshot.child("isAngry").getValue(String.class);
                                    reaction.isLiked = dataSnapshot.child("isLiked").getValue(String.class);
                                    reaction.reacter_name = dataSnapshot.child("reacter_name").getValue(String.class);
                                    reaction.reacter_id = dataSnapshot.child("reacter_id").getValue(String.class);
                                    reaction.reacter_image = dataSnapshot.child("reacter_image").getValue(String.class);
                                    reaction.reacter_KEY = dataSnapshot.child("reacter_KEY").getValue(String.class);
                                    reactions.add(reaction);
                                }
                                if(reactions.size() == cnt) {

                                    // adapter work
                                    ReactionAdapter adapter = new ReactionAdapter(reactions);
                                    adapter.notifyDataSetChanged();
                                    holder.rec_reactions.setLayoutManager(new LinearLayoutManager(context));
                                    holder.rec_reactions.setAdapter(adapter);
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });
        

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView name, email, id, message, cnt, status, feedID;
        private LottieAnimationView like, love, angry, sad, comment;
        private RecyclerView comment_rec_view,rec_reactions;
        private ImageView add_comment_button,see_all_reactions;
        private EditText commentWrittingEditText;
        private LinearLayout showCommentView;
        private RoundedImageView feed_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            feed_image = itemView.findViewById(R.id.news_feed_profile_image);
            name = itemView.findViewById(R.id.news_feed_name);
            email = itemView.findViewById(R.id.news_feed_email);
            id = itemView.findViewById(R.id.news_feed_id);
            rec_reactions = itemView.findViewById(R.id.rec_reactions);
            message = itemView.findViewById(R.id.news_feed_message);
            status = itemView.findViewById(R.id.news_feed_status);
            cnt = itemView.findViewById(R.id.total_count_news_feed);

            like = itemView.findViewById(R.id.news_feed_like);
            love = itemView.findViewById(R.id.news_feed_love);
            angry = itemView.findViewById(R.id.news_feed_angry);
            sad = itemView.findViewById(R.id.news_feed_sad);
            comment = itemView.findViewById(R.id.news_feed_comment);
            comment_rec_view = itemView.findViewById(R.id.news_feed_comment_recycler_view);
            add_comment_button = itemView.findViewById(R.id.addCommentButton);
            feedID = itemView.findViewById(R.id.feedIdTextView);
            commentWrittingEditText = itemView.findViewById(R.id.news_feed_add_comment_edit_text);
            showCommentView = itemView.findViewById(R.id.hidden_comment_layout);
            see_all_reactions = itemView.findViewById(R.id.allreactions);

            like.setOnClickListener(this);
            love.setOnClickListener(this);
            angry.setOnClickListener(this);
            sad.setOnClickListener(this);
            comment.setOnClickListener(this);
            like.setOnLongClickListener(this);
            add_comment_button.setOnClickListener(this);
            showCommentView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.news_feed_like:
                    like.playAnimation();
                    break;
                case R.id.news_feed_love:
                    love.playAnimation();
                    break;
                    // add a love
                case R.id.news_feed_angry:
                    angry.playAnimation();
                    break;
                    // add an angry
                case R.id.news_feed_sad:
                    sad.playAnimation();
                    break;
                    // add a sad
                case R.id.news_feed_comment:
                    comment.playAnimation();
                    loadComment();
                    break;
                case R.id.addCommentButton:
                    if (!commentWrittingEditText.getText().toString().isEmpty()) {
                        itemView.findViewById(R.id.addCommentButton).setVisibility(View.INVISIBLE);
                        itemView.findViewById(R.id.progressBarAddComment).setVisibility(View.VISIBLE);
                        writeComment();
                    } else {
                        Toast.makeText(v.getContext(), "Write comment First", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.news_feed_like:
                    itemView.findViewById(R.id.news_feed_hidden_reaction).setVisibility(View.VISIBLE);
                    return true;
                default:
                    return true;
            }
        }


        public void writeComment() {
            PreferenceManager preferenceManager = new PreferenceManager(itemView.getContext());

            HashMap<String, Object> comment = new HashMap<>();

            comment.put(Constants.KEY_COMMENTER_NAME, name.getText().toString());
            comment.put(Constants.KEY_COMMENT_FEED_ID, feedID.getText().toString());
            comment.put(Constants.KEY_COMMENTER_MESSAGE, commentWrittingEditText.getText().toString());
            comment.put(Constants.KEY_COMMENTER_ID, id.getText().toString());
            comment.put(Constants.KEY_COMMENTER_STATUS, status.getText().toString());
            comment.put(Constants.KEY_COMMENTER_IMAGE, preferenceManager.getString(Constants.KEY_IMAGE));

            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(Constants.KEY_COMMENTS_COLLECTION)
                    .add(comment)
                    .addOnSuccessListener(documentReference -> {
                        itemView.findViewById(R.id.addCommentButton).setVisibility(View.VISIBLE);
                        itemView.findViewById(R.id.progressBarAddComment).setVisibility(View.INVISIBLE);
                        loadComment();
                    })
                    .addOnFailureListener(exception -> {
                        Toast.makeText(itemView.getContext(), "Unable to comment", Toast.LENGTH_SHORT).show();
                    });
        }


        public void loadComment() {
            itemView.findViewById(R.id.progressBarSave).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.hidden_comment_layout).setVisibility(View.GONE);

            String feed_id = feedID.getText().toString();
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(Constants.KEY_COMMENTS_COLLECTION)
                    .whereEqualTo(Constants.KEY_COMMENT_FEED_ID, feed_id)
                    .get()
                    .addOnCompleteListener(task -> {
                        ArrayList<Comments> data = new ArrayList<>();
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                Comments comment = new Comments();
                                comment.comment_message = queryDocumentSnapshot.getString(Constants.KEY_COMMENTER_MESSAGE);
                                comment.comment_status = queryDocumentSnapshot.getString(Constants.KEY_COMMENTER_STATUS);
                                comment.commenterId = queryDocumentSnapshot.getString(Constants.KEY_COMMENTER_ID);
                                comment.commenterName = queryDocumentSnapshot.getString(Constants.KEY_COMMENTER_NAME);
                                comment.commenterImage = queryDocumentSnapshot.getString(Constants.KEY_COMMENTER_IMAGE);
                                data.add(comment);

                                CommentAdapter adapter = new CommentAdapter(data);
                                adapter.notifyDataSetChanged();
                                comment_rec_view.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
                                comment_rec_view.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                                    @Override
                                    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                                        int action = e.getAction();
                                        switch (action) {
                                            case MotionEvent.ACTION_MOVE:
                                                rv.getParent().requestDisallowInterceptTouchEvent(true);
                                                break;
                                        }
                                        return false;
                                    }

                                    @Override
                                    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                                    }

                                    @Override
                                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                                    }
                                });
                                comment_rec_view.setAdapter(new CommentAdapter(data));
                                itemView.findViewById(R.id.progressBarSave).setVisibility(View.GONE);
                                itemView.findViewById(R.id.hidden_comment_layout).setVisibility(View.VISIBLE);
                            }
                        }  else {
                            itemView.findViewById(R.id.progressBarSave).setVisibility(View.GONE);
                            itemView.findViewById(R.id.hidden_comment_layout).setVisibility(View.VISIBLE);
                        }
                    })
                    .addOnFailureListener(exception -> {
                        Toast.makeText(itemView.getContext(), "Unable to load comments", Toast.LENGTH_SHORT).show();
                        itemView.findViewById(R.id.hidden_comment_layout).setVisibility(View.VISIBLE);
                    });
        }
    }

}
