package com.example.afiat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afiat.datastore.PersistListener;
import com.example.afiat.datastore.StoreManager;
import com.example.afiat.screen.achievement.AchievementBus;
import com.example.afiat.screen.achievement.AchievementFragment;
import com.example.afiat.screen.achievement.AchievementPresenter;
import com.example.afiat.screen.main.MainBus;
import com.example.afiat.screen.main.MainEvent;
import com.example.afiat.screen.main.MainFragment;
import com.example.afiat.screen.main.MainPresenter;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends FragmentActivity implements MainEvent.Activity {
    private static final int REQUEST_ACCESS_FINE_LOCATION = 99;

    @BindView(R.id.pager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabs;
    @BindView(R.id.tv_logout) TextView tvLogout;
    @BindView(R.id.root) View rootView;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private PagerAdapter pagerAdapter;
    private boolean isRequestingPermission = false;
    private MainBus mainBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setupFirebaseAuth();

        pagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tab = tabs.getTabAt(position);
                if (tab != null) {
                    tab.select();
                }
            }
        });

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
            }
        };

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreManager manager = StoreManager.getInstance(getApplicationContext());
                manager.persistAndFlush(new PersistListener() {
                    @Override
                    public void onFinish(boolean success) {
                        if (success) {
                            Toast.makeText(getApplicationContext(), "Data is successfully persisted!", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Data persistent is failed!", Toast.LENGTH_SHORT)
                                    .show();
                        }
                        mAuth.signOut();
                    }
                });
            }
        });
    }

    private class CustomPagerAdapter extends FragmentStatePagerAdapter {
        private static final int TABS_COUNT = 2;

        CustomPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    mainBus = new MainBus(HomeActivity.this);
                    MainFragment mainFragment = MainFragment.newInstance(mainBus);
                    new MainPresenter(mainBus, mainFragment, getApplicationContext());
                    return mainFragment;
                case 1:
                    AchievementBus achievementBus = new AchievementBus();
                    AchievementFragment achievementFragment = AchievementFragment.newInstance(achievementBus);
                    AchievementPresenter achievementPresenter = new AchievementPresenter(achievementFragment, getApplicationContext());
                    achievementBus.setPresenter(achievementPresenter);
                    return achievementFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return TABS_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return "Main";
                case 1: return "Achievement";
            }
            return null;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void refreshPager() {
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void setBackgroundColor(int color) {
        rootView.setBackgroundColor(color);
    }

    @Override
    public void askPermission() {
        if (!isRequestingPermission) {
            isRequestingPermission = true;
            requestLocationPermissison();
        }
    }

    private void requestLocationPermissison() {
        ActivityCompat.requestPermissions(
                HomeActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_ACCESS_FINE_LOCATION
        );
    }

    @Override
    public void onRequestPermissionsResult(int rc, @NonNull String permissions[], @NonNull int[] results) {
        switch (rc) {
            case REQUEST_ACCESS_FINE_LOCATION: {
                if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                    mainBus.onPermissionGranted();
                } else {
                    Toast.makeText(this, "Location service is not activated.", Toast.LENGTH_SHORT).show();
                }
                isRequestingPermission = false;
            }
        }
    }
}
