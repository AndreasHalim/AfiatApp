package com.example.afiat.screen.main;

import android.graphics.Color;

public interface MainEvent {
    interface Presenter {
        void onStart();
        void onToggleService();
        void onDestroy();
        void onPermissionGranted();
    }

    public interface Activity {
        void refreshPager();
        void setBackgroundColor(int color);
        void askPermission();
    }
}
