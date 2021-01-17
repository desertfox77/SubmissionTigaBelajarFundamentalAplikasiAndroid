package com.example.subdua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.subdua.adapter.FavoriteAdapter;
import com.example.subdua.database.UserHelper;
import com.example.subdua.model.User;

import java.util.ArrayList;


public class FavoriteActivity extends AppCompatActivity {

    private UserHelper userHelper;
    private ArrayList<User> userGithubList =  new ArrayList<>();
    private FavoriteAdapter favoriteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        TextView textViewError = findViewById(R.id.tv_not_found);
        userHelper = new UserHelper(getApplicationContext());
        userHelper.open();
        userGithubList = userHelper.getDataUser();
        setRecyclerView();
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.favorite_app);
        }
        if(userGithubList.isEmpty()){
            textViewError.setVisibility(View.VISIBLE);
        }

    }

    private void setRecyclerView(){
        RecyclerView rv = findViewById(R.id.rv_user);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        favoriteAdapter = new FavoriteAdapter(getApplicationContext());
        rv.setAdapter(favoriteAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userGithubList = userHelper.getDataUser();
        favoriteAdapter.setUserGithubList(userGithubList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userHelper.close();
    }
}
