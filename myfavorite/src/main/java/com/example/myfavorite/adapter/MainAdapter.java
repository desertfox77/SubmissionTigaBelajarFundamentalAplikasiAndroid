package com.example.myfavorite.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myfavorite.R;
import com.example.myfavorite.model.User;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.UserViewHolder> {
    private Cursor cursor;
    private Context context;


    public MainAdapter(Context context) {
        this.context = context;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    private User getItemData(int position){
        if (!cursor.moveToPosition(position)){
            throw new IllegalStateException("ILLEGAL");
        }
        return new User(cursor);
    }


    @NonNull
    @Override
    public MainAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.UserViewHolder holder, int position) {
        User up = getItemData(position);
        holder.textView1.setText(up.getLogin());
        holder.textView2.setText(up.getHtmlUrl());
        Glide.with(holder.itemView.getContext())
                .load(up.getAvatarUrl())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if (cursor == null) return 0;
        return cursor.getCount();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1,textView2;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_user_detail_row);
            textView1 = itemView.findViewById(R.id.tv_username_detail_row);
            textView2 = itemView.findViewById(R.id.tv_htmlurl_detail_row);

        }

    }
}
