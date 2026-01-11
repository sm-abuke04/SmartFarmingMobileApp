package com.example.capstoneproject.dashboard.models;

public class Model {
    private int id;
    private String name;

    public Model(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() { return name; } // important for Spinner display
}
