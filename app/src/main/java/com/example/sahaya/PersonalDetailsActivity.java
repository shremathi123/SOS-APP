package com.example.sahaya;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PersonalDetailsActivity extends AppCompatActivity {

    EditText name, age, blood, other;
    CheckBox blind, deaf, dumb, paralysis, autism;
    Button saveBtn, backBtn;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        // Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("PersonalDetails");

        // UI
        name = findViewById(R.id.pName);
        age = findViewById(R.id.pAge);
        blood = findViewById(R.id.pBlood);
        other = findViewById(R.id.other);

        blind = findViewById(R.id.blind);
        deaf = findViewById(R.id.deaf);
        dumb = findViewById(R.id.dumb);
        paralysis = findViewById(R.id.paralysis);
        autism = findViewById(R.id.autism);

        saveBtn = findViewById(R.id.saveBtn);
        backBtn = findViewById(R.id.backBtn);

        // 🔙 BACK BUTTON
        backBtn.setOnClickListener(v -> finish());

        // 💾 SAVE BUTTON
        saveBtn.setOnClickListener(v -> {

            String n = name.getText().toString().trim();
            String a = age.getText().toString().trim();
            String b = blood.getText().toString().trim();
            String o = other.getText().toString().trim();

            // ✅ VALIDATION
            if (n.isEmpty()) {
                name.setError("Enter name");
                return;
            }

            if (a.isEmpty()) {
                age.setError("Enter age");
                return;
            }

            if (b.isEmpty()) {
                blood.setError("Enter blood group");
                return;
            }

            // 🧠 DISABILITY COMBINATION
            String disability = "";

            if (blind.isChecked()) disability += "Blind, ";
            if (deaf.isChecked()) disability += "Deaf, ";
            if (dumb.isChecked()) disability += "Dumb, ";
            if (paralysis.isChecked()) disability += "Paralysis, ";
            if (autism.isChecked()) disability += "Autism, ";

            if (!o.isEmpty()) {
                disability += o;
            }

            // 🔥 SAVE TO FIREBASE
            String id = databaseReference.push().getKey();

            databaseReference.child(id).child("name").setValue(n);
            databaseReference.child(id).child("age").setValue(a);
            databaseReference.child(id).child("blood").setValue(b);
            databaseReference.child(id).child("disability").setValue(disability);

            // ✅ SUCCESS MESSAGE
            Toast.makeText(this, "Details Saved!", Toast.LENGTH_SHORT).show();

            // 🚀 MOVE TO NEXT SCREEN (IMPORTANT)
            Intent intent = new Intent(PersonalDetailsActivity.this, EmergencyContactActivity.class);
            startActivity(intent);

        });
    }
}