package com.example.capstoneproject.api.models;

public class LoginResponse {
    private String message;
    private UserData user;
    private String status; // Optional, in case it exists
    private Boolean success; // Optional, in case it exists

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    // Helper method to determine if login was successful
    public boolean isLoginSuccessful() {
        // Check if message indicates success
        if (message != null && message.toLowerCase().contains("success")) {
            return true;
        }
        // Check success field if exists
        if (success != null && success) {
            return true;
        }
        // Check status field if exists
        if (status != null && status.equalsIgnoreCase("success")) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "message='" + message + '\'' +
                ", user=" + (user != null ? user.toString() : "null") +
                ", status='" + status + '\'' +
                ", success=" + success +
                '}';
    }

    public static class UserData {
        private Integer user; // Changed from String to Integer
        private String full_name;
        private String role;
        private String email; // Might not be in response, but we'll handle it

        // Getters and setters
        public Integer getUser() {
            return user;
        }

        public void setUser(Integer user) {
            this.user = user;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String toString() {
            return "UserData{" +
                    "user=" + user +
                    ", full_name='" + full_name + '\'' +
                    ", role='" + role + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }
}