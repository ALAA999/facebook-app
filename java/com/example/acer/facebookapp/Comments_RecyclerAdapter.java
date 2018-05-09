package com.example.acer.facebookapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2/9/2018.
 */

public class Comments_RecyclerAdapter extends RecyclerView.Adapter<Comments_RecyclerAdapter.ViewHolder> {
    List<Comments> list;
    Context context;
    Post post;
    News_RecyclerAdapter.ViewHolder post_holder;
    notificationAdapter.ViewHolder post_holders;

    public Comments_RecyclerAdapter(List<Comments> list, Context context, Post post, News_RecyclerAdapter.ViewHolder holder) {
        this.list = list;
        this.post_holder = holder;
        this.context = context;
        this.post = post;
    }

    public Comments_RecyclerAdapter(ArrayList<Comments> list, Context context, Post item, notificationAdapter.ViewHolder holder) {
        this.list = list;
        this.post_holders = holder;
        this.context = context;
        this.post = item;
    }

    @Override
    public Comments_RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comments_recycleradapter, parent, false);//
        return new Comments_RecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Comments item = list.get(position);
        Glide.with(context).load(item.getUrlImage_owner()).diskCacheStrategy(DiskCacheStrategy.ALL).
                into(holder.imgcomment_owner);
        try {
            holder.likes.setText(item.getUsers_liked().size() + "");
        } catch (Exception e) {
            holder.likes.setText("0");
        }
        holder.commetns_msg.setText(item.getComment());
        holder.date.setText(item.getTime().getHours() + ":" + item.getTime().getMinutes() + " " + DateFormat.format("EEEE", item.getTime()));
        holder.likes.setOnClickListener(new View.OnClickListener() {//Add a like
            @Override
            public void onClick(View view) {
                like_clicked(item, holder);
            }

        });
        holder.commentname_owner.setText(item.getComment_owner());
        if (item.getComment_owner().equals(splash_activity.current_user.getName())) {
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    News_RecyclerAdapter.comments_toShow.remove(position);
                    News_RecyclerAdapter.recyclerView.removeViewAt(position);
                    News_RecyclerAdapter.recyclerAdapter.notifyItemRemoved(position);
                    News_RecyclerAdapter.recyclerAdapter.notifyItemRangeChanged(position, News_RecyclerAdapter.comments_toShow.size());                    post.setComments((ArrayList<Comments>) list);
                    post.setComments(News_RecyclerAdapter.comments_toShow);
                    UploadingData.update_post(post);
                    post_holder.comment.setText(News_RecyclerAdapter.comments_toShow.size()+"");
                }
            });
        }
        try {//If this user likes this post it will be checked
            for (int i = 0; i < list.size(); i++) {
                //Log.e("ss", item.getUsers_liked().size() + "");
                for (int j = 0; j < item.getUsers_liked().size(); j++) {
                    if (splash_activity.preferences.getString("username_id", "").equals(item.getUsers_liked().get(j))) {
                        holder.likes.setChecked(true);
                        break;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void like_clicked(Comments item, ViewHolder holder) {
        int num = 0;
        ArrayList<String> arrayList = item.getUsers_liked();
        try {
            num = item.getUsers_liked().size();
            Log.e("num", num + "");
        } catch (Exception e) {
            arrayList = new ArrayList();
        }
        if (!holder.likes.isChecked()) {//remove a like
            --num;
            arrayList.remove(splash_activity.preferences.getString("username_id", ""));
            holder.likes.setText(num + "");
        } else {//add a like
            ++num;
            arrayList.add(splash_activity.preferences.getString("username_id", ""));
            holder.likes.setText(num + "");
        }
        item.setUsers_liked(arrayList);
        for (int i = 0; i < post.getComments().size(); i++) {
            if (post.getComments().get(i).getComment().equals(item.getComment())) {
                post.getComments().get(i).setUsers_liked(item.getUsers_liked());
            }
        }
        UploadingData.update_post(post);
    }


    @Override
    public int getItemCount() {
        try {
            return list.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgcomment_owner, delete;
        CheckBox likes;
        TextView commetns_msg, date, commentname_owner;

        public ViewHolder(View itemView) {
            super(itemView);
            imgcomment_owner = itemView.findViewById(R.id.imgcomment_owner);
            likes = itemView.findViewById(R.id.comment_likes);
            commetns_msg = itemView.findViewById(R.id.comment_msg);
            delete = itemView.findViewById(R.id.delete_mycomment);
            date = itemView.findViewById(R.id.comment_date);
            commentname_owner = itemView.findViewById(R.id.commentname_owner);
        }
    }
}
