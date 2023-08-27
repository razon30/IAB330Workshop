package com.example.fleetmanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fleetmanagement.DB.Vehicle;
import com.example.fleetmanagement.DB.VehicleDao;
import com.example.fleetmanagement.Utils.MyApp;

public class VehicleDetailsActivity extends AppCompatActivity {

    TextView tvVehicleName, tvVehicleType;
    Button btnUpdate, btnDelete;
    VehicleDao vehicleDao;
    int vehicleId;
    Vehicle vehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        bindViews();
        initialisation();
        populatingVehicleDetails();



    }

    private void populatingVehicleDetails() {

        if (getIntent().getIntExtra("vehicleId", -1) != -1){
            vehicleId = getIntent().getIntExtra("vehicleId", -1);
            retrievingAndSettingVehicleData(vehicleId);
            handlingUpdateEvent();
            handlingDeleteEvent();
        }else {
            Toast.makeText(this, "No Vehicle Id Found", Toast.LENGTH_SHORT).show();
        }

    }

    private void handlingUpdateEvent() {
        btnUpdate.setOnClickListener(view -> {
            if (vehicle != null) {
                updateTheVehicle();
            }
        });
    }

    private void handlingDeleteEvent() {
        btnDelete.setOnClickListener(view -> {
            if (vehicle != null) {
                deleteTheVehicle();
            }
        });
    }

    private void retrievingAndSettingVehicleData(int vehicleId) {
        vehicleDao.getVehicleById(vehicleId).observe(this, dbVehicle -> {
            if (dbVehicle != null) {
                this.vehicle = dbVehicle;
                tvVehicleName.setText("Vehicle Name: " + this.vehicle.getName());
                tvVehicleType.setText("Vehicle Type: " + this.vehicle.getType());
            }
        });
    }

    private void initialisation() {
        vehicleDao = MyApp.getAppDatabase().vehicleDao();
    }

    private void bindViews() {
        tvVehicleName = findViewById(R.id.tvVehicleName);
        tvVehicleType = findViewById(R.id.tvVehicleType);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
    }


    private void updateTheVehicle() {

        // Creating the view to create the dialog. We are re-using the dialog we created in Week-4 to add new vehicle.
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_vehicle, null);
        EditText editTextVehicleName = dialogView.findViewById(R.id.editTextVehicleName);
        EditText editTextVehicleType = dialogView.findViewById(R.id.editTextVehicleType);

        // Pre-set the current vehicle name and type to these edittext views.
        editTextVehicleName.setText(vehicle.getName());
        editTextVehicleType.setText(vehicle.getType());

        //Creating the dialog builder to create the pop up dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        //Setting and implementing the update operation
        builder.setPositiveButton("Update", (dialog, which) -> {
            String vehicleName = editTextVehicleName.getText().toString();
            String vehicleType = editTextVehicleType.getText().toString();

            //update the values
            vehicle.setName(vehicleName);
            vehicle.setType(vehicleType);

            AsyncTask.execute(() -> {
                vehicleDao.update(vehicle);
            });
        });

        // Setting the update cancellation
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Do nothing or handle any other actions
            dialog.cancel();
        });

        //Creating and showing the dialog.
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void deleteTheVehicle() {
        AsyncTask.execute(() -> {
            vehicleDao.delete(vehicle);
            finish();
        });
    }

}