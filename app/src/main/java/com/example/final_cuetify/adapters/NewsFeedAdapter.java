package com.example.final_cuetify.adapters;

import android.media.Image;
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
import com.example.final_cuetify.utilities.Constants;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> {
    private ArrayList<NewsFeed> data;

    public NewsFeedAdapter(ArrayList<NewsFeed> data) {
        this.data = data;
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
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView name, email, id, message, cnt, status, feedID;
        private LottieAnimationView like, love, angry, sad, comment;
        private RecyclerView comment_rec_view;
        private ImageView add_comment_button;
        private EditText commentWrittingEditText;
        private LinearLayout showCommentView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.news_feed_name);
            email = itemView.findViewById(R.id.news_feed_email);
            id = itemView.findViewById(R.id.news_feed_id);
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
                    // add a like
                    break;
                case R.id.news_feed_love:
                    // add a love
                case R.id.news_feed_angry:
                    // add an angry
                case R.id.news_feed_sad:
                    // add a sad
                case R.id.news_feed_comment:
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

            HashMap<String, Object> comment = new HashMap<>();

            comment.put(Constants.KEY_COMMENTER_NAME, name.getText().toString());
            comment.put(Constants.KEY_COMMENT_FEED_ID, feedID.getText().toString());
            comment.put(Constants.KEY_COMMENTER_MESSAGE, commentWrittingEditText.getText().toString());
            comment.put(Constants.KEY_COMMENTER_ID, id.getText().toString());
            comment.put(Constants.KEY_COMMENTER_STATUS, status.getText().toString());

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
            itemView.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
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
                                itemView.findViewById(R.id.progressBar).setVisibility(View.GONE);
                                itemView.findViewById(R.id.hidden_comment_layout).setVisibility(View.VISIBLE);
                            }
                        }  else {
                            itemView.findViewById(R.id.progressBar).setVisibility(View.GONE);
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