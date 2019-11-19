package com.example.progmobkelompok9.model;

import com.google.gson.annotations.SerializedName;

public class Subscriber {
    @SerializedName("id_subscriber")
    private String idSubscriber;
    @SerializedName("id_user")
    private String idUser;
    @SerializedName("id_user_subscribe")
    private String idUserSubscribe;

    public Subscriber(String idSubscriber, String idUser, String idUserSubscribe) {
        this.idSubscriber = idSubscriber;
        this.idUser = idUser;
        this.idUserSubscribe = idUserSubscribe;
    }

    public Subscriber() {
    }

    public String getIdSubscriber() {
        return idSubscriber;
    }

    public void setIdSubscriber(String idSubscriber) {
        this.idSubscriber = idSubscriber;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdUserSubscribe() {
        return idUserSubscribe;
    }

    public void setIdUserSubscribe(String idUserSubscribe) {
        this.idUserSubscribe = idUserSubscribe;
    }
}
