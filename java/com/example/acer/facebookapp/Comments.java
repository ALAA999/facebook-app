package com.example.acer.facebookapp;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by acer on 2/3/2018.
 */

public class Comments {
    private String comment;
    private Date time;
    private String comment_owner;
    private ArrayList<String> users_liked;
    private String UrlImage_owner;

    public Comments(String comment, Date time, String comment_owner, ArrayList<String> users_liked, String urlImage_owner) {
        this.comment = comment;
        this.UrlImage_owner = urlImage_owner;
        this.time = time;
        this.comment_owner = comment_owner;
        this.users_liked = users_liked;
    }

    public Comments() {

    }

    public String getUrlImage_owner() {
        return UrlImage_owner;
    }

    public void setUrlImage_owner(String urlImage_owner) {
        UrlImage_owner = urlImage_owner;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getComment_owner() {
        return comment_owner;
    }

    public void setComment_owner(String comment_owner) {
        this.comment_owner = comment_owner;
    }

    public ArrayList<String> getUsers_liked() {
        return users_liked;
    }

    public void setUsers_liked(ArrayList<String> users_liked) {
        this.users_liked = users_liked;
    }
}
