package com.example.fleetmanagement.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.fleetmanagement.DB.sensor.AccelerometerDao;
import com.example.fleetmanagement.DB.sensor.AccelerometerData;
import com.example.fleetmanagement.DB.sensor.TemperatureDao;
import com.example.fleetmanagement.DB.sensor.TemperatureData;
import com.example.fleetmanagement.DB.vehicle.Vehicle;
import com.example.fleetmanagement.DB.vehicle.VehicleDao;

@Database(entities = {Vehicle.class, AccelerometerData.class, TemperatureData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract VehicleDao vehicleDao();
    public abstract AccelerometerDao accelerometerDao();
    public abstract TemperatureDao temperatureDao();
}

