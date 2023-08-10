package com.example.fleetmanagement;

public class Vehicle {
    private String name;
    private String type;
    private String vehicleNumber;

    public Vehicle(String name, String type, String vehicleNumber) {
        this.name = name;
        this.type = type;
        this.vehicleNumber = vehicleNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}
