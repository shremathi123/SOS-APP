package com.example.sahaya;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText name, phone, password, confirmPassword;
    Button signupBtn;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // UI
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signupBtn = findViewById(R.id.signupBtn);

        // Button Click
        signupBtn.setOnClickListener(v -> {

            String n = name.getText().toString().trim();
            String ph = phone.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String confirm = confirmPassword.getText().toString().trim();

            // Validation
            if (n.isEmpty()) {
                name.setError("Enter name");
                return;
            }

            if (ph.isEmpty()) {
                phone.setError("Enter phone number");
                return;
            }

            if (ph.length() != 10) {
                phone.setError("Enter valid 10-digit number");
                return;
            }

            if (pass.isEmpty()) {
                password.setError("Enter password");
                return;
            }

            if (!pass.equals(confirm)) {
                confirmPassword.setError("Passwords do not match");
                return;
            }

            // 🔥 SAVE TO FIREBASE
            String userId = databaseReference.push().getKey();

            databaseReference.child(userId).child("name").setValue(n);
            databaseReference.child(userId).child("phone").setValue(ph);
            databaseReference.child(userId).child("password").setValue(pass);

            Toast.makeText(SignupActivity.this,
                    "Data Saved Successfully!",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignupActivity.this, PersonalDetailsActivity.class);
            startActivity(intent);
        });
    }
}