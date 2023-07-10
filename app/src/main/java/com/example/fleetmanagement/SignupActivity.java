package com.example.fleetmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    Button signupButton;
    EditText nameEditText;
    EditText emailEditText;
    EditText phoneEditText;
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setViewIds();

        signupButton.setOnClickListener(view -> {
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (!isValidName(name)) {
                nameEditText.setError("Invalid name");
                return;
            }

            if (!isValidEmail(email)) {
                emailEditText.setError("Invalid email");
                return;
            }

            if (!isValidPhoneNumber(phone)) {
                phoneEditText.setError("Invalid phone number");
                return;
            }

            if (!isValidPassword(password)) {
                passwordEditText.setError("Invalid password");
                return;
            }

            // If everything is okay, show success message
            Toast.makeText(SignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
        });

    }

    private void setViewIds() {
        signupButton = findViewById(R.id.signupButton);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
    }

    private boolean isValidName(String name) {
        // Check if name has at least 3 characters and contains only alphabets
        return name.length() >= 3 && name.matches("[a-zA-Z]+");
    }

    private boolean isValidEmail(String email) {
        // Check if email is in proper format using regex pattern
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPhoneNumber(String phone) {
        // Check if phone number is in proper format using regex pattern
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private boolean isValidPassword(String password) {
        // Check if password has at least 5 characters
        return password.length() >= 5;
    }

}