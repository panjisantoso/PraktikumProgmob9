package com.example.progmobkelompok9.offlineSQLite;

import com.google.gson.annotations.SerializedName;

public class DocumentOffline {
    @SerializedName("id_document")
    private String idDocument;
    @SerializedName("judul")
    private String judul;
    @SerializedName("category")
    private String category;
    @SerializedName("penulis")
    private String penulis;
    @SerializedName("penerbit")
    private String penerbit;
    @SerializedName("tahun_terbit")
    private String tahunTerbit;
    @SerializedName("foto")
    private String foto;
    @SerializedName("nama_user")
    private String namaUser;
    @SerializedName("id_user")
    private String idUser;

    public DocumentOffline(){

    }

    public DocumentOffline(String idDocument, String judul,String category, String penulis, String penerbit,String tahunTerbit, String foto, String namaUser, String idUser){
        this.idDocument = idDocument;
        this.judul = judul;
        this.category = category;
        this.penulis = penulis;
        this.penerbit = penerbit;
        this.tahunTerbit = tahunTerbit;
        this.foto = foto;
        this.namaUser = namaUser;
        this.idUser = idUser;
    }
    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }
    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getTahunTerbit(){
        return tahunTerbit;
    }
    public void setTahunTerbit(String tahunTerbit){
        this.tahunTerbit = tahunTerbit;
    }
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}
