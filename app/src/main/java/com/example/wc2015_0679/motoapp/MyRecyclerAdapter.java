package com.example.wc2015_0679.motoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wc2015_0679.motoapp.Models.UserModel;

import java.util.ArrayList;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyRecycleItemViewHolder> {
    private final ArrayList<UserModel> items;
    private final Context context;

    public MyRecyclerAdapter(Context context , ArrayList<UserModel> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position){
        return R.layout.activity_reports_list;
    }

    @NonNull
    @Override
    public MyRecyclerAdapter.MyRecycleItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int layout) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        MyRecycleItemViewHolder holder = new MyRecycleItemViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapter.MyRecycleItemViewHolder holder, int position) {
        holder.username.setText(items.get(position).getUsername());

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // here all code
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyRecycleItemViewHolder extends RecyclerView.ViewHolder {
        public TextView username, location;

        public MyRecycleItemViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tvUsername);
            location = itemView.findViewById(R.id.tvLocation);
        }
    }
}
