package com.example.acer.facebookapp;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by acer on 2/10/2018.
 */

public class Msg_recyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Messeges> list;
    Context context;
    String currnet_img;
    String called_img;

    public Msg_recyclerAdapter(List<Messeges> list, Context context, String currnet_img, String called_img) {
        this.list = list;
        this.context = context;
        this.called_img = called_img;
        this.currnet_img = currnet_img;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.msg_recycleradaptercaller, parent, false);//
            return new Msg_recyclerAdapter.ViewHolder0(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.msg_recycleradapterreciver, parent, false);//
            return new Msg_recyclerAdapter.ViewHolder1(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Messeges item = list.get(position);
        if (holder.getItemViewType() == 0) {
            ViewHolder0 holder0 = (ViewHolder0) holder;
            Glide.with(context).load(called_img).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder0.imageView);
            holder0.msg.setText(item.getMessege());
            holder0.time.setText(item.getTime().getHours() + ":" + item.getTime().getMinutes() + " " + DateFormat.format("EEEE", item.getTime()));
        } else {
            ViewHolder1 holder1 = (ViewHolder1) holder;
            Glide.with(context).load(currnet_img).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder1.imageView);
            holder1.msg.setText(item.getMessege());
            holder1.time.setText(item.getTime().getHours() + ":" + item.getTime().getMinutes() + " " + DateFormat.format("EEEE", item.getTime()));

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getReciver_id().equals(splash_activity.current_user.getId())) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder0 extends RecyclerView.ViewHolder {
        TextView msg, time;
        CardView cardView;
        ImageView imageView;

        public ViewHolder0(View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.this_msg);
            time = itemView.findViewById(R.id.this_msgtime);
            cardView = itemView.findViewById(R.id.msg_card_view);
            imageView = itemView.findViewById(R.id.msg_img);
        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView msg, time;
        CardView cardView;
        ImageView imageView;

        public ViewHolder1(View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.this_msg);
            time = itemView.findViewById(R.id.this_msgtime);
            cardView = itemView.findViewById(R.id.msg_card_view);
            imageView = itemView.findViewById(R.id.msg_img);
        }
    }
}
