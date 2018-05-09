package com.example.acer.facebookapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
public class News_Fragment extends Fragment {
    View view;
    public static News_RecyclerAdapter recyclerAdapter;
    public static RecyclerView recyclerView;
    DatabaseReference databaseReference;
    public static ArrayList<Post> post_list;

    public News_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_, container, false);
        recyclerView = view.findViewById(R.id.rv);
        databaseReference = FirebaseDatabase.getInstance().getReference("Post");
        post_list = new ArrayList<>();
        update_data();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        update_data();
    }

    public void update_data() {//Upload All Posts
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post_list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Post p = dataSnapshot1.getValue(Post.class);
                    post_list.add(p);
                }
                //we can get the data exactly after this point.
                //Log.e("posts_num", post_list.size() + "");
                Collections.reverse(post_list);
                RecyclerView.LayoutManager mlayout = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mlayout);
                recyclerAdapter = new News_RecyclerAdapter(post_list, getActivity());
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


}
