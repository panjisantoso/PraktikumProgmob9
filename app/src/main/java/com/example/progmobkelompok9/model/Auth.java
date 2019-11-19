package com.example.progmobkelompok9.model;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Field;

public class Auth {

    @SerializedName("success")
    private Boolean success = null;

    @SerializedName("message")
    private String message = null;
    @SerializedName("user")
    private User user = null;
    @SerializedName("token")
    private String token = null;

    public Auth() {
    }

    public Auth(Boolean success, String message, User user, String token) {
        this.success = success;
        this.message = message;
        this.user = user;
        this.token = token;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
