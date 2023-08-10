package com.example.tournamentapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {
    private List<Match> matchList;


    public MatchAdapter(List<Match> matchList) {
        this.matchList = matchList;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = matchList.get(position);
        String imurl;
        imurl= match.getMatchImage();
        String matchId = match.getMatchId();

        holder.tvMatchTitle.setText(match.getMatchTitle());
        holder.tvMatchType.setText(match.getMatchType());
        holder.tvMatchDate.setText(match.getMatchDate());
        holder.tvMatchEntry.setText(match.getMatchEntry());
        holder.tvMatchTime.setText(match.getMatchTime());
        Picasso.get().load(imurl).into(holder.tvMatchImage);
        holder.tvMatchWin1.setText(match.getMatchWin1());
        holder.tvMatchWin2.setText(match.getMatchWin2());
        holder.tvMatchWin3.setText(match.getMatchWin3());
        holder.tvMatchWin4.setText(match.getMatchWin4());
        holder.tvMatchWin5.setText(match.getMatchWin5());

        int joinedPlayers = match.getJoinedPlayers();
        int totalPlayers = match.getTotalPlayers();

        holder.progressBarJoinedPlayers.setMax(totalPlayers);
        holder.progressBarJoinedPlayers.setProgress(joinedPlayers);
        holder.tvJoinedPlayersCount.setText(String.valueOf(joinedPlayers));
        holder.tvTotalPlayersCount.setText(String.valueOf(totalPlayers)); // Set the maximum value of the ProgressBar

        DatabaseReference matchRef = FirebaseDatabase.getInstance().getReference("matches").child(matchId);
        DatabaseReference matchPlayersRef = matchRef.child("match_players");


        // Check if the user has already joined this match
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            matchPlayersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        DatabaseReference roomIdRef = matchRef.child("room_idPass");
                        roomIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String roomId = snapshot.child("room_Id").getValue(String.class);
                                String roomPass = snapshot.child("room_Pass").getValue(String.class);
                                holder.tvMatchRoomId.setText(roomId);
                                holder.tvMatchRoomPass.setText(roomPass);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        holder.btnJoinMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event for the "Join Match" button
                // You can implement the desired logic here

                joinMatch(holder.getAdapterPosition(), holder);
            }
        });
    }

    private void joinMatch(int position, MatchViewHolder holder) {
        Match match = matchList.get(position);
        String matchId = match.getMatchId();
        AtomicInteger joinedPlayers = new AtomicInteger(match.getJoinedPlayers());

        // Check if the maximum number of players has been reached
        if (joinedPlayers.get() < match.getTotalPlayers()  ) {
            DatabaseReference matchRef = FirebaseDatabase.getInstance().getReference("matches").child(matchId);
            DatabaseReference matchEntryRef = matchRef.child("matchEntry");
            DatabaseReference matchPlayersRef = matchRef.child("match_players");

            // Check if the user has already joined this match
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();

                matchPlayersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(holder.itemView.getContext(), "You have already joined this match", Toast.LENGTH_SHORT).show();

                        } else {


                            matchEntryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String matchEntryStr =snapshot.getValue(String.class);

                                    // Convert the match entry string to an integer
                                    double matchEntry = Double.parseDouble(matchEntryStr);
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                User user = snapshot.getValue(User.class);
                                                if (user != null) {

                                                    double currentBalance = snapshot.child("accountBalance").getValue(Double.class);

                                                    if (currentBalance<matchEntry){
                                                        Toast.makeText(holder.itemView.getContext(), "Account Balance is Low", Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        ProgressDialog dialog;
                                                        dialog=new ProgressDialog(holder.itemView.getContext());
                                                        dialog.setMessage("Loading");
                                                        dialog.show();
                                                        double balance = currentBalance-matchEntry;
                                                        userRef.child("accountBalance").setValue(balance);
                                                        // Increment the joined players count
                                                        int newJoinedPlayers = joinedPlayers.incrementAndGet();
                                                        match.setJoinedPlayers(newJoinedPlayers);

                                                        // Update the progress bar and joined players count
                                                        holder.progressBarJoinedPlayers.setProgress(newJoinedPlayers);
                                                        holder.tvJoinedPlayersCount.setText(String.valueOf(newJoinedPlayers));

                                                        // Update the match data in the Firebase Realtime Database
                                                        matchRef.child("joinedPlayers").setValue(newJoinedPlayers)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        // Store the user ID under the match_players node of the specific match
                                                                        matchPlayersRef.child(userId).setValue(true);

                                                                        Toast.makeText(holder.itemView.getContext(), "Joined match successfully", Toast.LENGTH_SHORT).show();
                                                                        dialog.dismiss();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(holder.itemView.getContext(), "Failed to join match", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }





                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(holder.itemView.getContext(), "Failed to join match", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(holder.itemView.getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(holder.itemView.getContext(), "Maximum players reached", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView tvMatchRoomId, tvMatchRoomPass, tvMatchTitle, tvMatchType, tvMatchDate, tvMatchEntry, tvMatchTime, tvMatchWin1, tvMatchWin2, tvMatchWin3, tvMatchWin4, tvMatchWin5;
        Button btnJoinMatch;
        ImageView tvMatchImage;
        ProgressBar progressBarJoinedPlayers;
        TextView tvJoinedPlayersCount, tvTotalPlayersCount;

        public MatchViewHolder(View itemView) {
            super(itemView);
            tvMatchTitle = itemView.findViewById(R.id.tvMatchTitle);
            tvMatchType = itemView.findViewById(R.id.tvMatchType);
            tvMatchDate = itemView.findViewById(R.id.tvMatchDate);
            tvMatchEntry = itemView.findViewById(R.id.tvMatchEntry);
            tvMatchTime = itemView.findViewById(R.id.tvMatchTime);
            tvMatchImage = itemView.findViewById(R.id.tvMatchImage);
            tvMatchWin1 = itemView.findViewById(R.id.tvMatchWin1);
            tvMatchWin2 = itemView.findViewById(R.id.tvMatchWin2);
            tvMatchWin3 = itemView.findViewById(R.id.tvMatchWin3);
            tvMatchWin4 = itemView.findViewById(R.id.tvMatchWin4);
            tvMatchWin5 = itemView.findViewById(R.id.tvMatchWin5);
            btnJoinMatch = itemView.findViewById(R.id.btnJoinMatch);
            progressBarJoinedPlayers = itemView.findViewById(R.id.progressBarJoinedPlayers);
            tvJoinedPlayersCount = itemView.findViewById(R.id.tvJoinedPlayersCount);
            tvTotalPlayersCount = itemView.findViewById(R.id.tvTotalPlayersCount);
            tvMatchRoomId = itemView.findViewById(R.id.tvMatchRoomId);
            tvMatchRoomPass = itemView.findViewById(R.id.tvMatchRoomPass);

        }
    }
}