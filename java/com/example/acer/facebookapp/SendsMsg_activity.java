package com.example.acer.facebookapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class SendsMsg_activity extends AppCompatActivity {
    Users current_usercalled;
    ArrayList<Messeges> All_Messeges;
    RecyclerView recyclerView;
    Msg_recyclerAdapter msg_recyclerAdapter;
    DatabaseReference databaseReference;
    Users current_user;
    public static String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sends_msg_activity);
        recyclerView = findViewById(R.id.messeges_rv);
        findViewById(R.id.back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        current_usercalled = new Users();
        current_user = splash_activity.current_user;
        Bundle b = getIntent().getExtras();
        id = b.getString("id");
        for (int i = 0; i < splash_activity.All_Users.size(); ++i) {
            if (splash_activity.All_Users.get(i).getId().equals(id)) {
                current_usercalled = splash_activity.All_Users.get(i);
                break;
            }
        }
        Glide.with(SendsMsg_activity.this).load(current_usercalled.getImg_id()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((ImageView) findViewById(R.id.usercalled_img));
        ((TextView) findViewById(R.id.usercalled_name)).setText(current_usercalled.getName());
        findViewById(R.id.send_mymsg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View views) {
                String msg = ((EditText) findViewById(R.id.mymsg_edittext)).getText().toString();
                if (msg.isEmpty()) {
                    Toast.makeText(SendsMsg_activity.this, "Write Something", Toast.LENGTH_SHORT).show();
                } else {
                    Date currentTime = Calendar.getInstance().getTime();
                    UploadingData.send_msg(msg, current_user.getId(), current_usercalled.getId(), currentTime);
                    Collections.reverse(All_Messeges);
                    try {
                        All_Messeges.get(0).setRead(true);
                        UploadingData.update_msg(All_Messeges.get(0));
                    } catch (Exception e) {

                    }
                    Collections.reverse(All_Messeges);
                    ((EditText) findViewById(R.id.mymsg_edittext)).setText("");
                }
            }
        });
        upload_messeges();
    }

    @Override
    protected void onStart() {
        super.onStart();
        upload_messeges();
    }

    public void upload_messeges() {
        All_Messeges = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Messeges");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                All_Messeges.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Messeges p = dataSnapshot1.getValue(Messeges.class);
                    if ((p.getSender_id().equals(current_user.getId())
                            && p.getReciver_id().equals(current_usercalled.getId()))
                            || (p.getSender_id().equals(current_usercalled.getId())
                            && p.getReciver_id().equals(current_user.getId()))) {
                        All_Messeges.add(p);
                    }
                }
                RecyclerView.LayoutManager mlayout = new LinearLayoutManager(SendsMsg_activity.this);
                recyclerView.setLayoutManager(mlayout);
                msg_recyclerAdapter = new Msg_recyclerAdapter(All_Messeges, SendsMsg_activity.this,
                        current_user.getImg_id(), current_usercalled.getImg_id());
                recyclerView.setAdapter(msg_recyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
