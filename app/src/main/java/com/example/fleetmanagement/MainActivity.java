package com.example.fleetmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fleetmanagement.Utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity {

    // Objects
    private TextView titleTextView;
    private Button loginButton;
    private Button signupButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SharedPrefManager.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, VehicleListActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_main);
            //Linking the views
            setViewIds();

            //Setting Click listeners
            loginButton.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            });

            signupButton.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            });
        }
    }

    private void setViewIds() {
        titleTextView = findViewById(R.id.title);
        loginButton = findViewById(R.id.btnLogin);
        signupButton = findViewById(R.id.btnSignup);
    }
}