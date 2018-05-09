package com.example.acer.facebookapp;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by acer on 2/3/2018.
 */

public class Post {
    private String id_owner;
    private String post_owenerimg;
    private String post_ownername;
    private Date time;
    private String msg;
    private ArrayList<String> users_liked;
    private String postid;
    private String imgurl;
    private ArrayList<Comments> comments;
    private Notification notification_like, notification_comment;

    public Post(String id_owner, Date time, String msg, ArrayList<String> users_liked,
                ArrayList<Comments> comments, String postid, String imgurl, String post_ownername,
                String post_owenerimg, Notification notification, Notification notification_comment) {
        this.id_owner = id_owner;
        this.notification_comment = notification_comment;
        this.post_ownername = post_ownername;
        this.imgurl = imgurl;
        this.time = time;
        this.notification_like = notification;
        this.post_owenerimg =  post_owenerimg;
        this.msg = msg;
        this.users_liked = users_liked;
        this.comments = comments;
        this.postid = postid;
    }

    public Notification getNotification_like() {
        return notification_like;
    }

    public void setNotification_like(Notification notification) {
        this.notification_like = notification;
    }

    public Notification getNotification_comment() {
        return notification_comment;
    }

    public void setNotification_comment(Notification notification_comment) {
        this.notification_comment = notification_comment;
    }

    public String getPost_owenerimg() {
        return post_owenerimg;
    }

    public void setPost_owenerimg(String post_owenerimg) {
        this.post_owenerimg = post_owenerimg;
    }

    public ArrayList<String> getUsers_liked() {
        return users_liked;
    }

    public void setUsers_liked(ArrayList<String> users_liked) {
        this.users_liked = users_liked;
    }

    public String getPost_ownername() {
        return post_ownername;
    }

    public void setPost_ownername(String post_ownername) {
        this.post_ownername = post_ownername;
    }

    public Post() {

    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getid_owner() {
        return id_owner;
    }

    public void setid_owner(String post_owener) {
        this.id_owner = post_owener;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



    public ArrayList<Comments> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comments> comments) {
        this.comments = comments;
    }
}
