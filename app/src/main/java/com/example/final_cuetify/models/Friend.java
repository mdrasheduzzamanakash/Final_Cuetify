package com.example.final_cuetify.models;

public class Friend {
    public String myName, friend_name, my_image, friend_imege, my_key, friend_key, my_id, friend_id, type = "ordinary";

    @Override
    public String toString() {
        return "Friend{" +
                "myName='" + myName + '\'' +
                ", friend_name='" + friend_name + '\'' +
                ", my_image='" + my_image + '\'' +
                ", friend_imege='" + friend_imege + '\'' +
                ", my_key='" + my_key + '\'' +
                ", friend_key='" + friend_key + '\'' +
                ", my_id='" + my_id + '\'' +
                ", friend_id='" + friend_id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getMy_image() {
        return my_image;
    }

    public void setMy_image(String my_image) {
        this.my_image = my_image;
    }

    public String getFriend_imege() {
        return friend_imege;
    }

    public void setFriend_imege(String friend_imege) {
        this.friend_imege = friend_imege;
    }

    public String getMy_key() {
        return my_key;
    }

    public void setMy_key(String my_key) {
        this.my_key = my_key;
    }

    public String getFriend_key() {
        return friend_key;
    }

    public void setFriend_key(String friend_key) {
        this.friend_key = friend_key;
    }

    public String getMy_id() {
        return my_id;
    }

    public void setMy_id(String my_id) {
        this.my_id = my_id;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
