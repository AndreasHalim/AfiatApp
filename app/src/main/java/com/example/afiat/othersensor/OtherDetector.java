package com.example.afiat.othersensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import java.util.ArrayList;

public class OtherDetector implements SensorEventListener {
    private ArrayList<OtherListener> listeners = new ArrayList<>();

    public void addListener(OtherListener listener) {
        listeners.add(listener);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_LIGHT) {
            for (OtherListener listener : listeners) {
                listener.onLightChange(event.values[0]);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}
