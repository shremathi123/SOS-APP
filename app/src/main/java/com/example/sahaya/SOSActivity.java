package com.example.sahaya;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.*;

public class SOSActivity extends AppCompatActivity {

    Button sosBtn, profileBtn;
    FusedLocationProviderClient fusedLocationClient;

    String phone1 = "", phone2 = "", phone3 = "";

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        sosBtn = findViewById(R.id.sosBtn);
        profileBtn = findViewById(R.id.profileBtn);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 1);

        databaseReference = FirebaseDatabase.getInstance().getReference("EmergencyContacts");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {

                    phone1 = data.child("contact1_phone").getValue(String.class);
                    phone2 = data.child("contact2_phone").getValue(String.class);
                    phone3 = data.child("contact3_phone").getValue(String.class);

                    break;
                }

                Toast.makeText(SOSActivity.this, "Contacts Loaded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SOSActivity.this, "Failed to load contacts", Toast.LENGTH_SHORT).show();
            }
        });

        sosBtn.setOnClickListener(v -> sendSOS());

        profileBtn.setOnClickListener(v -> {
            Toast.makeText(SOSActivity.this, "CLICK WORKING", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SOSActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void sendSOS() {

        sendSMS("🚨 EMERGENCY! I need help.");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                null
        ).addOnSuccessListener(location -> {

            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                String locationMsg = "📍 My Location:\nhttps://maps.google.com/?q="
                        + lat + "," + lon;

                sendSMS(locationMsg);
            } else {
                sendSMS("Location unavailable");
            }
        });
    }

    private void sendSMS(String message) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "SMS Permission Denied!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SmsManager sms = SmsManager.getDefault();

            if (phone1 != null && !phone1.isEmpty())
                sms.sendTextMessage(phone1, null, message, null, null);

            if (phone2 != null && !phone2.isEmpty())
                sms.sendTextMessage(phone2, null, message, null, null);

            if (phone3 != null && !phone3.isEmpty())
                sms.sendTextMessage(phone3, null, message, null, null);

            Toast.makeText(this, "Message sent to all contacts!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "SMS Failed!", Toast.LENGTH_SHORT).show();
        }
    }
}