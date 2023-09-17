package com.example.fleetmanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.example.fleetmanagement.DB.sensor.TemperatureDao;
import com.example.fleetmanagement.DB.sensor.TemperatureData;
import com.example.fleetmanagement.DB.vehicle.Vehicle;
import com.example.fleetmanagement.DB.vehicle.VehicleDao;
import com.example.fleetmanagement.Utils.MyApp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        analyseTemperatureData();
        analyseAccelerometerData();

    }

    private void analyseTemperatureData() {

        // Define the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        // Initialising the chart view with the AnyChart variable
        AnyChartView tempChartView = findViewById(R.id.tempChart);
        // Adding the progress bar for loading the chart
        tempChartView.setProgressBar(findViewById(R.id.progress_bar));

        // Creating an instance of the line chart.
        Cartesian tempLineChart = AnyChart.line();
        tempLineChart.animation(true); // Setting Animation for the chart
        tempLineChart.padding(10d, 20d, 5d, 20d);

        // Setting the tap on value bar with X-value (Cross between the temperature and corresponding time)
        tempLineChart.crosshair().enabled(true);
        tempLineChart.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        // Setting a point for tapping on the chart.
        tempLineChart.tooltip().positionMode(TooltipPositionMode.POINT);

        tempLineChart.title("Temperature analysis over the time.");
        tempLineChart.yAxis(0).title("Temperature (Degree celcius)");
        tempLineChart.xAxis(0).title("Timestamp");
        tempLineChart.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
        tempLineChart.legend().enabled(true);
        tempLineChart.legend().fontSize(13d);
        tempLineChart.legend().padding(0d, 0d, 10d, 0d);

        // Initialising data for the chart
        List<DataEntry> seriesData = new ArrayList<>();
        Set set = Set.instantiate(); // Initiating AnyChart set to synch the custom data with AnyChart data
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }"); // Map for mapping values in X-Axis (time) with Y-axix (temperature)

        // Creating series for first car. For multiple car, OR multiple lines of data, we need to repeat all the following 'series1' codes for each of them
        Line series1 = tempLineChart.line(series1Mapping);
        series1.name(tvVehicleName.getText().toString());
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        tempChartView.setChart(tempLineChart); // adding the chart to the chart view

        TemperatureDao temperatureDao = MyApp.getAppDatabase().temperatureDao();
        LiveData<List<TemperatureData>> temperatureLiveData = temperatureDao.getAllTemperatureData();
        temperatureLiveData.observe(this, temperatureDataList -> {
            // Handle the list of vehicles here
            for(TemperatureData temperatureData: temperatureDataList){
                Date date = new Date(temperatureData.getTimeStamp());
                // converting our data into AnyChart customdata and adding them to seriesData
                seriesData.add(new CustomDataEntry(dateFormat.format(date), temperatureData.getTemp()));
            }

            // Setting the series data into the set method of AnyChart library.
            set.data(seriesData);

        });



    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value) {
            super(x, value);
        }

    }

    private void analyseAccelerometerData() {

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