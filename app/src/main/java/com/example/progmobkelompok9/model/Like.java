package com.example.progmobkelompok9.model;

import com.google.gson.annotations.SerializedName;

public class Like {
    @SerializedName("id_like")
    private String idLike;
    @SerializedName("id_user")
    private String idUser;
    @SerializedName("id_document")
    private String idDocument;
    @SerializedName("status_like")
    private String statusLike;

    public Like() {
    }

    public Like(String idLike, String idUser, String idDocument, String statusLike) {
        this.idLike = idLike;
        this.idUser = idUser;
        this.idDocument = idDocument;
        this.statusLike = statusLike;
    }

    public String getIdLike() {
        return idLike;
    }

    public void setIdLike(String idLike) {
        this.idLike = idLike;
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

    public String getStatusLike() {
        return statusLike;
    }

    public void setStatusLike(String statusLike) {
        this.statusLike = statusLike;
    }
}
