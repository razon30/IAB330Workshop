package com.example.fleetmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class VehicleListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VehicleAdapter vehicleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Vehicle> vehicleList = generateDummyData(); // Replace this with your actual vehicle data

        vehicleAdapter = new VehicleAdapter(vehicleList);
        recyclerView.setAdapter(vehicleAdapter);

        vehicleAdapter.setOnItemClickListener(position -> {
            // Handle recyclerview item click here
            // For example, you can open a new activity
            Toast.makeText(VehicleListActivity.this,
                    vehicleList.get(position).getName(), Toast.LENGTH_SHORT).show();
        });
    }

    // Replace this method with your actual vehicle data
    private ArrayList<Vehicle> generateDummyData() {
        ArrayList<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(new Vehicle("Car 1", "Sedan"));
        vehicleList.add(new Vehicle("Truck 1", "Heavy Duty"));
        vehicleList.add(new Vehicle("Car 2", "Sedan"));
        vehicleList.add(new Vehicle("Truck 2", "Heavy Duty"));
        vehicleList.add(new Vehicle("Car 3", "Sedan"));
        vehicleList.add(new Vehicle("Truck 3", "Heavy Duty"));
        vehicleList.add(new Vehicle("Car 4", "Sedan"));
        vehicleList.add(new Vehicle("Truck 4", "Heavy Duty"));
        vehicleList.add(new Vehicle("Car 5", "Sedan"));
        vehicleList.add(new Vehicle("Truck 5", "Heavy Duty"));
        vehicleList.add(new Vehicle("Car 6", "Sedan"));
        vehicleList.add(new Vehicle("Truck 6", "Heavy Duty"));
        vehicleList.add(new Vehicle("Car 7", "Sedan"));
        vehicleList.add(new Vehicle("Truck 7", "Heavy Duty"));
        vehicleList.add(new Vehicle("Car 8", "Sedan"));
        vehicleList.add(new Vehicle("Truck 8", "Heavy Duty"));
        // Add more vehicles as needed
        return vehicleList;
    }
}