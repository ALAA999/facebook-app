package com.example.acer.facebookapp;

import java.util.Date;

/**
 * Created by acer on 2/3/2018.
 */

public class Messeges {
    private Date time;
    private String messege;
    private String sender_id;
    private String reciver_id;
    private String msg_id;
    boolean read;

    public Messeges(Date time, String messege, String sender_id, String reciver_id, String msg_id, boolean read) {
        this.time = time;
        this.read = read;
        this.msg_id = msg_id;
        this.sender_id = sender_id;
        this.reciver_id = reciver_id;
        this.messege = messege;
    }

    public Messeges(){

    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReciver_id() {
        return reciver_id;
    }

    public void setReciver_id(String reciver_id) {
        this.reciver_id = reciver_id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMessege() {
        return messege;
    }

    public void setMessege(String messege) {
        this.messege = messege;
    }
}
