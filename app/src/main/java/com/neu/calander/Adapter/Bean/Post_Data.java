package com.neu.calander.Adapter.Bean;

import java.util.ArrayList;
import java.util.List;

public class Post_Data {
    private String User_id;
    private String content;
    private String user_name;
    private List<String> image;
    private String time;
    private String User_image;
    private int like=0;
    private int reply=0;
    private int id;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    //private List<reply_user> reply_users;

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Post_Data{" +
                "User_id='" + User_id + '\'' +
                ", content='" + content + '\'' +
                ", image=" + image +
                ", time='" + time + '\'' +
                '}';
    }

    public String getUser_image() {
        return User_image;
    }

    public void setUser_image(String user_image) {
        User_image = user_image;
    }
}
