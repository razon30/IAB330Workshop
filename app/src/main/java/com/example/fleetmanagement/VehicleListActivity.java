package com.example.fleetmanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fleetmanagement.Utils.SharedPrefManager;

import java.util.ArrayList;

public class VehicleListActivity extends AppCompatActivity {

    ArrayList<Vehicle> vehicleList;
    private RecyclerView recyclerView;
    private Button btnAddNewVehicle;
    private VehicleAdapter vehicleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        recyclerView = findViewById(R.id.recyclerView);
        btnAddNewVehicle = findViewById(R.id.btnAddVehicle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        vehicleList = generateDummyData(); // Replace this with your actual vehicle data

        vehicleAdapter = new VehicleAdapter(vehicleList);
        recyclerView.setAdapter(vehicleAdapter);

        if (SharedPrefManager.isAdmin()) {
            btnAddNewVehicle.setVisibility(View.VISIBLE);
            btnAddNewVehicle.setOnClickListener(view -> {
                manageNewVehicleFunctionality();
            });
        } else {
            btnAddNewVehicle.setVisibility(View.GONE);
        }

    }

    private void manageNewVehicleFunctionality() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_vehicle, null);
        builder.setView(dialogView);

        EditText editTextVehicleName = dialogView.findViewById(R.id.editTextVehicleName);
        EditText editTextVehicleType = dialogView.findViewById(R.id.editTextVehicleType);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String vehicleName = editTextVehicleName.getText().toString();
            String vehicleType = editTextVehicleType.getText().toString();

            // Add the new vehicle to the list
            Vehicle vehicle = new Vehicle(vehicleName, vehicleType);
            vehicleList.add(0, vehicle); // Adding the vehicle at the beginning of the list (index = 0)
            // Refresh the RecyclerView
            vehicleAdapter.notifyDataSetChanged();
        });

        AlertDialog dialog = builder.create();
        builder.setNegativeButton("Cancel", null);
        dialog.show();

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