package com.example.localmarketbagroot;

public class SellerDB {
    private String firstName;
    private String lastName;
    private String email;
    //define seller table in database

    public SellerDB() {
        // Default constructor required for calls to DataSnapshot.getValue(SellerDB.class)
    }

    public SellerDB(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

