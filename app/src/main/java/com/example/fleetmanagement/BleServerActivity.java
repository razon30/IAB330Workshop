package com.example.fleetmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.fleetmanagement.BLE.DataEnCryptDecrypt;
import com.google.android.material.snackbar.Snackbar;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

public class BleServerActivity extends AppCompatActivity {

    private BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    String TAG = "BleActivity";
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bleactivity);

        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth is not enabled", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Bluetooth is not enabled");
            return;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED) {
                startAdvertising();
            } else {
                requestBluetoothPermissions();
            }
        }else {
            startAdvertising();
        }

    }

    private void startAdvertising() {

        BluetoothLeAdvertiser bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .setConnectable(true)
                .build();

        // Build the manufacturer-specific data including major and minor values
        int major = 1000;  // Example major value
        int minor = 200;   // Example minor value
        ByteBuffer manufacturerData = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        manufacturerData.putShort((short) major);
        manufacturerData.putShort((short) minor);

        UUID customData = DataEnCryptDecrypt.encryptToUUID("A 16 bit message"); // 16 characters, not more, not less

        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName(false)
                .addServiceUuid(new ParcelUuid(customData))
                .addManufacturerData(0x004C, manufacturerData.array())
                .build();

        AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                Toast.makeText(BleServerActivity.this, "Advertising started", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Advertising started");
            }

            @Override
            public void onStartFailure(int errorCode) {
                super.onStartFailure(errorCode);
                Toast.makeText(BleServerActivity.this, "Advertising failed: " + errorCode, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Advertising failed");
            }
        };

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
            requestBluetoothPermissions();
        }else {
            bluetoothLeAdvertiser.startAdvertising(settings, data, advertiseCallback);
        }
    }


    private void requestBluetoothPermissions() {
        String[] permissions = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            permissions = new String[]{
                    Manifest.permission.BLUETOOTH_ADVERTISE,
            };
            ActivityCompat.requestPermissions(this, permissions, REQUEST_BLUETOOTH_PERMISSIONS);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            int grandetPermission = 0;
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    // Handle accordingly
                    grandetPermission++;

                    // Checking whether the number of granted permission is equal to the number or permission we requested.
                    if (grandetPermission == permissions.length) {
                        startAdvertising();
                    }
                } else if (grantResult == PackageManager.PERMISSION_DENIED) {
                    // Permission denied
                    // Handle denial (explain, request again, etc.)
                    if (!shouldShowRequestPermissionRationale(permission)) {
                        // User denied without asking again
                        // Explain why the permission is needed and prompt to go to settings
                        showPermissionExplanationDialog();
                    } else {
                        // User denied but can be asked again
                        // Explain why the permission is needed
                        showPermissionExplanationSnackbar();
                    }
                }
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showPermissionExplanationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Required")
                .setMessage("This app needs Bluetooth permissions for BLE communication. You can grant these permissions in the app settings.")
                .setPositiveButton("Go to Settings", (dialog, which) -> openAppSettings())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void showPermissionExplanationSnackbar() {
        Snackbar.make(findViewById(android.R.id.content), "Bluetooth permissions are needed for BLE communication.", Snackbar.LENGTH_LONG)
                .setAction("Grant", view -> requestBluetoothPermissions())
                .show();
    }


}