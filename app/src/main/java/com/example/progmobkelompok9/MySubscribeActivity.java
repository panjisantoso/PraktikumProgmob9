package com.example.progmobkelompok9;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.progmobkelompok9.adapter.SubscriberAdapter;
import com.example.progmobkelompok9.api.ApiClient;
import com.example.progmobkelompok9.api.ApiService;
import com.example.progmobkelompok9.model.User;
import com.example.progmobkelompok9.util.StringFixed;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MySubscribeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    RecyclerView recyclerView;
    ProgressBar progressBar;
    String idUser;
    SubscriberAdapter adapter;
    List<User> userList = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscribe);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("My Subscribe");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        sharedPreferences = getSharedPreferences(StringFixed.KEY_LOGIN,MODE_PRIVATE);
        idUser = sharedPreferences.getString(StringFixed.KEY_ID_USER,"");

        recyclerView = findViewById(R.id.my_subscribe_recyclerView);
        progressBar = findViewById(R.id.my_subscribe_progressBar);

        getSubscribe();
    }

    private void setting(){
        if (userList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new SubscriberAdapter(this, userList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            recyclerView.setAdapter(adapter);
        }
    }

    private void getSubscribe(){
        ApiClient.getClient()
                .create(ApiService.class)
                .getSubscribe(idUser)
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        try {
                            userList.addAll(response.body());
                            setting();
                            progressBar.setVisibility(View.GONE);
                        }catch (Exception e){
                            Log.e("error",e.getMessage());
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Log.e("error",t.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
