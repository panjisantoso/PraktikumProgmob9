package com.example.progmobkelompok9.model;

import com.google.gson.annotations.SerializedName;

public class Document {
    @SerializedName("id_document")
    private String idDocument;
    @SerializedName("id_user")
    private String idUser;
    @SerializedName("id_category")
    private String idCategory;
    @SerializedName("file_type")
    private String fileType;
    @SerializedName("nama_document")
    private String namaDocument;
    @SerializedName("penulis")
    private String penulis;
    @SerializedName("penerbit")
    private String penerbit;
    @SerializedName("tahun_terbit")
    private String tahunTerbit;
    @SerializedName("deskripsi")
    private String deskripsi;
    @SerializedName("path")
    private String path;
    @SerializedName("total_view")
    private String totalView;
    @SerializedName("total_download")
    private String totalDownload;
    @SerializedName("tgl_upload")
    private String tglUpload;
    @SerializedName("status_aktif")
    private String statusAktif;
    @SerializedName("status_like")
    private String statusLike;
    @SerializedName("nama_user")
    private String namaUser;
    @SerializedName("nama_category")
    private String namaCategory;
    @SerializedName("image")
    private String image;
    @SerializedName("image_user")
    private String imageUser;

    public Document() {
    }

    public Document(String idDocument, String idUser, String idCategory, String fileType, String namaDocument, String penulis, String penerbit, String tahunTerbit, String deskripsi, String path, String totalView, String totalDownload, String tglUpload, String statusAktif, String statusLike, String namaUser, String namaCategory, String image, String imageUser) {
        this.idDocument = idDocument;
        this.idUser = idUser;
        this.idCategory = idCategory;
        this.fileType = fileType;
        this.namaDocument = namaDocument;
        this.penulis = penulis;
        this.penerbit = penerbit;
        this.tahunTerbit = tahunTerbit;
        this.deskripsi = deskripsi;
        this.path = path;
        this.totalView = totalView;
        this.totalDownload = totalDownload;
        this.tglUpload = tglUpload;
        this.statusAktif = statusAktif;
        this.statusLike = statusLike;
        this.namaUser = namaUser;
        this.namaCategory = namaCategory;
        this.image = image;
        this.imageUser = imageUser;
    }

    public String getStatusLike() {
        return statusLike;
    }

    public void setStatusLike(String statusLike) {
        this.statusLike = statusLike;
    }

    public String getImageUser() {
        return imageUser;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getNamaCategory() {
        return namaCategory;
    }

    public void setNamaCategory(String namaCategory) {
        this.namaCategory = namaCategory;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getNamaDocument() {
        return namaDocument;
    }

    public void setNamaDocument(String namaDocument) {
        this.namaDocument = namaDocument;
    }

    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public void setPenerbit(String penerbit) {
        this.penerbit = penerbit;
    }

    public String getTahunTerbit() {
        return tahunTerbit;
    }

    public void setTahunTerbit(String tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTotalView() {
        return totalView;
    }

    public void setTotalView(String totalView) {
        this.totalView = totalView;
    }

    public String getTotalDownload() {
        return totalDownload;
    }

    public void setTotalDownload(String totalDownload) {
        this.totalDownload = totalDownload;
    }

    public String getTglUpload() {
        return tglUpload;
    }

    public void setTglUpload(String tglUpload) {
        this.tglUpload = tglUpload;
    }

    public String getStatusAktif() {
        return statusAktif;
    }

    public void setStatusAktif(String statusAktif) {
        this.statusAktif = statusAktif;
    }
}
