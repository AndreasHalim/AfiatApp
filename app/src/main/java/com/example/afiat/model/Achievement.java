package com.example.afiat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Achievement {
    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("max_steps")
    @Expose
    private Integer maxSteps;

    @SerializedName("max_speed_average")
    @Expose
    private String maxSpeedAverage;

    @SerializedName("max_distance")
    @Expose
    private String maxDistance;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Integer getMaxSteps() {
        return maxSteps;
    }

    public void setMaxSteps(Integer maxSteps) {
        this.maxSteps = maxSteps;
    }

    public String getMaxSpeedAverage() {
        return maxSpeedAverage;
    }

    public void setMaxSpeedAverage(String maxSpeedAverage) {
        this.maxSpeedAverage = maxSpeedAverage;
    }

    public String getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(String maxDistance) {
        this.maxDistance = maxDistance;
    }

}