package com.example.progmobkelompok9.api;

import com.example.progmobkelompok9.model.Auth;
import com.example.progmobkelompok9.model.Category;
import com.example.progmobkelompok9.model.Comment;
import com.example.progmobkelompok9.model.Document;
import com.example.progmobkelompok9.model.Like;
import com.example.progmobkelompok9.model.ResponseMessage;
import com.example.progmobkelompok9.model.User;

import java.util.List;
import java.util.Map;
import java.util.Observable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("login")
    Call<Auth> login(@Field("username") String username,
                      @Field("password") String password);

    @Multipart
    @POST("register")
    Call<Auth> register(
            @Part MultipartBody.Part image,
            @PartMap Map<String,RequestBody> text);

    @Multipart
    @POST("edit-profil")
    Call<Auth> editProfile(
            @Part MultipartBody.Part image,
            @PartMap Map<String,RequestBody> text);

    @GET("document")
    Call<List<Document>> getDocument();

    @GET("mydocument")
    Call<List<Document>> getMyDocument(@Query("id_user") String idUser);

    @Multipart
    @POST("edit-document")
    Call<ResponseMessage> editDocument(
            @Part MultipartBody.Part path,
            @PartMap Map<String,RequestBody> text);

    @Multipart
    @POST("store-document")
    Call<ResponseMessage> storeDocument(
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part path,
            @PartMap Map<String,RequestBody> text);

    @GET("delete-document")
    Call<ResponseMessage> deleteDocument(
            @Query("id_document") String idDocument);

    @GET("category")
    Call<List<Category>> getCategory();

    @GET("like-document")
    Call<List<Like>> getLikeDocumentUser(@Query("id_user") String idUser, @Query("id_document") String idDocument);

    @FormUrlEncoded
    @POST("store-like")
    Call<ResponseMessage> storeLike(@Field("id_user") String idUser,
                         @Field("id_document") String idDocument,
                         @Field("status_like") String statusLike);

    @GET("comment-document")
    Call<List<Comment>> getCommentDocument(@Query("id_document") String idDocument);

    @FormUrlEncoded
    @POST("store-comment")
    Call<ResponseMessage> storeComment(@Field("id_user") String idUser,
                         @Field("id_document") String idDocument,
                         @Field("comment") String comment);
}