package com.example.acer.facebookapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class Show_User_page extends AppCompatActivity {
    public static News_RecyclerAdapter recyclerAdapter;
    public static RecyclerView recyclerView;
    Users current_user;
    public static String id;
    ArrayList<Post> current_userPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__user_page);
        current_userPosts = new ArrayList<Post>();
        current_user = new Users();
        Bundle b = getIntent().getExtras();
        id = b.getString("id");
        for (int i = 0; i < splash_activity.All_Users.size(); ++i) {
            if (splash_activity.All_Users.get(i).getId().equals(id)) {
                current_user = splash_activity.All_Users.get(i);
                break;
            }
        }
        Button send_msg = findViewById(R.id.msg_searchedUser);
        if (id.equals(splash_activity.preferences.getString("username_id", ""))) {
            send_msg.setEnabled(false);
            send_msg.setVisibility(View.INVISIBLE);
        }else {
            send_msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Show_User_page.this, SendsMsg_activity.class);
                    i.putExtra("id", current_user.getId());
                    startActivity(i);
                }
            });
        }
        ((TextView) findViewById(R.id.user_name_opened)).setText(current_user.getName());
        Glide.with(this).load(current_user.getImg_id()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((ImageView) findViewById(R.id.user_img)));
        for (int i = 0; i < News_Fragment.post_list.size(); ++i) {
            if (News_Fragment.post_list.get(i).getid_owner().equals(current_user.getId())) {
                current_userPosts.add(News_Fragment.post_list.get(i));
            }
        }
        recyclerView = findViewById(R.id.userposts_rv);
        RecyclerView.LayoutManager mlayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mlayout);
        recyclerAdapter = new News_RecyclerAdapter(current_userPosts, this);
        recyclerView.setAdapter(recyclerAdapter);
    }
}
