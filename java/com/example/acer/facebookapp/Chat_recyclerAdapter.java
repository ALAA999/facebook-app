package com.example.acer.facebookapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2/7/2018.
 */

public class Chat_recyclerAdapter extends RecyclerView.Adapter<Chat_recyclerAdapter.ViewHolder> {
    List<Users> list;
    Context context;

    public Chat_recyclerAdapter(List<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public Chat_recyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_recycleradapter, parent, false);//
        return new Chat_recyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Chat_recyclerAdapter.ViewHolder holder, int position) {
        final Users item = list.get(position);
        holder.textView.setText(item.getName());
        Glide.with(context).load(item.getImg_id()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
        holder.recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Show_User_page.class);
                i.putExtra("id", item.getId());
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, last_msg;
        CardView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.search_imgsc);
            textView = itemView.findViewById(R.id.search_textviewc);
            recyclerView = itemView.findViewById(R.id.search_cardViewc);
            last_msg = itemView.findViewById(R.id.last_msgc);
        }
    }
}
