package com.example.afiat.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tracking {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("last_tracking_start")
    @Expose
    private Integer lastTrackingStart;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Integer getLastTrackingStart() {
        return lastTrackingStart;
    }

    public void setLastTrackingStart(Integer lastTrackingStart) {
        this.lastTrackingStart = lastTrackingStart;
    }

}
