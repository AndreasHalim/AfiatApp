package com.example.afiat.screen.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.afiat.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment implements MainContract.View {
    @BindView(R.id.tv_steps) TextView tvSteps;
    @BindView(R.id.tv_speed) TextView tvSpeed;
    @BindView(R.id.tv_distance) TextView tvDistance;
    @BindView(R.id.btn_toggle) Button btnToggle;
    @BindView(R.id.tv_latitude) TextView tvLatitude;
    @BindView(R.id.tv_longitude) TextView tvLongitude;

    private static MainBus mainBus;

    public static MainFragment newInstance(MainBus bus) {
        MainFragment fragment = new MainFragment();
        mainBus = bus;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainBus.onToggleService();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mainBus.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mainBus.onDestroy();
    }

    @Override
    public void setSteps(int steps) {
        tvSteps.setText(String.valueOf(steps));
    }

    @Override
    public void setSpeed(float speed) {
        tvSpeed.setText(String.valueOf(speed));
    }

    @Override
    public void setDistance(float distance) {
        tvDistance.setText(String.valueOf(distance));
    }

    @Override
    public void setToggleName(String message) {
        btnToggle.setText(message);
    }

    @Override
    public void setLocation(float latitude, float longitude) {
        tvLatitude.setText(String.valueOf(latitude));
        tvLongitude.setText(String.valueOf(longitude));
    }
}
