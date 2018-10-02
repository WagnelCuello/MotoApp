package com.example.wc2015_0679.motoapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.renderscript.RenderScript;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.wc2015_0679.motoapp.Models.UserModel;
import com.example.wc2015_0679.motoapp.Report.ReportsListActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyRecycleItemViewHolder> {
    private final ArrayList<UserModel> items;
    private final Context context;

    public MyRecyclerAdapter(Context context , ArrayList<UserModel> items){
        this.context = context;
        this.items = items;
    }

    @Override public int getItemViewType(int position){
        return R.layout.layout_users_list;
    }

    @Override public MyRecycleItemViewHolder onCreateViewHolder(ViewGroup parent, int layout) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        MyRecycleItemViewHolder holder = new MyRecycleItemViewHolder(view);
        return holder;
    }
    @Override public void onBindViewHolder(final MyRecyclerAdapter.MyRecycleItemViewHolder holder, int position) {
        final UserModel model = items.get(position);

        // this part of code find image inside FirebaseStorage and put it in an ImageView
        holder.username.setText(model.getBrand());
        Glide.with(context)
                .load(model.getMotoUri())
                .into(holder.imageView);

        // this part of code show options to share the image
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.imageView.buildDrawingCache();
                Bitmap bitmap = holder.imageView.getDrawingCache();
                try {
                    File file = new File(context.getCacheDir(), bitmap + ".png");
                    FileOutputStream fOut = null;
                    fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();

                    file.setReadable(true, false);
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    intent.setType("image/");
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override public int getItemCount() {
        return items.size();
    }

    // when MyRecyclerAdapter start, this class and inside method begin first
    public static class MyRecycleItemViewHolder extends RecyclerView.ViewHolder {
        public TextView username, location, share;
        public ImageView imageView;

        public MyRecycleItemViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tvUsername);
            share = itemView.findViewById(R.id.tvShare);
            imageView = itemView.findViewById(R.id.ivImgPost);
        }
    }
}
