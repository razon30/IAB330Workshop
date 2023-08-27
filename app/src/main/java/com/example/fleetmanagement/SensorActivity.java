package com.example.fleetmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private static final float THRESHOLD = 9.8f * 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            Log.d("Sensor data ", "Ambient temperature is: "+sensorEvent.values[0]+" C");
            if (sensorEvent.values[0] > 40){
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.red, null));
            }else {
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.white, null));
            }
        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE){
            Log.d("Sensor data ", "Ambient pressure is is: "+sensorEvent.values[0]+" millibar");
        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            double magnitude = Math.sqrt(x * x + y * y + z * z);

            Log.d("Sensor data ", "Acceleration towards X, Y, and Z "+ Arrays.toString(sensorEvent.values) +" and magnitude: "+magnitude);

            if (magnitude > THRESHOLD){
                Log.d("Sensor fall data ", "Fall detected");
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.red, null));
            }else {
                getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.white, null));
            }

        }

        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE){


        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            if (accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE){
                // Handle the unreliable accelerometer data
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            Sensor tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            sensorManager.registerListener(this, tempSensor, 10 * 1000000);
        }else {
            Log.d("Sensor data ", "No Ambient temperature Sensor Found");
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
            Sensor pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            sensorManager.registerListener(this, pressureSensor, 10 * 1000000);
        }else {
            Log.d("Sensor data ", "No Ambient pressure Sensor Found");
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, 10 * 1000000);
        }else {
            Log.d("Sensor data ", "No Accelerometer Sensor Found");
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            sensorManager.registerListener(this, accelerometer, 10 * 1000000);
        }else {
            Log.d("Sensor data ", "No Gyroscope Sensor Found");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}