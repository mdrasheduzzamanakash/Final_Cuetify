package com.example.final_cuetify.models;

import java.io.Serializable;

public class NewsFeed implements Serializable {
    public String name;
    public String uni_id;
    public String email;
    public String message;
    public String status;
    public String image;
    public String comments;
    public String user_key;
    public String feed_id;

    public long like = 0, love = 0, angry = 0, sad = 0, cnt_comment = 0;

    public String getSum() {
        long sum = like + love + angry + sad+cnt_comment;
        return Long.toString(sum);
    }

}
