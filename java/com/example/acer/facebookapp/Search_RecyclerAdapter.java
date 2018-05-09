package com.example.acer.facebookapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by acer on 2/6/2018.
 */

public class Search_RecyclerAdapter  extends RecyclerView.Adapter<Search_RecyclerAdapter.ViewHolder>{
    List<Users> list;
    Context context;

    public  Search_RecyclerAdapter(List<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public Search_RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_recycler_adapter, parent, false);//
        return new Search_RecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Search_RecyclerAdapter.ViewHolder holder, int position) {
        final Users item = list.get(position);
        holder.textView.setText(item.getName());
        Glide.with(context).load(item.getImg_id()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
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
    TextView textView;
    CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.search_imgs);
            textView = itemView.findViewById(R.id.search_textview);
            cardView = itemView.findViewById(R.id.search_cardView);
        }
    }
}
