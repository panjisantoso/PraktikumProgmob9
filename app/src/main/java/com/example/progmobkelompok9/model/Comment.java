package com.example.progmobkelompok9.model;

import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("id_comment")
    private String idComment;
    @SerializedName("id_user")
    private String idUser;
    @SerializedName("id_document")
    private String idDocument;
    @SerializedName("comment")
    private String comment;
    @SerializedName("nama_user")
    private String namaUser;
    @SerializedName("image_user")
    private String imageUser;

    public Comment() {
    }

    public Comment(String idComment, String idUser, String idDocument, String comment, String namaUser, String imageUser) {
        this.idComment = idComment;
        this.idUser = idUser;
        this.idDocument = idDocument;
        this.comment = comment;
        this.namaUser = namaUser;
        this.imageUser = imageUser;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getImageUser() {
        return imageUser;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
