package com.example.tournamentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateMatchActivity extends AppCompatActivity {

    private EditText etMatchTitle, etMatchType, etMatchDate, etMatchEntry, etMatchTime,
            etMatchWin1, etMatchWin2, etMatchWin3, etMatchWin4, etMatchWin5, etMatchImageLink, etJoinedPlayers, etTotalPlayers,etRoomId,etRoomPass;
    private Button btnUpdateMatch, btnRoomIdPass;
    private String matchId;


    private DatabaseReference matchRef,roomIdPassRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_match);

        // Get the match ID from the intent
        matchId = getIntent().getStringExtra("matchId");

        // Initialize database reference
        matchRef = FirebaseDatabase.getInstance().getReference("matches").child(matchId);
roomIdPassRef = matchRef.child("room_idPass");

        // Initialize views
        etMatchTitle = findViewById(R.id.etUpdateMatchTitle);
        etMatchType = findViewById(R.id.etUpdateMatchType);
        etMatchDate = findViewById(R.id.etUpdateMatchDate);
        etMatchEntry = findViewById(R.id.etUpdateMatchEntry);
        etMatchTime = findViewById(R.id.etUpdateMatchTime);
        etMatchWin1 = findViewById(R.id.etUpdateMatchWin1);
        etMatchWin2 = findViewById(R.id.etUpdateMatchWin2);
        etMatchWin3 = findViewById(R.id.etUpdateMatchWin3);
        etMatchWin4 = findViewById(R.id.etUpdateMatchWin4);
        etMatchWin5 = findViewById(R.id.etUpdateMatchWin5);
        etMatchImageLink = findViewById(R.id.etUpdateMatchImageLink);
        etJoinedPlayers = findViewById(R.id.etUpdateJoinedPlayers);
        etTotalPlayers = findViewById(R.id.etUpdateTotalPlayers);
        btnUpdateMatch = findViewById(R.id.btnUpdateMatch);
        btnRoomIdPass=findViewById(R.id.btnIdPass);
        etRoomId= findViewById(R.id.etUpdateRoomId);
        etRoomPass= findViewById(R.id.etUpdateRoomPass);





        btnRoomIdPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provideRoomIdPass();
            }
        });
        // Set click listener for the update button
        btnUpdateMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMatch();
            }
        });

        // Retrieve the match data from Firebase and populate the EditText fields
        retrieveMatchData();
    }



    private void retrieveMatchData() {
        // Retrieve the match data from Firebase using the matchId
        matchRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Match match = dataSnapshot.getValue(Match.class);
                    if (match != null) {
                        // Set the retrieved data to the EditText fields
                        etMatchTitle.setText(match.getMatchTitle());
                        etMatchType.setText(match.getMatchType());
                        etMatchDate.setText(match.getMatchDate());
                        etMatchEntry.setText(match.getMatchEntry());
                        etMatchTime.setText(match.getMatchTime());
                        etMatchWin1.setText(match.getMatchWin1());
                        etMatchWin2.setText(match.getMatchWin2());
                        etMatchWin3.setText(match.getMatchWin3());
                        etMatchWin4.setText(match.getMatchWin4());
                        etMatchWin5.setText(match.getMatchWin5());
                        etMatchImageLink.setText(match.getMatchImage());
                        etJoinedPlayers.setText(String.valueOf(match.getJoinedPlayers()));
                        etTotalPlayers.setText(String.valueOf(match.getTotalPlayers()));
                    }
                } else {
                    Toast.makeText(UpdateMatchActivity.this, "Match data does not exist", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateMatchActivity.this, "Failed to retrieve match data", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateMatch() {
        // Get the updated data from the EditText fields
        String matchTitle = etMatchTitle.getText().toString().trim();
        String matchType = etMatchType.getText().toString().trim();
        String matchDate = etMatchDate.getText().toString().trim();
        String matchEntry = etMatchEntry.getText().toString().trim();
        String matchTime = etMatchTime.getText().toString().trim();
        String matchWin1 = etMatchWin1.getText().toString().trim();
        String matchWin2 = etMatchWin2.getText().toString().trim();
        String matchWin3 = etMatchWin3.getText().toString().trim();
        String matchWin4 = etMatchWin4.getText().toString().trim();
        String matchWin5 = etMatchWin5.getText().toString().trim();
        String imageLink = etMatchImageLink.getText().toString().trim();
        int joinedPlayers = Integer.parseInt(etJoinedPlayers.getText().toString());
        int totalPlayers = Integer.parseInt(etTotalPlayers.getText().toString());


        // Update the match data in Firebase
        matchRef.child("joinedPlayers").setValue(joinedPlayers);
        matchRef.child("matchDate").setValue(matchDate);
        matchRef.child("matchEntry").setValue(matchEntry);
        matchRef.child("matchId").setValue(matchId);
        matchRef.child("matchImage").setValue(imageLink);
        matchRef.child("matchTime").setValue(matchTime);
        matchRef.child("matchTitle").setValue(matchTitle);
        matchRef.child("matchType").setValue(matchType);
        matchRef.child("matchWin1").setValue(matchWin1);
        matchRef.child("matchWin2").setValue(matchWin2);
        matchRef.child("matchWin3").setValue(matchWin3);
        matchRef.child("matchWin4").setValue(matchWin4);
        matchRef.child("matchWin5").setValue(matchWin5);
        matchRef.child("totalPlayers").setValue(totalPlayers);

        if (joinedPlayers == 0){
            matchRef.child("match_players").removeValue();
        }

        Toast.makeText(this, "Match Update Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void provideRoomIdPass() {
String roomID = etRoomId.getText().toString().trim();
String roomPass = etRoomPass.getText().toString().trim();

roomIdPassRef.child("room_Id").setValue(roomID);
roomIdPassRef.child("room_Pass").setValue(roomPass);
        Toast.makeText(this, "ID Password Uploaded", Toast.LENGTH_SHORT).show();
finish();
    }
}
