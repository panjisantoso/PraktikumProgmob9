package com.example.progmobkelompok9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.progmobkelompok9.adapter.HomeAdapter;
import com.example.progmobkelompok9.api.ApiClient;
import com.example.progmobkelompok9.api.ApiService;
import com.example.progmobkelompok9.model.Document;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDetailActivity extends AppCompatActivity {

    NestedScrollView mScroller;
    Toolbar mToolbar;

    AppBarLayout mAppBar;
    float opacity = 0;

    RecyclerView documentRv;
    TextView txtTitle, txtEmpty;
    ImageView img;
    ProgressBar progressBar;
    String idCategory="";
    HomeAdapter adapter;
    List<Document> documentList = new ArrayList<Document>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        Intent i = getIntent();

        String category = i.getStringExtra("category");

        txtTitle = findViewById(R.id.category_detail_title);
        txtEmpty = findViewById(R.id.category_detail_empty);
        documentRv = findViewById(R.id.category_detail_recyclerview);
        progressBar = findViewById(R.id.category_detail_progressBar);
        txtTitle.setText(category);

        if (category.equals("Jurnal")){
           idCategory="2";
        }
        else if (category.equals("Komik")){
            idCategory="3";
        }
        else if (category.equals("Novel")){
            idCategory="1";
        }
        else if (category.equals("Biografi")){
            idCategory="4";
        }
        txtEmpty.setVisibility(View.GONE);
        setHeader(category);
        getDocument();
    }

    private void setRecycler(){

        if (documentList.isEmpty()){
            documentRv.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        else{
            documentRv.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            adapter = new HomeAdapter(this, documentList);
            documentRv.setLayoutManager(new LinearLayoutManager(this));

            documentRv.setAdapter(adapter);
        }
    }

    private void getDocument(){
        ApiClient.getClient()
                .create(ApiService.class)
                .getCategoryDocument(idCategory)
                .enqueue(new Callback<List<Document>>() {
                    @Override
                    public void onResponse(Call<List<Document>> call, Response<List<Document>> response) {
//                        Log.e("error bro",response.body().get(0).getDeskripsi().toString());
                        documentList.clear();
                        try {
                            documentList.addAll(response.body());
                            setRecycler();
                            progressBar.setVisibility(View.GONE);
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

    private void setHeader(String category){
        mScroller = (NestedScrollView) findViewById(R.id.category_detail_nestedScrollView);
        mToolbar = (Toolbar) findViewById(R.id.category_detail_toolbar);
        mAppBar = (AppBarLayout) findViewById(R.id.category_detail_appBarLayout);

        mToolbar.setTitle(category);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(Color.argb(255,255,255,255), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }

        if (mScroller != null) {
            mScroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

//              scroll down
                    if (scrollY > oldScrollY) {
                        if (scrollY > 100 && scrollY <= 400){
                            settingToolbar(scrollY);
                        }
                    }

//              scroll up
                    if (scrollY < oldScrollY) {
                        if (scrollY > 100 && scrollY <= 400){
                            settingToolbar(scrollY);
                        }
                    }

                    if (scrollY >= 400){
                        mAppBar.setElevation(6);
                        int color = 135;

                        if (getSupportActionBar() != null){
                            Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
                            upArrow.setColorFilter(Color.argb(255,color,color,color), PorterDuff.Mode.SRC_ATOP);
                            getSupportActionBar().setHomeAsUpIndicator(upArrow);
                        }

                        mToolbar.setTitleTextColor(Color.argb(255,color,color,color));
                        mToolbar.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    }

//                check for top
                    if (scrollY == 0) {
                        mAppBar.setElevation(0);
                        mToolbar.setTitleTextColor(Color.argb(255,255,255,255));
                        mToolbar.setBackgroundColor(Color.argb(0, 255, 255, 255));
                        mAppBar.bringToFront();
                    }

//                check for bottom
//                if (scrollY == ( v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() )) {
//                    Log.i(TAG, "BOTTOM SCROLL");
//                }
                }
            });
        }
    }

    private void settingToolbar(int scrollY){
        opacity = ((float)scrollY - 100) / 400;
        mAppBar.setElevation(opacity * 6);
        int color = 255 - (int)(120 * (float)opacity);

        if (getSupportActionBar() != null){
            Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(Color.argb(255,255,255,255), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }

        mToolbar.setTitleTextColor(Color.argb(255,255,255,255));
        mToolbar.setBackgroundColor(Color.argb((int)(opacity * 255), 255, 255, 255));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }
}
