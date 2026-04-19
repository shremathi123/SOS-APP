package com.example.sahaya;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class ProfileActivity extends AppCompatActivity {

    TextView name, age, blood, disability, c1, c2, c3;

    DatabaseReference db1, db2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_profile);

            name = findViewById(R.id.name);
            age = findViewById(R.id.age);
            blood = findViewById(R.id.blood);
            disability = findViewById(R.id.disability);

            c1 = findViewById(R.id.c1);
            c2 = findViewById(R.id.c2);
            c3 = findViewById(R.id.c3);

            db1 = FirebaseDatabase.getInstance().getReference("PersonalDetails");
            db2 = FirebaseDatabase.getInstance().getReference("EmergencyContacts");

            // 🔥 PERSONAL DETAILS
            db1.addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        for (DataSnapshot data : snapshot.getChildren()) {

                            String n = data.child("name").getValue(String.class);
                            String a = data.child("age").getValue(String.class);
                            String b = data.child("blood").getValue(String.class);
                            String d = data.child("disability").getValue(String.class);

                            name.setText(n != null ? n : "No Name");
                            age.setText("Age: " + (a != null ? a : "-"));
                            blood.setText("Blood: " + (b != null ? b : "-"));
                            disability.setText("Disability: " + (d != null ? d : "-"));

                            break;
                        }
                    } catch (Exception e) {
                        Toast.makeText(ProfileActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
                    }
                }

                public void onCancelled(DatabaseError error) {}
            });

            // 🔥 CONTACTS
            db2.addListenerForSingleValueEvent(new ValueEventListener() {
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        for (DataSnapshot data : snapshot.getChildren()) {

                            String p1 = data.child("contact1_phone").getValue(String.class);
                            String p2 = data.child("contact2_phone").getValue(String.class);
                            String p3 = data.child("contact3_phone").getValue(String.class);

                            c1.setText("Contact 1: " + (p1 != null ? p1 : "-"));
                            c2.setText("Contact 2: " + (p2 != null ? p2 : "-"));
                            c3.setText("Contact 3: " + (p3 != null ? p3 : "-"));

                            break;
                        }
                    } catch (Exception e) {
                        Toast.makeText(ProfileActivity.this, "Error loading contacts", Toast.LENGTH_SHORT).show();
                    }
                }

                public void onCancelled(DatabaseError error) {}
            });

        } catch (Exception e) {
            Toast.makeText(this, "Profile crashed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}