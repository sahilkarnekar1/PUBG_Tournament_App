package com.example.tournamentapp;

public class User {
    private String id;
    private String username;
    private String email;
    private double accountBalance;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String username, String email, double accountBalance) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.accountBalance = accountBalance;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
}
