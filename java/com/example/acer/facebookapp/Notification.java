package com.example.acer.facebookapp;

import java.util.ArrayList;

/**
 * Created by acer on 2/13/2018.
 */

public class Notification {
    private ArrayList<String> people_react;

    public Notification(ArrayList<String> people_react) {
        this.people_react = people_react;
    }

    public Notification() {

    }

    public ArrayList<String> getPeople_react() {
        return people_react;
    }

    public void setPeople_react(ArrayList<String> people_react) {
        this.people_react = people_react;
    }
}
