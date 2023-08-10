package com.example.tournamentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadMatchesActivity extends AppCompatActivity {
    private EditText etMatchTitle, etMatchType, etMatchDate, etMatchEntry, etMatchTime, etMatchImage;
    private EditText etMatchWin1, etMatchWin2, etMatchWin3, etMatchWin4, etMatchWin5;
    private EditText etMaxPlayers;
    private Button btnUploadMatch, btnHandleMatch, btnAddCashHandle;

    private DatabaseReference matchesRef;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_matches);
        dialog=new ProgressDialog(UploadMatchesActivity.this);
        dialog.setMessage("Loading");


        // Initialize Firebase Realtime Database reference
        matchesRef = FirebaseDatabase.getInstance().getReference("matches");

        // Bind layout views
        etMatchTitle = findViewById(R.id.etMatchTitle);
        etMatchType = findViewById(R.id.etMatchType);
        etMatchDate = findViewById(R.id.etMatchDate);
        etMatchEntry = findViewById(R.id.etMatchEntry);
        etMatchTime = findViewById(R.id.etMatchTime);
        etMatchImage = findViewById(R.id.etMatchImage);
        etMatchWin1 = findViewById(R.id.etMatchWin1);
        etMatchWin2 = findViewById(R.id.etMatchWin2);
        etMatchWin3 = findViewById(R.id.etMatchWin3);
        etMatchWin4 = findViewById(R.id.etMatchWin4);
        etMatchWin5 = findViewById(R.id.etMatchWin5);
        etMaxPlayers = findViewById(R.id.etMaxPlayers);
        btnUploadMatch = findViewById(R.id.btnUploadMatch);
        btnHandleMatch = findViewById(R.id.btnHandleMatch);
        btnAddCashHandle = findViewById(R.id.btnAddCashHandle);

        btnAddCashHandle. setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadMatchesActivity.this, AdminAddCashActivity.class);
                startActivity(intent);
            }
        });

        btnHandleMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadMatchesActivity.this, HandleMatchesActivity.class);
                startActivity(intent);
            }
        });

        btnUploadMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMatch();
            }
        });
    }

    private void uploadMatch() {
        dialog.show();
        String matchTitle = etMatchTitle.getText().toString().trim();
        String matchType = etMatchType.getText().toString().trim();
        String matchDate = etMatchDate.getText().toString().trim();
        String matchEntry = etMatchEntry.getText().toString().trim();
        String matchTime = etMatchTime.getText().toString().trim();
        String matchImage = etMatchImage.getText().toString().trim();
        String matchWin1 = etMatchWin1.getText().toString().trim();
        String matchWin2 = etMatchWin2.getText().toString().trim();
        String matchWin3 = etMatchWin3.getText().toString().trim();
        String matchWin4 = etMatchWin4.getText().toString().trim();
        String matchWin5 = etMatchWin5.getText().toString().trim();
        int joinedPlayers = 0; // Initialize joined players to 0
        int totalPlayers = Integer.parseInt(etMaxPlayers.getText().toString()); // Get the maximum number of players

        // Generate a unique key for the match entry
        String matchId = matchesRef.push().getKey();
        // Create a new Match object
        Match match = new Match(matchId,matchTitle, matchType, matchDate, matchEntry, matchTime,
                matchImage, matchWin1, matchWin2, matchWin3, matchWin4, matchWin5,
                joinedPlayers, totalPlayers);



        // Save the match object to the Firebase Realtime Database
        matchesRef.child(matchId).setValue(match)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UploadMatchesActivity.this, "Match uploaded successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadMatchesActivity.this, "Failed to upload match", Toast.LENGTH_SHORT).show();
                    }
                });
        dialog.dismiss();
    }
}
