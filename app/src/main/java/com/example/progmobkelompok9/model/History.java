package com.example.progmobkelompok9.model;

import com.google.gson.annotations.SerializedName;

public class History {
    @SerializedName("id_history")
    private String idHistory;
    @SerializedName("id_user")
    private String idUser;
    @SerializedName("id_document")
    private String idDocument;
    @SerializedName("tanggal")
    private String tanggal;

    public History(String idHistory, String idUser, String idDocument, String tanggal) {
        this.idHistory = idHistory;
        this.idUser = idUser;
        this.idDocument = idDocument;
        this.tanggal = tanggal;
    }

    public History() {
    }

    public String getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(String idHistory) {
        this.idHistory = idHistory;
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

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
