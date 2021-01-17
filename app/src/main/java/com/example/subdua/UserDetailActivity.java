package com.example.subdua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.subdua.adapter.PageAdapter;
import com.example.subdua.api.APISettings;
import com.example.subdua.api.JSONPlaceHolderAPI;
import com.example.subdua.database.DatabaseContract;
import com.example.subdua.database.DatabaseHelper;
import com.example.subdua.database.UserHelper;
import com.example.subdua.model.User;
import com.example.subdua.model.UserDetail;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import static com.example.subdua.database.DatabaseContract.DbContractColumn.nama_table;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailActivity extends AppCompatActivity {
    private UserHelper userHelper;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        PageAdapter adapter = new PageAdapter(this,getSupportFragmentManager());
        TabLayout tabLayout = findViewById(R.id.tl_detail);
        ViewPager viewPager = findViewById(R.id.vp_detail);

        final ProgressDialog progress = new ProgressDialog(UserDetailActivity.this);
        progress.setMessage(getString(R.string.loading));
        progress.show();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setElevation(0);

        final User up = getIntent().getParcelableExtra("LEO");


        final TextView textView = findViewById(R.id.tv_username_detail);
        final TextView textView1 = findViewById(R.id.tv_location_detail);
        final TextView textView2 = findViewById(R.id.tv_email_detail);
        final TextView textView3 = findViewById(R.id.tv_followers_detail);
        final TextView textView4 = findViewById(R.id.tv_following_detail);
        final ImageView imageView = findViewById(R.id.image_user_detail);
        final FloatingActionButton floatingActionButton = findViewById(R.id.icon_favorite);

        JSONPlaceHolderAPI jsonPlaceHolderAPI = APISettings.getRetrofit().create(JSONPlaceHolderAPI.class);
        Call<UserDetail> call = jsonPlaceHolderAPI.userDetail(up.getLogin());
        call.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {

                textView.setText(response.body().getName());
                textView1.setText(getString(R.string.location) + response.body().getLocation());
                textView2.setText(getString(R.string.email) + response.body().getEmail());
                textView3.setText(getString(R.string.detailfollowers) + String.valueOf(response.body().getFollowers()));
                textView4.setText(getString(R.string.detailfollowing) + String.valueOf(response.body().getFollowing()));
                Glide.with(getApplicationContext())
                        .load(response.body().getAvatarUrl())
                        .into(imageView);
                textView.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
                textView4.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                progress.dismiss();

            }

            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {
                Toast.makeText(UserDetailActivity.this, "No Connection!!!", Toast.LENGTH_SHORT).show();
            }
        });

        if (check(up.getLogin()) == true) {
            floatingActionButton.setVisibility(View.GONE);
        } else {
            floatingActionButton.setOnClickListener(view -> {
                ArrayList<User> userGithubList = new ArrayList<>();
                userGithubList = userHelper.getDataUser();
                userHelper.userInsert(up);
                Toast.makeText(UserDetailActivity.this, "Success added favorite", Toast.LENGTH_SHORT).show();
                floatingActionButton.setVisibility(View.GONE);
            });
        }

    }

    public void goBack (View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean check(String username){
        String change = DatabaseContract.DbContractColumn.nama + "=?";
        String[] changeArg = {username};
        String limit = "1";
        userHelper = new UserHelper(this);
        userHelper.open();

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = database.query(nama_table,null,change,changeArg,null,null,null,limit);
        boolean exist = (cursor.getCount() > 0 );
        cursor.close();
        return exist;
    }
}
