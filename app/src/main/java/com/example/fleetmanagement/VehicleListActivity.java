package com.example.fleetmanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fleetmanagement.DB.Vehicle;
import com.example.fleetmanagement.DB.VehicleDao;
import com.example.fleetmanagement.Utils.MyApp;
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

        vehicleAdapter.setOnItemClickListener(position -> {
            // Handle recyclerview item click here
            // For example, you can open a new activity
            Toast.makeText(VehicleListActivity.this,
                    vehicleList.get(position).getName(), Toast.LENGTH_SHORT).show();
        });

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

            VehicleDao vehicleDao = MyApp.getAppDatabase().vehicleDao();
            AsyncTask.execute(() -> {
                vehicleDao.insert(vehicle);
            });


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

        // Retrieve all vehicles asynchronously using LiveData
        VehicleDao vehicleDao = MyApp.getAppDatabase().vehicleDao();
        LiveData<ArrayList<Vehicle>> vehiclesLiveData = vehicleDao.getAllVehicles();
        vehiclesLiveData.observe(this, vehicles -> {
            // Handle the list of vehicles here
            vehicleList.addAll(vehicles);
        });

        // Add more vehicles as needed
        return vehicleList;
    }
}