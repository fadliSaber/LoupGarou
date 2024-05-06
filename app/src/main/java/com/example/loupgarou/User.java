package com.example.loupgarou;

public class User {
    private String id; // ID obtained from Firebase Authentication
    private String state;
    private String role;

    // Constructors, getters, and setters
    // ...

    public User() {
        // Default constructor required for Firebase serialization
    }

    public User(String id, String state, String role) {
        this.id = id;
        this.state = state;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

