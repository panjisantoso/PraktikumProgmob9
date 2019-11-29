package com.example.progmobkelompok9.offlineSQLite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.progmobkelompok9.BuildConfig;
import com.example.progmobkelompok9.DocumentDetailActivity;
import com.example.progmobkelompok9.R;
import com.example.progmobkelompok9.api.ApiClient;
import com.example.progmobkelompok9.api.ApiService;
import com.example.progmobkelompok9.model.Document;
import com.example.progmobkelompok9.model.Like;
import com.example.progmobkelompok9.model.ResponseMessage;
import com.example.progmobkelompok9.util.StringFixed;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentHolder extends RecyclerView.Adapter<DocumentHolder.MyViewHolder>{

    private List<Document> documentList;
    private Context context;
    private LayoutInflater inflater;
    Document document;
    String statusLike;

    public DocumentHolder(Context context, List<Document> documentsList) {
        this.context = context;
        this.documentList = documentsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.item_document, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        document = documentList.get(position);
        Glide.with(context)
                .load(BuildConfig.BASE_URL_IMAGE+document.getImage())
                .centerCrop()
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imageDocument);

        holder.namaDocument.setText(document.getNamaDocument());
        holder.deskripsi.setText(document.getDeskripsi());

        checkStatusLike(document.getIdDocument(),holder);

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeLike(documentList.get(position).getIdDocument(),"1",holder);
            }
        });

        holder.likeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeLike(documentList.get(position).getIdDocument(),"1",holder);
            }
        });

        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeLike(documentList.get(position).getIdDocument(),"2",holder);
            }
        });

        holder.dislikeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeLike(documentList.get(position).getIdDocument(),"2",holder);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DocumentDetailActivity.class);
                intent.putExtra(StringFixed.KEY_ID_DOCUMENT,documentList.get(position).getIdDocument());
                intent.putExtra(StringFixed.KEY_NAMA_USER,documentList.get(position).getNamaUser());
                intent.putExtra(StringFixed.KEY_NAMA_CATEGORY,documentList.get(position).getNamaCategory());
                intent.putExtra(StringFixed.KEY_NAMA_DOCUMENT,documentList.get(position).getNamaDocument());
                intent.putExtra(StringFixed.KEY_FILE_TYPE,documentList.get(position).getFileType());
                intent.putExtra(StringFixed.KEY_PENULIS,documentList.get(position).getPenulis());
                intent.putExtra(StringFixed.KEY_PENERBIT,documentList.get(position).getPenerbit());
                intent.putExtra(StringFixed.KEY_TAHUN_TERBIT,documentList.get(position).getTahunTerbit());
                intent.putExtra(StringFixed.KEY_DESKRIPSI,documentList.get(position).getDeskripsi());
                intent.putExtra(StringFixed.KEY_PATH,documentList.get(position).getPath());
                intent.putExtra(StringFixed.KEY_TOTAL_VIEW,documentList.get(position).getTotalView());
                intent.putExtra(StringFixed.KEY_TOTAL_DOWNLOAD,documentList.get(position).getTotalDownload());
                intent.putExtra(StringFixed.KEY_TGL_UPLOAD,documentList.get(position).getTglUpload());
                intent.putExtra(StringFixed.KEY_STATUS_AKTIF,documentList.get(position).getStatusAktif());
                intent.putExtra(StringFixed.KEY_IMAGE_USER,documentList.get(position).getImageUser());
                intent.putExtra(StringFixed.KEY_IMAGE,documentList.get(position).getImage());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return getDocumentList().size();
    }

    public List<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
    }

    private void storeLike(String idDocument, final String statusLike1, final MyViewHolder holder){
        SharedPreferences sharedPreferences = context.getSharedPreferences(StringFixed.KEY_LOGIN,Context.MODE_PRIVATE);
        Boolean session = sharedPreferences.getBoolean(StringFixed.KEY_SESSION,false);
        if (session){
            String idUser = sharedPreferences.getString(StringFixed.KEY_ID_USER,"");

            ApiClient.getClient()
                    .create(ApiService.class)
                    .storeLike(idUser,idDocument,statusLike1)
                    .enqueue(new Callback<ResponseMessage>() {
                        @Override
                        public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                            Log.e("test", response.message());
                            if (statusLike1.equals("1") ){
                                statusLike = "1";
                                Glide.with(context)
                                        .load(R.drawable.ic_like_aktif)
                                        .centerCrop()
                                        .placeholder(R.drawable.ic_like_nonaktif)
                                        .into(holder.like);
                                holder.likeTxt.setTextColor(Color.rgb(30,136,229));
                                holder.dislikeTxt.setTextColor(Color.rgb(181,181,181));
                                Glide.with(context)
                                        .load(R.drawable.ic_dislike_nonaktif)
                                        .centerCrop()
                                        .placeholder(R.drawable.ic_dislike_nonaktif)
                                        .into(holder.dislike);
                            }
                            else if (statusLike1.equals("2")){
                                statusLike = "2";
                                Glide.with(context)
                                        .load(R.drawable.ic_dislike_aktif)
                                        .centerCrop()
                                        .placeholder(R.drawable.ic_dislike_nonaktif)
                                        .into(holder.dislike);
                                holder.dislikeTxt.setTextColor(Color.rgb(30,136,229));
                                holder.likeTxt.setTextColor(Color.rgb(181,181,181));
                                Glide.with(context)
                                        .load(R.drawable.ic_like_nonaktif)
                                        .centerCrop()
                                        .placeholder(R.drawable.ic_like_nonaktif)
                                        .into(holder.like);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseMessage> call, Throwable t) {
                            Log.e("error",t.getMessage());
                        }
                    });
        }
        else{
            Toast.makeText(context,"You need to login to use this feature",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkStatusLike(final String idDocument, final MyViewHolder holder){
        SharedPreferences sharedPreferences = context.getSharedPreferences(StringFixed.KEY_LOGIN,Context.MODE_PRIVATE);
        Boolean session = sharedPreferences.getBoolean(StringFixed.KEY_SESSION,false);

        if (session){
            ApiClient.getClient()
                    .create(ApiService.class)
                    .getLikeDocumentUser(sharedPreferences.getString(StringFixed.KEY_ID_USER,""),idDocument)
                    .enqueue(new Callback<List<Like>>() {
                        @Override
                        public void onResponse(Call<List<Like>> call, Response<List<Like>> response) {
                            if (!response.body().isEmpty()) {
                                for (int i=0;i<response.body().size();i++){
                                    if (response.body().get(i).getIdDocument().equals(idDocument)){
                                        if (response.body().get(i).getStatusLike().equals("1")){
                                            statusLike = "1";
                                            Glide.with(context)
                                                    .load(R.drawable.ic_like_aktif)
                                                    .centerCrop()
                                                    .placeholder(R.drawable.ic_like_nonaktif)
                                                    .into(holder.like);
                                            holder.likeTxt.setTextColor(Color.rgb(30,136,229));
                                            holder.dislikeTxt.setTextColor(Color.rgb(181,181,181));
                                            Glide.with(context)
                                                    .load(R.drawable.ic_dislike_nonaktif)
                                                    .centerCrop()
                                                    .placeholder(R.drawable.ic_dislike_nonaktif)
                                                    .into(holder.dislike);
                                        }
                                        else if (response.body().get(i).getStatusLike().equals("2")){
                                            statusLike = "2";
                                            Glide.with(context)
                                                    .load(R.drawable.ic_dislike_aktif)
                                                    .centerCrop()
                                                    .placeholder(R.drawable.ic_dislike_nonaktif)
                                                    .into(holder.dislike);
                                            holder.dislikeTxt.setTextColor(Color.rgb(30,136,229));
                                            holder.likeTxt.setTextColor(Color.rgb(181,181,181));
                                            Glide.with(context)
                                                    .load(R.drawable.ic_like_nonaktif)
                                                    .centerCrop()
                                                    .placeholder(R.drawable.ic_like_nonaktif)
                                                    .into(holder.like);
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView namaDocument, deskripsi, likeTxt,dislikeTxt,offlineTxt,readTxt;
        private ImageView imageDocument, like, dislike, offline, read;
        private Button btnHapus;
        private CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            namaDocument = itemView.findViewById(R.id.item_document_nama);
            deskripsi = itemView.findViewById(R.id.item_document_deskripsi);
            likeTxt = itemView.findViewById(R.id.item_document_likeTxt);
            dislikeTxt = itemView.findViewById(R.id.item_document_dislikeTxt);
            offlineTxt = itemView.findViewById(R.id.item_document_offlineTxt);
            readTxt = itemView.findViewById(R.id.item_document_readTxt);

            like = itemView.findViewById(R.id.item_document_like);
            dislike = itemView.findViewById(R.id.item_document_dislike);
            offline = itemView.findViewById(R.id.item_document_offline);
            read = itemView.findViewById(R.id.item_document_read);

            imageDocument = itemView.findViewById(R.id.item_document_image);

            cardView = itemView.findViewById(R.id.item_document_cardView);

        }
    }
}
