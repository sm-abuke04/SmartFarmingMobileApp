package com.example.capstoneproject.api.models;

public class User {
    private String id;
    private String name;
    private String email;
    private String token;

    // Constructor
    public User(String id, String name, String email, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.token = token;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
