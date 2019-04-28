package com.example.afiat.api;

import com.example.afiat.model.Account;
import com.example.afiat.model.Achievement;
import com.example.afiat.model.Tracking;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AfiatAPI {
    @GET("/{identifier}")
    Call<Account> getAccountData(@Path("identifier") String identifier);

    @PUT("/{identifier}/tracking")
    @FormUrlEncoded
    Call<Void> updateTracking(@Path("identifier") String identifier,
                              @Field("last_tracking_start") Long lastTrackingStart);

    @PUT("/{identifier}/achievement")
    @FormUrlEncoded
    Call<Void> updateAchievement(@Path("identifier") String identifier,
                                 @Field("max_steps") Integer steps,
                                 @Field("max_speed_average") String speed,
                                 @Field("max_distance") String distance);

    @PUT("/{identifier}/profile")
    @FormUrlEncoded
    Call<Void> updateProfile(@Path("identifier") String identifier,
                             @Field("latitude") String latitude,
                             @Field("longitude") String longitude);
}
