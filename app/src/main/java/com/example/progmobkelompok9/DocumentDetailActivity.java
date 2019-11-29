package com.example.progmobkelompok9;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.progmobkelompok9.adapter.CommentAdapter;
import com.example.progmobkelompok9.api.ApiClient;
import com.example.progmobkelompok9.api.ApiService;
import com.example.progmobkelompok9.model.Auth;
import com.example.progmobkelompok9.model.Comment;
import com.example.progmobkelompok9.model.Like;
import com.example.progmobkelompok9.model.ResponseMessage;
import com.example.progmobkelompok9.offlineSQLite.DBHelper;
import com.example.progmobkelompok9.util.StringFixed;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentDetailActivity extends AppCompatActivity {

    TextView mNama,mPenulis,mPenerbit,mThnTerbit,mCategory,mNamaUser,mDeskripsi,mLikeTxt,mDislikeTxt,mOfflineTxt,mReadTxt,mAuthor;
    EditText mComment;
    CircleImageView mImageUser;
    ImageView mImage,mLike,mDislike,mOffline,mRead;
    RecyclerView rvComment;
    Button mCommentButton, mDeskripsiButton, mSendButton;

    String idDocument, idUserDocument, idUser, nama, penulis, penerbit, thnTerbit, category, namaUser, deskripsi, imageUser, image, comment, path, fileType, totalView, totalDownload, tglUpload, statusAktif;

    SharedPreferences sharedPreferences;
    String statusLike;
    Boolean session;
    List<Comment> commentList = new ArrayList<>();
    CommentAdapter adapter;

    //SQLite
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_detail);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Document Detail");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        dbHelper = new DBHelper(this);
        sharedPreferences = getSharedPreferences(StringFixed.KEY_LOGIN,MODE_PRIVATE);
        idUser = sharedPreferences.getString(StringFixed.KEY_ID_USER,"");
        session = sharedPreferences.getBoolean(StringFixed.KEY_SESSION,false);
        inisialisasiComponent();
        settingData();

        mAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscriber();
            }
        });

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

                if (session){
                    mDeskripsi.setVisibility(View.GONE);
                    rvComment.setVisibility(View.VISIBLE);
                    mComment.setVisibility(View.VISIBLE);
                    mSendButton.setVisibility(View.VISIBLE);
                }
                else{
                    mDeskripsi.setVisibility(View.GONE);
                    rvComment.setVisibility(View.VISIBLE);
                    mComment.setVisibility(View.GONE);
                    mSendButton.setVisibility(View.GONE);
                }
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

        mOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mPath = BuildConfig.BASE_URL_DOCUMENT + path;
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("INSERT INTO document(judul, category, penulis, penerbit, tahun_terbit, nama_user, deskripsi, " +
                        "path, tgl_upload, status_aktif, " +
                        "id_user" +
                        ") VALUES('" +
                        mNama.getText().toString() + "','" +
                        mCategory.getText().toString() + "','" +
                        mPenulis.getText().toString() + "','" +
                        mPenerbit.getText().toString() + "','" +
                        mThnTerbit.getText().toString() + "','" +
                        mNamaUser.getText().toString() +  "','" +
                        mDeskripsi.getText().toString() + "','" +
                        path + "','" +
                        tglUpload + "','" +
                        statusAktif + "','" +
                        idUser +
                        "')");

                Toast.makeText(getApplicationContext(), "Download Document Success", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        mRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = BuildConfig.BASE_URL_DOCUMENT + path;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();

            }
        });

        mImageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscriber();
            }
        });

        mNamaUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscriber();
            }
        });
    }

    private void subscriber(){
        if (session){
            if (!idUser.equals(idUserDocument)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure?").setMessage("Subscribe this person").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiClient.getClient()
                                .create(ApiService.class)
                                .storeSubscribe(idUser,idUserDocument)
                                .enqueue(new Callback<ResponseMessage>() {
                                    @Override
                                    public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                        try {
                                            Toast.makeText(DocumentDetailActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                        catch (Exception e){
                                            Log.e("error",e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseMessage> call, Throwable t) {

                                    }
                                });
                    }
                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        }
        else{
            Toast.makeText(this,"you need to login to subscribe this person",Toast.LENGTH_SHORT).show();
        }

    }

    private void checkStatusLike(){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (idUser.equals(idUserDocument)){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.top_bar_edit, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_edit) {
            Intent intent = new Intent(DocumentDetailActivity.this,UpdateDocumentActivity.class);

            //tambahan
            intent.putExtra(StringFixed.KEY_ID_USER,idUserDocument);
            intent.putExtra(StringFixed.KEY_NAMA_USER,namaUser);
            intent.putExtra(StringFixed.KEY_TOTAL_VIEW,totalView);
            intent.putExtra(StringFixed.KEY_TOTAL_DOWNLOAD,totalDownload);
            intent.putExtra(StringFixed.KEY_TGL_UPLOAD,tglUpload);
            intent.putExtra(StringFixed.KEY_STATUS_AKTIF,statusAktif);
            intent.putExtra(StringFixed.KEY_IMAGE_USER,imageUser);

            //penting
            intent.putExtra(StringFixed.KEY_ID_DOCUMENT,idDocument);
            intent.putExtra(StringFixed.KEY_NAMA_DOCUMENT,nama);
            intent.putExtra(StringFixed.KEY_FILE_TYPE,fileType);
            intent.putExtra(StringFixed.KEY_NAMA_CATEGORY,category);
            intent.putExtra(StringFixed.KEY_PATH,path);
            intent.putExtra(StringFixed.KEY_IMAGE,image);
            intent.putExtra(StringFixed.KEY_PENULIS,penulis);
            intent.putExtra(StringFixed.KEY_PENERBIT,penerbit);
            intent.putExtra(StringFixed.KEY_TAHUN_TERBIT,thnTerbit);
            intent.putExtra(StringFixed.KEY_DESKRIPSI,deskripsi);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void storeLike(final String statusLike1){
        if (session){
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
        else{
            Toast.makeText(this,"you need to login to use this feature",Toast.LENGTH_SHORT).show();
        }
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
        path = intent.getStringExtra(StringFixed.KEY_PATH);
        fileType = intent.getStringExtra(StringFixed.KEY_FILE_TYPE);
        idUserDocument = intent.getStringExtra(StringFixed.KEY_ID_USER);
        totalDownload = intent.getStringExtra(StringFixed.KEY_TOTAL_DOWNLOAD);
        totalView = intent.getStringExtra(StringFixed.KEY_TOTAL_VIEW);
        tglUpload = intent.getStringExtra(StringFixed.KEY_TGL_UPLOAD);
        statusAktif = intent.getStringExtra(StringFixed.KEY_STATUS_AKTIF);

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

        mAuthor = findViewById(R.id.document_detail_author);

        rvComment = findViewById(R.id.detail_document_recyclerViewComment);

        mCommentButton = findViewById(R.id.detail_document_commentButton);
        mDeskripsiButton = findViewById(R.id.detail_document_deskripsiButton);
        mSendButton = findViewById(R.id.detail_document_sendButton);

        if (session){
            mDeskripsi.setVisibility(View.GONE);
            rvComment.setVisibility(View.VISIBLE);
            mComment.setVisibility(View.VISIBLE);
            mSendButton.setVisibility(View.VISIBLE);
        }
        else{
            mDeskripsi.setVisibility(View.GONE);
            rvComment.setVisibility(View.VISIBLE);
            mComment.setVisibility(View.GONE);
            mSendButton.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
