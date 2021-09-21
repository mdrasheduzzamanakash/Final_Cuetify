package com.example.final_cuetify.models;

public class Reaction {
    public String reacter_name, reacter_KEY, reacter_image, reacter_id, feed_id, isLiked = "none", isLoved = "none", isAngry = "none", isSad = "none", reaction_key_firestore, ac_reaction;

    @Override
    public String toString() {
        return "Reaction{" +
                "reacter_name='" + reacter_name + '\'' +
                ", reacter_KEY='" + reacter_KEY + '\'' +
                ", reacter_image='" + reacter_image + '\'' +
                ", reacter_id='" + reacter_id + '\'' +
                ", feed_id='" + feed_id + '\'' +
                ", isLiked='" + isLiked + '\'' +
                ", isLoved='" + isLoved + '\'' +
                ", isAngry='" + isAngry + '\'' +
                ", isSad='" + isSad + '\'' +
                ", reaction_key_firestore='" + reaction_key_firestore + '\'' +
                ", ac_reaction='" + ac_reaction + '\'' +
                '}';
    }
}
