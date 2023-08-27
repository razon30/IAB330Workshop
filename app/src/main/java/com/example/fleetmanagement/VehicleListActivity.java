package com.example.fleetmanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fleetmanagement.DB.Vehicle;
import com.example.fleetmanagement.DB.VehicleDao;
import com.example.fleetmanagement.SensorUtil.FallDetectionService;
import com.example.fleetmanagement.Utils.MyApp;
import com.example.fleetmanagement.Utils.SharedPrefManager;
import java.util.ArrayList;
import java.util.List;

public class VehicleListActivity extends AppCompatActivity {

    ArrayList<Vehicle> vehicleList;
    private RecyclerView recyclerView;
    private Button btnAddNewVehicle;
    private VehicleAdapter vehicleAdapter;
    private Intent serviceIntent;
    private VehicleVibrationDataReceiver dataReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        viewBinding();
        initialising();
        getDataFromDatabase();
        handleClickOnVehicleItem();
        manageRoleBasedFeatures();
        manageSensorServices();

    }

    private void manageSensorServices() {
        // Start the FallDetectionService
        serviceIntent = new Intent(this, FallDetectionService.class);
        startService(serviceIntent);

        // Register the BroadcastReceiver
        dataReceiver = new VehicleVibrationDataReceiver(); // Initialising the varibale
        IntentFilter filter = new IntentFilter("VEHICLE_VIBRATION_DATA");// creating an instance of the filter that returns the vibrarion data
        registerReceiver(dataReceiver, filter); // registering the filter with our dataReceiver
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the BroadcastReceiver
        if (dataReceiver != null) {
            unregisterReceiver(dataReceiver);
        }
    }

    private class VehicleVibrationDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("VEHICLE_VIBRATION_DATA")) {
                // Receive the sensor data and update the UI if required.
            }
        }
    }


    private void manageRoleBasedFeatures() {
        if (SharedPrefManager.isAdmin()) {
            btnAddNewVehicle.setVisibility(View.VISIBLE);
            btnAddNewVehicle.setOnClickListener(view -> {
                manageNewVehicleFunctionality();
            });
        } else {
            btnAddNewVehicle.setVisibility(View.GONE);
        }
    }

    private void handleClickOnVehicleItem() {
        vehicleAdapter.setOnItemClickListener(position -> {
            // Handle recyclerview item click here
            // For example, you can open a new activity
            Toast.makeText(VehicleListActivity.this,
                    vehicleList.get(position).getName(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(VehicleListActivity.this, VehicleDetailsActivity.class);
            Vehicle vehicle = vehicleList.get(position);
            intent.putExtra("vehicleId",vehicle.id);
            startActivity(intent);
        });
    }

    private void initialising() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vehicleList = new ArrayList<>();
        vehicleAdapter = new VehicleAdapter(vehicleList);
        recyclerView.setAdapter(vehicleAdapter);
    }

    private void viewBinding() {
        recyclerView = findViewById(R.id.recyclerView);
        btnAddNewVehicle = findViewById(R.id.btnAddVehicle);
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
            //vehicleAdapter.notifyDataSetChanged();
        });

        AlertDialog dialog = builder.create();
        builder.setNegativeButton("Cancel", null);
        dialog.show();

    }

    private void getDataFromDatabase() {
        // Retrieve all vehicles asynchronously using LiveData
        VehicleDao vehicleDao = MyApp.getAppDatabase().vehicleDao();
        LiveData<List<Vehicle>> vehiclesLiveData = vehicleDao.getAllVehicles();
        vehiclesLiveData.observe(this, vehicles -> {
            // Handle the list of vehicles here
            vehicleList.clear();
            vehicleList.addAll(vehicles);
            vehicleAdapter.notifyDataSetChanged();
        });
    }
}