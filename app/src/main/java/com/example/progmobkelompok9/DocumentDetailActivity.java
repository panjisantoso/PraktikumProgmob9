package com.example.progmobkelompok9;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.progmobkelompok9.adapter.CommentAdapter;
import com.example.progmobkelompok9.api.ApiClient;
import com.example.progmobkelompok9.api.ApiService;
import com.example.progmobkelompok9.model.Comment;
import com.example.progmobkelompok9.model.Like;
import com.example.progmobkelompok9.model.ResponseMessage;
import com.example.progmobkelompok9.util.StringFixed;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentDetailActivity extends AppCompatActivity {

    TextView mNama,mPenulis,mPenerbit,mThnTerbit,mCategory,mNamaUser,mDeskripsi,mLikeTxt,mDislikeTxt,mOfflineTxt,mReadTxt;
    EditText mComment;
    CircleImageView mImageUser;
    ImageView mImage,mLike,mDislike,mOffline,mRead;
    RecyclerView rvComment;
    Button mCommentButton, mDeskripsiButton, mSendButton;

    String idDocument, idUser, nama, penulis, penerbit, thnTerbit, category, namaUser, deskripsi, imageUser, image, comment;

    SharedPreferences sharedPreferences;
    String statusLike;

    List<Comment> commentList = new ArrayList<>();
    CommentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_detail);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Document Detail");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        sharedPreferences = getSharedPreferences(StringFixed.KEY_LOGIN,MODE_PRIVATE);
        idUser = sharedPreferences.getString(StringFixed.KEY_ID_USER,"");

        inisialisasiComponent();
        settingData();

        mDeskripsiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeskripsi.setVisibility(View.VISIBLE);
                rvComment.setVisibility(View.GONE);
                mSendButton.setVisibility(View.GONE);
                mComment.setVisibility(View.GONE);
            }
        });

        mCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeskripsi.setVisibility(View.GONE);
                rvComment.setVisibility(View.VISIBLE);
                mSendButton.setVisibility(View.VISIBLE);
                mComment.setVisibility(View.VISIBLE);
            }
        });

        mLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeLike("1");
            }
        });

        mDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeLike("2");
            }
        });

        mLikeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeLike("1");
            }
        });

        mDislikeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeLike("2");
            }
        });

        checkStatusLike();

        getComment();

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
    }

    private void checkStatusLike(){
        Boolean session = sharedPreferences.getBoolean(StringFixed.KEY_SESSION,false);

        if (session){
            ApiClient.getClient()
                    .create(ApiService.class)
                    .getLikeDocumentUser(sharedPreferences.getString(StringFixed.KEY_ID_USER,""),idDocument)
                    .enqueue(new Callback<List<Like>>() {
                        @Override
                        public void onResponse(Call<List<Like>> call, Response<List<Like>> response) {
                            if (response.body() != null && !response.body().isEmpty()) {
                                for (int i=0;i<response.body().size();i++){
                                    if (response.body().get(i).getIdDocument().equals(idDocument)){
                                        if (response.body().get(i).getStatusLike().equals("1")){
                                            statusLike = "1";
                                            Glide.with(DocumentDetailActivity.this)
                                                    .load(R.drawable.ic_like_aktif)
                                                    .centerCrop()
                                                    .placeholder(R.drawable.ic_like_nonaktif)
                                                    .into(mLike);
                                            mLikeTxt.setTextColor(Color.rgb(30,136,229));
                                            mDislikeTxt.setTextColor(Color.rgb(181,181,181));
                                            Glide.with(DocumentDetailActivity.this)
                                                    .load(R.drawable.ic_dislike_nonaktif)
                                                    .centerCrop()
                                                    .placeholder(R.drawable.ic_dislike_nonaktif)
                                                    .into(mDislike);
                                        }
                                        else if (response.body().get(i).getStatusLike().equals("2")){
                                            statusLike = "2";
                                            Glide.with(DocumentDetailActivity.this)
                                                    .load(R.drawable.ic_dislike_aktif)
                                                    .centerCrop()
                                                    .placeholder(R.drawable.ic_dislike_nonaktif)
                                                    .into(mDislike);
                                            mDislikeTxt.setTextColor(Color.rgb(30,136,229));
                                            mLikeTxt.setTextColor(Color.rgb(181,181,181));
                                            Glide.with(DocumentDetailActivity.this)
                                                    .load(R.drawable.ic_like_nonaktif)
                                                    .centerCrop()
                                                    .placeholder(R.drawable.ic_like_nonaktif)
                                                    .into(mLike);
                                        }
                                    }
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<List<Like>> call, Throwable t) {
                            Log.e("error",t.getMessage());
                        }
                    });
        }
    }

    private void storeLike(final String statusLike1){
        ApiClient.getClient()
                .create(ApiService.class)
                .storeLike(idUser,idDocument,statusLike1)
                .enqueue(new Callback<ResponseMessage>() {
                    @Override
                    public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                        Log.e("test", response.message());
                        if (statusLike1.equals("1") ){
                            statusLike = "1";
                            Glide.with(DocumentDetailActivity.this)
                                    .load(R.drawable.ic_like_aktif)
                                    .centerCrop()
                                    .placeholder(R.drawable.ic_like_nonaktif)
                                    .into(mLike);
                            mLikeTxt.setTextColor(Color.rgb(30,136,229));
                            mDislikeTxt.setTextColor(Color.rgb(181,181,181));
                            Glide.with(DocumentDetailActivity.this)
                                    .load(R.drawable.ic_dislike_nonaktif)
                                    .centerCrop()
                                    .placeholder(R.drawable.ic_dislike_nonaktif)
                                    .into(mDislike);
                        }
                        else if (statusLike1.equals("2")){
                            statusLike = "2";
                            Glide.with(DocumentDetailActivity.this)
                                    .load(R.drawable.ic_dislike_aktif)
                                    .centerCrop()
                                    .placeholder(R.drawable.ic_dislike_nonaktif)
                                    .into(mDislike);
                            mDislikeTxt.setTextColor(Color.rgb(30,136,229));
                            mLikeTxt.setTextColor(Color.rgb(181,181,181));
                            Glide.with(DocumentDetailActivity.this)
                                    .load(R.drawable.ic_like_nonaktif)
                                    .centerCrop()
                                    .placeholder(R.drawable.ic_like_nonaktif)
                                    .into(mLike);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage> call, Throwable t) {
                        Log.e("error",t.getMessage());
                    }
                });
    }

    private void setRecyclerComment(){
        rvComment = findViewById(R.id.detail_document_recyclerViewComment);

        if (commentList.isEmpty()){
            rvComment.setVisibility(View.GONE);
        }
        else{
            rvComment.setVisibility(View.VISIBLE);
            adapter = new CommentAdapter(this, commentList);
            rvComment.setLayoutManager(new LinearLayoutManager(this));

            rvComment.setAdapter(adapter);
        }
    }

    private void getComment(){
        ApiClient.getClient()
                .create(ApiService.class)
                .getCommentDocument(idDocument)
                .enqueue(new Callback<List<Comment>>() {
                    @Override
                    public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                        try {
                            commentList.addAll(response.body());
                            setRecyclerComment();
                        }
                        catch (Exception e){
                            Log.e("error",e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Comment>> call, Throwable t) {

                    }
                });
    }

    private void sendComment(){
        comment = mComment.getText().toString();
        ApiClient.getClient()
                .create(ApiService.class)
                .storeComment(idUser,idDocument,comment)
                .enqueue(new Callback<ResponseMessage>() {
                    @Override
                    public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                        Intent intent = getIntent();
                        intent.putExtra("comment","comment");
                        overridePendingTransition(0, 0);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ResponseMessage> call, Throwable t) {

                    }
                });
    }

    private void settingData(){
        Intent intent = getIntent();
        idDocument = intent.getStringExtra(StringFixed.KEY_ID_DOCUMENT);
        nama = intent.getStringExtra(StringFixed.KEY_NAMA_DOCUMENT);
        penulis = intent.getStringExtra(StringFixed.KEY_PENULIS);
        penerbit = intent.getStringExtra(StringFixed.KEY_PENERBIT);
        thnTerbit = intent.getStringExtra(StringFixed.KEY_TAHUN_TERBIT);
        category = intent.getStringExtra(StringFixed.KEY_NAMA_CATEGORY);
        namaUser = intent.getStringExtra(StringFixed.KEY_NAMA_USER);
        deskripsi = intent.getStringExtra(StringFixed.KEY_DESKRIPSI);
        imageUser = intent.getStringExtra(StringFixed.KEY_IMAGE_USER);
        image = intent.getStringExtra(StringFixed.KEY_IMAGE);

        mNamaUser.setText(namaUser);
        mNama.setText(": "+nama);
        mPenulis.setText(": "+penulis);
        mPenerbit.setText(": "+penerbit);
        mThnTerbit.setText(": "+thnTerbit);
        mCategory.setText(": "+category);
        mDeskripsi.setText(deskripsi);

        Glide.with(this)
                .load(BuildConfig.BASE_URL_IMAGE+image)
                .centerCrop()
                .placeholder(R.drawable.image_placeholder)
                .into(mImage);

        Glide.with(this)
                .load(BuildConfig.BASE_URL_IMAGE+imageUser)
                .centerCrop()
                .placeholder(R.drawable.avatar)
                .into(mImageUser);

    }

    private void inisialisasiComponent(){
        mNama = findViewById(R.id.detail_document_namaDocument);
        mPenulis = findViewById(R.id.detail_document_penulis);
        mPenerbit = findViewById(R.id.detail_document_penerbit);
        mThnTerbit = findViewById(R.id.detail_document_tahunTerbit);
        mCategory = findViewById(R.id.detail_document_namaCategory);
        mNamaUser = findViewById(R.id.detail_document_namaUser);
        mDeskripsi = findViewById(R.id.detail_document_deskripsi);
        mLikeTxt = findViewById(R.id.detail_document_likeTxt);
        mDislikeTxt = findViewById(R.id.detail_document_dislikeTxt);
        mOfflineTxt = findViewById(R.id.detail_document_offlineTxt);
        mReadTxt = findViewById(R.id.detail_document_readTxt);

        mComment = findViewById(R.id.detail_document_editComment);

        mImageUser = findViewById(R.id.detail_document_userImage);

        mImage = findViewById(R.id.detail_document_image);
        mLike = findViewById(R.id.document_detail_like);
        mDislike = findViewById(R.id.document_detail_dislike);
        mOffline = findViewById(R.id.document_detail_offline);
        mRead = findViewById(R.id.document_detail_read);

        rvComment = findViewById(R.id.detail_document_recyclerViewComment);

        mCommentButton = findViewById(R.id.detail_document_commentButton);
        mDeskripsiButton = findViewById(R.id.detail_document_deskripsiButton);
        mSendButton = findViewById(R.id.detail_document_sendButton);

        mDeskripsi.setVisibility(View.GONE);
        rvComment.setVisibility(View.VISIBLE);
        mComment.setVisibility(View.VISIBLE);
        mSendButton.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
