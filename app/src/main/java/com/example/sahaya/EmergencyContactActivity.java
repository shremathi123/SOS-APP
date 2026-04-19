package com.example.sahaya;

import android.os.Bundle;
import android.content.Intent;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmergencyContactActivity extends AppCompatActivity {

    EditText name1, phone1, name2, phone2, name3, phone3, helpline;
    Button saveBtn;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);

        // 🔗 Connect UI
        name1 = findViewById(R.id.name1);
        phone1 = findViewById(R.id.phone1);
        name2 = findViewById(R.id.name2);
        phone2 = findViewById(R.id.phone2);
        name3 = findViewById(R.id.name3);
        phone3 = findViewById(R.id.phone3);
        helpline = findViewById(R.id.helpline);
        saveBtn = findViewById(R.id.saveBtn);

        // 🔥 Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("EmergencyContacts");

        // 💾 Save Button
        saveBtn.setOnClickListener(v -> {

            String n1 = name1.getText().toString().trim();
            String p1 = phone1.getText().toString().trim();
            String n2 = name2.getText().toString().trim();
            String p2 = phone2.getText().toString().trim();
            String n3 = name3.getText().toString().trim();
            String p3 = phone3.getText().toString().trim();
            String help = helpline.getText().toString().trim();

            // ✅ Validation
            if (p1.isEmpty()) {
                phone1.setError("Enter at least one contact");
                return;
            }

            // 🔥 Generate ID
            String id = databaseReference.push().getKey();

            // 🔥 Save data
            databaseReference.child(id).child("contact1_name").setValue(n1);
            databaseReference.child(id).child("contact1_phone").setValue(p1);

            databaseReference.child(id).child("contact2_name").setValue(n2);
            databaseReference.child(id).child("contact2_phone").setValue(p2);

            databaseReference.child(id).child("contact3_name").setValue(n3);
            databaseReference.child(id).child("contact3_phone").setValue(p3);

            databaseReference.child(id).child("helpline").setValue(help);

            // ✅ Success
            Toast.makeText(this, "Contacts Saved!", Toast.LENGTH_SHORT).show();

            // 🚀 FIXED NAVIGATION (IMPORTANT)
            Intent intent = new Intent(EmergencyContactActivity.this, SOSActivity.class);

            // 🔥 CLEAR BACK STACK (NO LOOP BUG)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        });
    }
}