package com.example.afiat.screen.main;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.example.afiat.R;
import com.example.afiat.datastore.AchievementStore;
import com.example.afiat.datastore.PersistListener;
import com.example.afiat.datastore.StoreManager;
import com.example.afiat.service.OnSensorChangeListener;
import com.example.afiat.service.ServiceRunner;

import static com.example.afiat.location.LocationNotifier.STATUS_OK;
import static com.example.afiat.location.LocationNotifier.STATUS_PROHIBITTED;

public class MainPresenter implements MainEvent.Presenter {
    private MainBus bus;
    private MainContract.View view;
    private ServiceRunner runner;
    private Context context;

    private int currentSteps;
    private float currentSpeedAvg;
    private float currentDistance;

    public MainPresenter(MainBus bus, MainContract.View view, Context context) {
        this.bus = bus;
        this.view = view;
        this.context = context;

        bus.setPresenter(this);
    }

    @Override
    public void onStart() {
        runner = new ServiceRunner(context, callback);
        if (runner.isWorkerServiceRunning()) {
            runner.bind();
            view.setToggleName(context.getString(R.string.turn_off));
        } else {
            view.setToggleName(context.getString(R.string.turn_on));
        }
    }

    private OnSensorChangeListener callback = new OnSensorChangeListener() {
        @Override
        public void stepsChanged(int value) {
            view.setSteps(value);
            currentSteps = value;
        }

        @Override
        public void distanceChanged(float value) {
            view.setDistance(value);
            currentDistance = value;
        }

        @Override
        public void speedChanged(float value) {
            view.setSpeed(value);
            currentSpeedAvg = value;
        }

        @Override
        public void locationUpdated(double latitude, double longitude, short status) {
            if (status == STATUS_OK) {
                view.setLocation((float) latitude, (float) longitude);
            } else if (status == STATUS_PROHIBITTED) {
                bus.askPermission();
            }
        }

        @Override
        public void onLightChange(float lightLevel) {
            if (lightLevel < 80) {
                bus.setBackgroundColor(Color.parseColor("#555555"));
            } else {
                bus.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }

        @Override
        public void onGravityChange(float x, float y, float z) { }
    };

    @Override
    public void onToggleService() {
        if (runner.isWorkerServiceRunning()) {
            PersistListener listener = new PersistListener() {
                @Override
                public void onFinish(boolean success) {
                    runner.stop();
                    view.setToggleName(context.getString(R.string.turn_on));
                    bus.refreshPager();
                }
            };
            analyzeAchievement(listener);
        } else {
            PersistListener listener = new PersistListener() {
                @Override
                public void onFinish(boolean success) {
                    runner.bind();
                    view.setToggleName(context.getString(R.string.turn_off));
                }
            };
            saveTurnOnTimestamp(listener);
        }
    }

    @Override
    public void onDestroy() {
        runner.unbind();
    }

    private void analyzeAchievement(PersistListener listener) {
        StoreManager manager = StoreManager.getInstance(context);
        AchievementStore store = manager.getAchievement();
        if (store.getMaxSteps() < currentSteps) {
            store.setMaxSteps(currentSteps);
        }
        if (store.getHighestSpeed() < currentSpeedAvg) {
            store.setHighestSpeed(currentSpeedAvg);
        }
        if (store.getLongestDistance() < currentDistance) {
            store.setLongestDistance(currentDistance);
        }
        manager.persistAchievement(listener);
    }

    private void saveTurnOnTimestamp(PersistListener listener) {
        StoreManager manager = StoreManager.getInstance(context);
        manager.getTracking().setLastTrackingTime(System.currentTimeMillis());
        manager.persistTracking(listener);
    }

    @Override
    public void onPermissionGranted() {
        runner.getService().activateLocationListener();
    }
}
