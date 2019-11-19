package com.example.progmobkelompok9.model;

import com.google.gson.annotations.SerializedName;

public class ResponseMessage {

    @SerializedName("success")
    private String success;
    @SerializedName("message")
    private String message;
    @SerializedName("id")
    private String id;

    public ResponseMessage() {
    }

    public ResponseMessage(String success, String message, String id) {
        this.success = success;
        this.message = message;
        this.id = id;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
