package com.example.fleetmanagement.SensorData;

public class VibrationData {
    private long timestamp;
    private float xAcceleration;
    private float yAcceleration;
    private float zAcceleration;

    public VibrationData(long timestamp, float xAcceleration, float yAcceleration, float zAcceleration) {
        this.timestamp = timestamp;
        this.xAcceleration = xAcceleration;
        this.yAcceleration = yAcceleration;
        this.zAcceleration = zAcceleration;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getxAcceleration() {
        return xAcceleration;
    }

    public void setxAcceleration(float xAcceleration) {
        this.xAcceleration = xAcceleration;
    }

    public float getyAcceleration() {
        return yAcceleration;
    }

    public void setyAcceleration(float yAcceleration) {
        this.yAcceleration = yAcceleration;
    }

    public float getzAcceleration() {
        return zAcceleration;
    }

    public void setzAcceleration(float zAcceleration) {
        this.zAcceleration = zAcceleration;
    }
}
