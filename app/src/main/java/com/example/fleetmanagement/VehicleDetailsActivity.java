package com.example.fleetmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class VehicleDetailsActivity extends AppCompatActivity {

    TextView tvVehicleName, tvVehicleType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        tvVehicleName = findViewById(R.id.tvVehicleName);
        tvVehicleType = findViewById(R.id.tvVehicleType);

    }
}