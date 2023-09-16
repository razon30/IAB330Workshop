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

import com.example.fleetmanagement.DB.vehicle.Vehicle;
import com.example.fleetmanagement.DB.vehicle.VehicleDao;
import com.example.fleetmanagement.DB.sensor.AccelerometerData;
import com.example.fleetmanagement.SensorUtil.SensorService;
import com.example.fleetmanagement.Utils.MyApp;
import com.example.fleetmanagement.Utils.SharedPrefManager;
import java.util.ArrayList;
import java.util.List;

public class VehicleListActivity extends AppCompatActivity {

    ArrayList<Vehicle> vehicleList;
    private RecyclerView recyclerView;
    private Button btnAddNewVehicle;
    private VehicleAdapter vehicleAdapter;
    private Button btnSensorData;

    Intent serviceIntent;
    SensorDatReceiver dataReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        viewBinding();
        initialising();
        getDataFromDatabase();
        handleClickOnVehicleItem();
        manageRoleBasedFeatures();
        manageSensorData();

    }

    private void manageSensorData() {

        serviceIntent = new Intent(this, SensorService.class);
        startService(serviceIntent);

        dataReceiver = new SensorDatReceiver();
        IntentFilter filter = new IntentFilter("VEHICLE_SENSOR_DATA");
        registerReceiver(dataReceiver, filter);

    }


    private class SensorDatReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("VEHICLE_SENSOR_DATA")) {
                // Receive the sensor data and update the UI if required.

                AccelerometerData accelerometerData = (AccelerometerData)
                        intent.getSerializableExtra("accelerometerData");
                // Making changes in the UI based on sensor data
                if (accelerometerData != null && accelerometerData.getMagnitude()
                        < 9.81){

                    getWindow().getDecorView().setBackgroundColor(getResources().getColor
                            (R.color.white, null));
                }else {
//                    Toast.makeText(VehicleListActivity.this, "Danger!!!",
//                            Toast.LENGTH_SHORT).show();

                    getWindow().getDecorView().setBackgroundColor(getResources().getColor
                            (R.color.red, null));
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(dataReceiver);
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
        btnSensorData = findViewById(R.id.btnSensorData);
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