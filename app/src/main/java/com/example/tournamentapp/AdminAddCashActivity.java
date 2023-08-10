package com.example.tournamentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminAddCashActivity extends AppCompatActivity {

    private EditText etUserEmail, etAmount;
    private Button btnAddCash;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_cash);

        etUserEmail = findViewById(R.id.etUserEmail);
        etAmount = findViewById(R.id.etAmount);
        btnAddCash = findViewById(R.id.btnAddCash);
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        btnAddCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userId = etUserEmail.getText().toString().trim();
                String amountStr = etAmount.getText().toString().trim();
                if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(amountStr)) {
                    Toast.makeText(AdminAddCashActivity.this, "Please enter UserId and Amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                double amount = Double.parseDouble(amountStr);

                usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            double currentBalance = dataSnapshot.child("accountBalance").getValue(Double.class);
                            double newBalance = currentBalance + amount;

                            usersRef.child(userId).child("accountBalance").setValue(newBalance)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(AdminAddCashActivity.this,
                                                        "Cash added successfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(AdminAddCashActivity.this,
                                                        "Failed to add cash", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(AdminAddCashActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(AdminAddCashActivity.this, "Failed to add cash", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }
}