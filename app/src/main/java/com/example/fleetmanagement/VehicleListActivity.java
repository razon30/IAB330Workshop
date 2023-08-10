package com.example.fleetmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
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


        recyclerView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                View childView = recyclerView.findChildViewUnder(event.getX(), event.getY());
                if (childView != null) {
                    int position = recyclerView.getChildAdapterPosition(childView);
                    if (position != RecyclerView.NO_POSITION) {
                        // Handle item click here
                        // For example, you can open a new activity or show a dialog
                        Toast.makeText(VehicleListActivity.this, vehicleList.get(position).getVehicleNumber(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            return false;
        });




    }

    // Replace this method with your actual vehicle data
    private ArrayList<Vehicle> generateDummyData() {
        ArrayList<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(new Vehicle("Car 1", "Sedan", "1231"));
        vehicleList.add(new Vehicle("Truck 1", "Heavy Duty", "1231sfs"));
        vehicleList.add(new Vehicle("Car 2", "Sedan", "fafa"));
        vehicleList.add(new Vehicle("Truck 2", "Heavy Duty", "1afas"));
        vehicleList.add(new Vehicle("Car 3", "Sedan", "1231afa"));
        vehicleList.add(new Vehicle("Truck 3", "Heavy Duty", "fwqda45"));
        // Add more vehicles as needed
        return vehicleList;
    }
}