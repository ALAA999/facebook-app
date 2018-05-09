package com.example.acer.facebookapp;

import java.util.ArrayList;

/**
 * Created by acer on 2/3/2018.
 */

public class Users {//I have converted the data to private to be able to work with genymotion
    private String name;
    private String id;
    private String password;
    private String img_id;
    private ArrayList<Messeges> from;
    private ArrayList<Messeges> to;

    public Users(String name, String id, String password, String img_id, ArrayList<Messeges> from, ArrayList<Messeges> to) {
        this.name = name;
        this.id = id;
        this.password = password;
        this.img_id = img_id;
        this.from = from;
        this.to = to;
    }
    public Users(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }

    public ArrayList<Messeges> getFrom() {
        return from;
    }

    public void setFrom(ArrayList<Messeges> from) {
        this.from = from;
    }

    public ArrayList<Messeges> getTo() {
        return to;
    }

    public void setTo(ArrayList<Messeges> to) {
        this.to = to;
    }
}
