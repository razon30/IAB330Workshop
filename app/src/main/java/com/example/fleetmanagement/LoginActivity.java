package com.example.fleetmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fleetmanagement.Utils.SharedPrefManager;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setViewIds();

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Perform login authentication logic here
            if (isValidCredentials(email, password)) {
                // Successful login, navigate to next activity
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                SharedPrefManager.setLoginState(true);
                Intent intent = new Intent(this, VehicleListActivity.class);
                startActivity(intent);

            } else {
                // Invalid credentials, show error message
                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                emailEditText.setError("Invalid email");
                passwordEditText.setError("Invalid password");            }
        });
    }

    private void setViewIds() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
    }

    private boolean isValidCredentials(String email, String password) {
        // Perform validation logic here
        // Return true if credentials are valid, false otherwise
        return email.equals("ex@ex.com") && password.equals("pass123");
    }
}

