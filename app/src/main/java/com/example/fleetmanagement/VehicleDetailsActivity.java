package com.example.fleetmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fleetmanagement.DB.VehicleDao;
import com.example.fleetmanagement.Utils.MyApp;

public class VehicleDetailsActivity extends AppCompatActivity {

    TextView tvVehicleName, tvVehicleType;
    Button btnUpdate, btnDelete;
    VehicleDao vehicleDao;
    int vehicleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        tvVehicleName = findViewById(R.id.tvVehicleName);
        tvVehicleType = findViewById(R.id.tvVehicleType);

        vehicleDao = MyApp.getAppDatabase().vehicleDao();

        if (getIntent().getIntExtra("vehicleId", -1) != -1){
            vehicleId = getIntent().getIntExtra("vehicleId", -1);

            vehicleDao.getVehicleById(vehicleId).observe(this, vehicle -> {
                tvVehicleName.setText("Vehicle Name: "+ vehicle.getName());
                tvVehicleType.setText("Vehicle Type: "+ vehicle.getType());
            });

        }else {
            Toast.makeText(this, "No Vehicle Id Found", Toast.LENGTH_SHORT).show();
        }

    }
}