package com.example.afiat.api;

import android.content.Context;

import com.example.afiat.datastore.AchievementStore;
import com.example.afiat.datastore.BasicStore;
import com.example.afiat.datastore.FetchStoreListener;
import com.example.afiat.datastore.PersistListener;
import com.example.afiat.datastore.ProfileStore;
import com.example.afiat.datastore.TrackingStore;
import com.example.afiat.model.Account;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {
    private static final String BASE_URL = "http://103.129.220.173:7000";

    public static Retrofit getInstance() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static void persistAchievement(String identifier, AchievementStore achievement, final PersistListener callback){
        AfiatAPI api = getInstance().create(AfiatAPI.class);
        Call<Void> call = api.updateAchievement(
                identifier,
                achievement.getMaxSteps(),
                String.valueOf(achievement.getHighestSpeed()),
                String.valueOf(achievement.getLongestDistance())
        );
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 204) {
                    callback.onFinish(true);
                } else {
                    callback.onFinish(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFinish(false);
                t.printStackTrace();
            }
        });
    }

    public static void persistProfile(String identifier, ProfileStore profile, final PersistListener callback){
        AfiatAPI api = getInstance().create(AfiatAPI.class);
        Call<Void> call = api.updateProfile(
                identifier,
                String.valueOf(profile.getHomeLat()),
                String.valueOf(profile.getHomeLng())
        );
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 204) {
                    callback.onFinish(true);
                } else {
                    callback.onFinish(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFinish(false);
                t.printStackTrace();
            }
        });
    }

    public static void persistTracking(String identifier, TrackingStore tracking, final PersistListener callback){
        AfiatAPI api = getInstance().create(AfiatAPI.class);
        Call<Void> call = api.updateTracking(
                identifier,
                tracking.getLastTrackingTime()
        );
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 204) {
                    callback.onFinish(true);
                } else {
                    callback.onFinish(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFinish(false);
                t.printStackTrace();
            }
        });
    }

    public static void fetchAccount(final Context context, String identifier, final FetchStoreListener callback) {
        AfiatAPI api = getInstance().create(AfiatAPI.class);
        Call<Account> call = api.getAccountData(identifier);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.code() == 200) {
                    AchievementStore achievement = new AchievementStore(
                            context,
                            response.body().getMaxSteps(),
                            Float.parseFloat(response.body().getMaxSpeedAverage()),
                            Float.parseFloat(response.body().getMaxDistance())
                    );
                    TrackingStore tracking = new TrackingStore(context, response.body().getLastTrackingStart());
                    ProfileStore profile = new ProfileStore(
                            context,
                            response.body().getHomeLatitude(),
                            response.body().getHomeLongitude()
                    );

                    BasicStore[] results = new BasicStore[3];
                    results[0] = achievement;
                    results[1] = tracking;
                    results[2] = profile;

                    callback.onFinish(true, results);
                } else {
                    callback.onFinish(false, null);
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                callback.onFinish(false, null);
                t.printStackTrace();
            }
        });
        BasicStore[]stores = new BasicStore[3];
        stores[0] = new AchievementStore(context);
        stores[1] = new ProfileStore(context);
        stores[2] = new TrackingStore(context);
        callback.onFinish(false, stores);
    }
}
