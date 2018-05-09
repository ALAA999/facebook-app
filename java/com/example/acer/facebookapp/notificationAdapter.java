package com.example.acer.facebookapp;

import android.app.Dialog;
import android.content.Context;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by acer on 2/13/2018.
 */

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.ViewHolder> {
    List<Post> list;
    Context context;

    public notificationAdapter(List<Post> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public notificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notificationrecycleradapter, parent, false);//
        return new notificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final notificationAdapter.ViewHolder holder, final int position) {
        final Post item = list.get(position);
        String names = "people like " + item.getPost_ownername() + "'s post: ";
        Glide.with(context).load(item.getPost_owenerimg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
        holder.post_time.setText(item.getTime().getHours() + ":" + item.getTime().getMinutes() + " " + DateFormat.format("EEEE", item.getTime()));
        for (int i = 0; i < splash_activity.All_Users.size(); i++) {
            for (int j = 0; j < item.getNotification_like().getPeople_react().size(); j++) {
                if (item.getNotification_like().getPeople_react().get(j).equals(splash_activity.All_Users.get(i).getId())) {
                    names = names + (splash_activity.All_Users.get(i).getName() + ", ");
                }
            }
        }
        holder.post_msgs.setText(item.getMsg());
        holder.people_like.append(names.substring(0, names.length() - 2));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comments_RecyclerAdapter recyclerAdapter;
                RecyclerView recyclerView;
                ArrayList<Comments> comments_toShow;
                final Dialog dilog = new Dialog(context);
                dilog.setContentView(R.layout.comments_dialog);
                dilog.setCancelable(true);
                dilog.setTitle("Comments.");
                dilog.show();
                ((TextView) dilog.findViewById(R.id.titlepostname_owner)).setText(item.getPost_ownername());
                ((TextView) dilog.findViewById(R.id.titlepost_date)).setText(item.getTime().getHours() + ":" + item.getTime().getMinutes() + " " + DateFormat.format("EEEE", item.getTime()));
                ((TextView) dilog.findViewById(R.id.titlepost_msg)).setText(item.getMsg());
                Glide.with(context).load(item.getImgurl()).diskCacheStrategy(DiskCacheStrategy.ALL).
                        into(((ImageView) dilog.findViewById(R.id.titlepost_img)));
                Glide.with(context).load(item.getPost_owenerimg()).diskCacheStrategy(DiskCacheStrategy.ALL).
                        into(((ImageView) dilog.findViewById(R.id.titleimgpost_owner)));
                final TextView people_liked = dilog.findViewById(R.id.num_likedPost);
                final Button more = dilog.findViewById(R.id.see_more);

                comments_toShow = item.getComments();
                recyclerView = dilog.findViewById(R.id.comments_rv);
                if (comments_toShow != null) {
                    Collections.reverse(comments_toShow);
                }
                RecyclerView.LayoutManager mlayout = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(mlayout);
                recyclerAdapter = new Comments_RecyclerAdapter(comments_toShow, context, item, holder);
                recyclerView.setAdapter(recyclerAdapter);
                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        more.setEnabled(false);
                        more.setVisibility(View.INVISIBLE);
                        try {//Show people how like this post
                            String names = "";
                            for (int i = 0; i < item.getUsers_liked().size(); i++) {
                                for (int j = 0; j < splash_activity.All_Users.size(); j++) {
                                    if (splash_activity.All_Users.get(j).getId().equals(item.getUsers_liked().get(i))) {
                                        names = names + (splash_activity.All_Users.get(j).getName() + ", ");
                                    }
                                }
                            }
                            people_liked.append(names.substring(0, names.length() - 2));
                        } catch (Exception e) {
                        }
                    }
                });
                dilog.findViewById(R.id.comment_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {//Adding a new Comment
                        String my_comment = ((EditText) dilog.findViewById(R.id.write_comment)).getText().toString();
                        if (my_comment.isEmpty()) {
                            Toast.makeText(context, "Write Something", Toast.LENGTH_SHORT).show();
                        } else {
                            final ArrayList<String> users_liked = new ArrayList<>();
                            ArrayList<Comments> comments;
                            try {//If no comments were written the app will crash so try{}catch(){}Solves the problem
                                comments = item.getComments();
                                comments.add(new Comments(my_comment, Calendar.getInstance().getTime(),
                                        splash_activity.preferences.getString("username", null),
                                        users_liked, splash_activity.current_user.getImg_id()));
                            } catch (Exception e) {//It will happen only when no Comments have been written
                                comments = new ArrayList<>();
                                comments.add(new Comments(my_comment, Calendar.getInstance().getTime(),
                                        splash_activity.preferences.getString("username", null),
                                        users_liked, splash_activity.current_user.getImg_id()));
                            }
                            item.setComments(comments);
                            UploadingData.update_post(item);
                            dilog.dismiss();
                        }
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        RelativeLayout relativeLayout;
        TextView post_time, people_like, post_msgs;

        public ViewHolder(View itemView) {
            super(itemView);
            post_msgs = itemView.findViewById(R.id.post_msgs);
            imageView = itemView.findViewById(R.id.note_img);
            post_time = itemView.findViewById(R.id.post_time);
            relativeLayout = itemView.findViewById(R.id.relative_notes);
            people_like = itemView.findViewById(R.id.people_likeit);
        }
    }
}