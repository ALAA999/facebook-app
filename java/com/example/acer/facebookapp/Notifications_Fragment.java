package com.example.acer.facebookapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class Notifications_Fragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    notificationAdapter adapter;
    DatabaseReference databaseReference;
    public static ArrayList<Notification> note_list;


    public Notifications_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("Notification");
        note_list = new ArrayList<>();
        update_data();
        return view;
    }

    public void update_data() {//Upload All Notifications_Fragment
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                note_list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Notification p = dataSnapshot1.getValue(Notification.class);
                    note_list.add(p);
                }
                setadapter();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public void setadapter() {
        ArrayList<Post> posts = new ArrayList<>();
        for (int i = 0; i < News_Fragment.post_list.size(); i++) {
            try {
                if (News_Fragment.post_list.get(i).getNotification_like().getPeople_react().size() != 0) {
                    posts.add(News_Fragment.post_list.get(i));
                }
            } catch (Exception e) {
            }
        }
        recyclerView = view.findViewById(R.id.notification_rv);
        RecyclerView.LayoutManager mlayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mlayout);
        adapter = new notificationAdapter(posts, getActivity());
        recyclerView.setAdapter(adapter);
    }

    public void All_notes() {

    }
}
