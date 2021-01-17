package com.example.subdua.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.subdua.R;
import com.example.subdua.UserDetailActivity;
import com.example.subdua.database.UserHelper;
import com.example.subdua.model.User;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.UserViewHolder> {
    private static final String aku = "LEO";
    private UserHelper userHelper;
    private User comeon;
    private Context c;
    private ArrayList<User> users;


    public FavoriteAdapter(Context c) {
        this.c = c;
    }

    public void setUserGithubList(ArrayList<User> userGithubArrayList){
        this.users = userGithubArrayList;
        userHelper = new UserHelper(this.c);
    }

    @NonNull
    @Override
    public FavoriteAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_favorite,parent,false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User up = users.get(position);
        holder.textView1.setText(up.getLogin());
        holder.textView2.setText(up.getHtmlUrl());
        Glide.with(holder.itemView.getContext())
                .load(up.getAvatarUrl())
                .into(holder.imageView);
        holder.imageViewDelete.setOnClickListener(view -> {
            final AlertDialog.Builder alert = new AlertDialog.Builder(holder.itemView.getContext());
            alert.setTitle("Remove Confirmation");
            alert.setMessage("Are you sure?");
            alert.setCancelable(false);
            alert.setPositiveButton("Yes", (dialogInterface, i) -> {
                userHelper.open();
                comeon = users.get(position);
                users = userHelper.getDataUser();
                userHelper.userDelete(String.valueOf(comeon.getId()));
                users.remove(position);
                notifyDataSetChanged();
                Snackbar.make(view, "Success Delete", Snackbar.LENGTH_SHORT).show();
            });
            alert.setNegativeButton("No", ((dialogInterface, i) -> alert.setCancelable(true)));
            alert.show();
        });
    }



    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView, imageViewDelete;
        TextView textView1,textView2;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_user_detail_row);
            textView1 = itemView.findViewById(R.id.tv_username_detail_row);
            textView2 = itemView.findViewById(R.id.tv_htmlurl_detail_row);
            imageViewDelete = itemView.findViewById(R.id.imageView_delete);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            User up = users.get(getAdapterPosition());
            Intent intent = new Intent(c, UserDetailActivity.class);
            intent.putExtra(aku,up);
            v.getContext().startActivity(intent);
        }
    }
}
