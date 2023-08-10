package com.example.tournamentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HandleMatchesActivity extends AppCompatActivity implements HandleMatchAdapter.OnMatchClickListener {

    private RecyclerView recyclerView;
    private List<Match> matchList;
    private DatabaseReference matchesRef;
    private HandleMatchAdapter handleMatchAdapter;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_matches);

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerHandleMatches);
        dialog=new ProgressDialog(HandleMatchesActivity.this);
        dialog.setMessage("Loading");
        dialog.show();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        matchList = new ArrayList<>();
        handleMatchAdapter = new HandleMatchAdapter(matchList, this);
        recyclerView.setAdapter(handleMatchAdapter);

        // Get reference to the "matches" node in the Firebase database
        matchesRef = FirebaseDatabase.getInstance().getReference("matches");

        // Retrieve the list of matches from the database
        retrieveMatches();

        dialog.dismiss();
    }

    private void retrieveMatches() {
        matchesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                matchList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Match match = snapshot.getValue(Match.class);
                    matchList.add(match);
                }

                handleMatchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HandleMatchesActivity.this, "Failed to retrieve matches.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleteClick(int position) {
        dialog.show();
        Match match = matchList.get(position);

        // Delete the match from the database
        matchesRef.child(match.getMatchId()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(HandleMatchesActivity.this, "Match deleted successfully.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HandleMatchesActivity.this, "Failed to delete match.", Toast.LENGTH_SHORT).show();
                   dialog.dismiss();
                    }
                });
    }

    @Override
    public void onUpdateClick(int position) {
        Match match = matchList.get(position);

        // Open the UpdateMatchActivity and pass the match ID
        Intent intent = new Intent(HandleMatchesActivity.this, UpdateMatchActivity.class);
        intent.putExtra("matchId", match.getMatchId());
        startActivity(intent);
    }
}
