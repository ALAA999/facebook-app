package com.example.acer.facebookapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.acer.facebookapp.News_Fragment.post_list;

/**
 * Created by acer on 2/3/2018.
 */

public class News_RecyclerAdapter extends RecyclerView.Adapter<News_RecyclerAdapter.ViewHolder> {
    List<Post> list;
    Context context;
    public static Comments_RecyclerAdapter recyclerAdapter;
    public static RecyclerView recyclerView;
    public static ArrayList<Comments> comments_toShow;

    public News_RecyclerAdapter(List<Post> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_recycleradapter_post, parent, false);//
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Post item = list.get(position);
        holder.post_msg.setText(item.getMsg());
        holder.date.setText(item.getTime().getHours() + ":" + item.getTime().getMinutes() + " " + DateFormat.format("EEEE", item.getTime()));
        Glide.with(context).load(item.getPost_owenerimg()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
        if (item.getImgurl().equals("")) {
        } else {
            holder.postimg.setVisibility(View.VISIBLE);
            Glide.with(context).load(item.getImgurl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.postimg);
        }
        holder.post_owner.setText(item.getPost_ownername());
        holder.post_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Show_User_page.class);
                i.putExtra("id", item.getid_owner());
                context.startActivity(i);
            }
        });
        //Set num of likes for every post
        try {
            holder.like.setText(item.getUsers_liked().size() + "");
        } catch (Exception e) {
            holder.like.setText("0");
        }
        try {//Set num of comments for every post
            holder.comment.setText(item.getComments().size() + "");
        } catch (Exception e) {
            holder.comment.setText("0");
        }
        holder.like.setOnClickListener(new View.OnClickListener() {//Add a like
            @Override
            public void onClick(View view) {
                like_clicked(item, holder);
            }

        });
        try {//If this user likes this post it will be checked
            for (int i = 0; i < list.size(); i++) {
                //Log.e("ss", item.getUsers_liked().size() + "");
                for (int j = 0; j < item.getUsers_liked().size(); j++) {
                    if (splash_activity.preferences.getString("username_id", "").equals(item.getUsers_liked().get(j))) {
                        holder.like.setChecked(true);
                        break;
                    }
                }
            }
        } catch (Exception e) {
        }
        //Show Comments
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comments_options(item, holder);
            }
        });
        if (item.getid_owner().equals(splash_activity.current_user.getId())) {
            holder.more_vert.setEnabled(true);
            holder.more_vert.setVisibility(View.VISIBLE);
            holder.more_vert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.delte) {
                                UploadingData.delete_post(item);
                                Toast.makeText(context, "Post has been deleted refresh page", Toast.LENGTH_SHORT).show();
                                return true;
                            } else if (menuItem.getItemId() == R.id.update) {
                                final Dialog dilog = new Dialog(context);
                                dilog.setContentView(R.layout.write_post);
                                dilog.setCancelable(true);
                                dilog.setTitle("Write A Post.");
                                dilog.show();
                                (dilog.findViewById(R.id.publihs)).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String post_msg = ((EditText) dilog.findViewById(R.id.posttxt)).getText().toString();
                                        item.setMsg(post_msg);
                                        if (post_msg.isEmpty() && UploadingData.uri == null) {
                                            Toast.makeText(context, "Write Something.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            UploadingData.update_post(item);
                                            dilog.dismiss();
                                            Toast.makeText(context, "Post updated refresh page", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                return true;
                            }
                            return true;
                        }
                    });
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.show();
                }
            });
        }
    }

    public void more_vert() {

    }

    public void like_clicked(Post item, ViewHolder holder) {
        int num = 0;
        ArrayList<String> arrayList = item.getUsers_liked();
        try {
            num = item.getUsers_liked().size();
            Log.e("num", num + "");
        } catch (Exception e) {
            arrayList = new ArrayList();
        }
        if (!holder.like.isChecked()) {//remove a like
            --num;
            arrayList.remove(splash_activity.preferences.getString("username_id", ""));
        } else {//add a like
            ++num;
            arrayList.add(splash_activity.preferences.getString("username_id", ""));
        }
        holder.like.setText(num + "");
        item.setUsers_liked(arrayList);
        UploadingData.update_Note(item.getUsers_liked(), item);
    }

    public void Comments_options(final Post item, final ViewHolder holder) {
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
                    holder.comment.setText(comments.size() + "");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView post_owner, post_msg, date;
        ImageView imageView, postimg, more_vert;
        CheckBox like, comment;

        public ViewHolder(View itemView) {
            super(itemView);
            post_msg = itemView.findViewById(R.id.post_msg);
            post_owner = itemView.findViewById(R.id.postname_owner);
            like = itemView.findViewById(R.id.likes);
            postimg = itemView.findViewById(R.id.post_img);
            date = itemView.findViewById(R.id.post_date);
            comment = itemView.findViewById(R.id.comments);
            imageView = itemView.findViewById(R.id.imgpost_owner);
            more_vert = itemView.findViewById(R.id.more_vert);
        }
    }
}
