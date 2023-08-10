package com.example.tournamentapp;

public class Match {
    private String matchId;
    private String matchTitle;
    private String matchType;
    private String matchDate;
    private String matchEntry;
    private String matchTime;
    private String matchImage;
    private String matchWin1;
    private String matchWin2;
    private String matchWin3;
    private String matchWin4;
    private String matchWin5;
    private int joinedPlayers; // New field for joined players
    private int totalPlayers;

    // Default constructor (required for Firebase)
    public Match() {
    }

    public Match(String matchId, String matchTitle, String matchType, String matchDate, String matchEntry, String matchTime, String matchImage, String matchWin1, String matchWin2, String matchWin3, String matchWin4, String matchWin5, int joinedPlayers, int totalPlayers) {
        this.matchId = matchId;
        this.matchTitle = matchTitle;
        this.matchType = matchType;
        this.matchDate = matchDate;
        this.matchEntry = matchEntry;
        this.matchTime = matchTime;
        this.matchImage = matchImage;
        this.matchWin1 = matchWin1;
        this.matchWin2 = matchWin2;
        this.matchWin3 = matchWin3;
        this.matchWin4 = matchWin4;
        this.matchWin5 = matchWin5;
        this.joinedPlayers = joinedPlayers;
        this.totalPlayers = totalPlayers;
    }

    // Getters and setters for all fields

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getMatchTitle() {
        return matchTitle;
    }

    public void setMatchTitle(String matchTitle) {
        this.matchTitle = matchTitle;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getMatchEntry() {
        return matchEntry;
    }

    public void setMatchEntry(String matchEntry) {
        this.matchEntry = matchEntry;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public String getMatchImage() {
        return matchImage;
    }

    public void setMatchImage(String matchImage) {
        this.matchImage = matchImage;
    }

    public String getMatchWin1() {
        return matchWin1;
    }

    public void setMatchWin1(String matchWin1) {
        this.matchWin1 = matchWin1;
    }

    public String getMatchWin2() {
        return matchWin2;
    }

    public void setMatchWin2(String matchWin2) {
        this.matchWin2 = matchWin2;
    }

    public String getMatchWin3() {
        return matchWin3;
    }

    public void setMatchWin3(String matchWin3) {
        this.matchWin3 = matchWin3;
    }

    public String getMatchWin4() {
        return matchWin4;
    }

    public void setMatchWin4(String matchWin4) {
        this.matchWin4 = matchWin4;
    }

    public String getMatchWin5() {
        return matchWin5;
    }

    public void setMatchWin5(String matchWin5) {
        this.matchWin5 = matchWin5;
    }
    public int getJoinedPlayers() {
        return joinedPlayers;
    }

    public void setJoinedPlayers(int joinedPlayers) {
        this.joinedPlayers = joinedPlayers;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }
}

