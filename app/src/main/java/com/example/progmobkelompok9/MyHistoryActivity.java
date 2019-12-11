package com.example.progmobkelompok9;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.progmobkelompok9.adapter.HistoryAdapter;
import com.example.progmobkelompok9.adapter.MyDocumentAdapter;
import com.example.progmobkelompok9.api.ApiClient;
import com.example.progmobkelompok9.api.ApiService;
import com.example.progmobkelompok9.model.Document;
import com.example.progmobkelompok9.util.StringFixed;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyHistoryActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    TextView emptyTxt;
    HistoryAdapter adapter;
    List<Document> documentList = new ArrayList<Document>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("My History");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        sharedPreferences = getSharedPreferences(StringFixed.KEY_LOGIN, Context.MODE_PRIVATE);
        Boolean session = sharedPreferences.getBoolean(StringFixed.KEY_SESSION,false);
        recyclerView = findViewById(R.id.history_rv);
        emptyTxt = findViewById(R.id.history_empty);
        progressBar = findViewById(R.id.history_progressBar);

        if (session){
            getDocument();
        }
        else{
            recyclerView.setVisibility(View.GONE);
            emptyTxt.setVisibility(View.VISIBLE);
            emptyTxt.setText("You need to Login");
        }

    }

    private void setRecycler(){


        Log.e("sdf",documentList.toString());
        if (documentList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyTxt.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyTxt.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            adapter = new HistoryAdapter(this, documentList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            recyclerView.setAdapter(adapter);
        }
    }

    private void getDocument(){

        ApiClient.getClient()
                .create(ApiService.class)
                .getHistory(sharedPreferences.getString(StringFixed.KEY_ID_USER,""))
                .enqueue(new Callback<List<Document>>() {
                    @Override
                    public void onResponse(Call<List<Document>> call, Response<List<Document>> response) {
//                        Log.e("error bro",response.body().get(0).getDeskripsi().toString());
                        documentList.clear();
                        try {
                            documentList.addAll(response.body());
                            setRecycler();
                        }
                        catch (Exception e){
                            Log.e("error",e.getMessage());
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Document>> call, Throwable t) {
                        Log.e("error bro",t.toString());
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
