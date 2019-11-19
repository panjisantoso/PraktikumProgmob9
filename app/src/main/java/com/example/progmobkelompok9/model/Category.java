package com.example.progmobkelompok9.model;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("id_category")
    private String idCategory;
    @SerializedName("image")
    private String image;
    @SerializedName("category_name")
    private String categoryName;
    @SerializedName("status_aktif")
    private String statusAktif;

    public Category(String idCategory, String image, String categoryName, String statusAktif) {
        this.idCategory = idCategory;
        this.image = image;
        this.categoryName = categoryName;
        this.statusAktif = statusAktif;
    }

    public Category() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStatusAktif() {
        return statusAktif;
    }

    public void setStatusAktif(String statusAktif) {
        this.statusAktif = statusAktif;
    }
}
