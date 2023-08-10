package com.example.tournamentapp;

import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HandleMatchAdapter extends RecyclerView.Adapter<HandleMatchAdapter.MatchViewHolder> {

    private List<Match> matchList;
    private OnMatchClickListener matchClickListener;

    public HandleMatchAdapter(List<Match> matchList, OnMatchClickListener matchClickListener) {
        this.matchList = matchList;
        this.matchClickListener = matchClickListener;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_handle_match, parent, false);
        return new MatchViewHolder(itemView, matchClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = matchList.get(position);
        String imurl;
        imurl= match.getMatchImage();

        String matchId = match.getMatchId();
        DatabaseReference matchRef = FirebaseDatabase.getInstance().getReference("matches").child(matchId);
        DatabaseReference roomIdRef = matchRef.child("room_idPass");
        roomIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String roomId = snapshot.child("room_Id").getValue(String.class);
                String roomPass = snapshot.child("room_Pass").getValue(String.class);
                holder.tvHandleMatchRoomId.setText(roomId);
                holder.tvHandleMatchRoomPass.setText(roomPass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.tvHandleMatchTitle.setText(match.getMatchTitle());
        holder.tvHandleMatchType.setText(match.getMatchType());
        holder.tvHandleMatchDate.setText(match.getMatchDate());
        holder.tvHandleMatchEntry.setText(match.getMatchEntry());
        holder.tvHandleMatchTime.setText(match.getMatchTime());
        holder.tvHandleMatchWin1.setText(match.getMatchWin1());
        holder.tvHandleMatchWin2.setText(match.getMatchWin2());
        holder.tvHandleMatchWin3.setText(match.getMatchWin3());
        holder.tvHandleMatchWin4.setText(match.getMatchWin4());
        holder.tvHandleMatchWin5.setText(match.getMatchWin5());
        Picasso.get().load(imurl).into(holder.tvHandleMatchImage);

        int joinedPlayers = match.getJoinedPlayers();
        int totalPlayers = match.getTotalPlayers();

        holder.progressBarHandleJoinedPlayers.setMax(totalPlayers);
        holder.progressBarHandleJoinedPlayers.setProgress(joinedPlayers);
        holder.tvHandleJoinedPlayersCount.setText(String.valueOf(joinedPlayers));
        holder.tvHandleTotalPlayersCount.setText(String.valueOf(totalPlayers));
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvHandleMatchTitle, tvHandleMatchType, tvHandleMatchDate, tvHandleMatchEntry, tvHandleMatchTime;
        TextView tvHandleMatchRoomId,tvHandleMatchRoomPass, tvHandleMatchWin1, tvHandleMatchWin2, tvHandleMatchWin3, tvHandleMatchWin4, tvHandleMatchWin5;
        ProgressBar progressBarHandleJoinedPlayers;
        TextView tvHandleJoinedPlayersCount, tvHandleTotalPlayersCount;
        Button btnDelete, btnUpdate;
        OnMatchClickListener matchClickListener;
        ImageView tvHandleMatchImage;

        public MatchViewHolder(View itemView, OnMatchClickListener matchClickListener) {
            super(itemView);
            tvHandleMatchTitle = itemView.findViewById(R.id.tvHandleMatchTitle);
            tvHandleMatchType = itemView.findViewById(R.id.tvHandleMatchType);
            tvHandleMatchDate = itemView.findViewById(R.id.tvHandleMatchDate);
            tvHandleMatchEntry = itemView.findViewById(R.id.tvHandleMatchEntry);
            tvHandleMatchTime = itemView.findViewById(R.id.tvHandleMatchTime);
            tvHandleMatchWin1 = itemView.findViewById(R.id.tvHandleMatchWin1);
            tvHandleMatchWin2 = itemView.findViewById(R.id.tvHandleMatchWin2);
            tvHandleMatchWin3 = itemView.findViewById(R.id.tvHandleMatchWin3);
            tvHandleMatchWin4 = itemView.findViewById(R.id.tvHandleMatchWin4);
            tvHandleMatchWin5 = itemView.findViewById(R.id.tvHandleMatchWin5);
            tvHandleMatchImage = itemView.findViewById(R.id.tvHandleMatchImage);
            progressBarHandleJoinedPlayers = itemView.findViewById(R.id.progressBarHandleJoinedPlayers);
            tvHandleJoinedPlayersCount = itemView.findViewById(R.id.tvHandleJoinedPlayersCount);
            tvHandleTotalPlayersCount = itemView.findViewById(R.id.tvHandleTotalPlayersCount);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            tvHandleMatchRoomId = itemView.findViewById(R.id.tvHandleMatchRoomId);
            tvHandleMatchRoomPass = itemView.findViewById(R.id.tvHandleMatchRoomPass);

            this.matchClickListener = matchClickListener;

            btnDelete.setOnClickListener(this);
            btnUpdate.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION) {
                switch (v.getId()) {
                    case R.id.btnDelete:

                        matchClickListener.onDeleteClick(position);
                        break;
                    case R.id.btnUpdate:
                        matchClickListener.onUpdateClick(position);
                        break;
                }
            }
        }
    }

    public interface OnMatchClickListener {
        void onDeleteClick(int position);
        void onUpdateClick(int position);
    }
}


