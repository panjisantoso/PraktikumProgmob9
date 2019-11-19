package com.example.progmobkelompok9.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id_user")
    private String idUser;
    @SerializedName("nama_user")
    private String namaUser;
    @SerializedName("image")
    private String image;
    @SerializedName("tgl_lahir")
    private String tglLahir;
    @SerializedName("alamat")
    private String alamat;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("tipe_user")
    private String tipeUser;
    @SerializedName("status_aktif")
    private String statusAktif;
    @SerializedName("fcm_token")
    private String fcmToken;

    public User() {
    }

    public User(String idUser, String namaUser, String image, String tglLahir, String alamat, String username, String password, String tipeUser, String statusAktif, String fcmToken) {
        this.idUser = idUser;
        this.namaUser = namaUser;
        this.image = image;
        this.tglLahir = tglLahir;
        this.alamat = alamat;
        this.username = username;
        this.password = password;
        this.tipeUser = tipeUser;
        this.statusAktif = statusAktif;
        this.fcmToken = fcmToken;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipeUser() {
        return tipeUser;
    }

    public void setTipeUser(String tipeUser) {
        this.tipeUser = tipeUser;
    }

    public String getStatusAktif() {
        return statusAktif;
    }

    public void setStatusAktif(String statusAktif) {
        this.statusAktif = statusAktif;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
