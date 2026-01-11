package com.example.capstoneproject.api.models;


public class AutomationRequest {
    private String date;     // "YYYY-MM-DD"
    private String time;     // "HH:MM"
    private int duration;    // in minutes
    private boolean enabled; // optional, for auto on/off

    // Getters
    public String getDate() { return date; }
    public String getTime() { return time; }
    public int getDuration() { return duration; }
    public boolean isEnabled() { return enabled; }

    // Setters
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
