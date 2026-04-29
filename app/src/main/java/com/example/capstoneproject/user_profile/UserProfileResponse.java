package com.example.capstoneproject.user_profile;

import com.google.gson.annotations.SerializedName;

public class UserProfileResponse {

    @SerializedName("user_id")
    private int userId;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("middle_name")
    private String middleName;

    @SerializedName("extension_name")
    private String extensionName;

    @SerializedName("email")
    private String email;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("gender")
    private String gender;

    @SerializedName("status")
    private String status;

    @SerializedName("role")
    private String role;

    @SerializedName("user_image")
    private String userImage;

    @SerializedName("is_verified")
    private boolean isVerified;

    // Getters
    public int getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getMiddleName() { return middleName; }
    public String getExtensionName() { return extensionName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getGender() { return gender; }
    public String getStatus() { return status; }
    public String getRole() { return role; }
    public String getUserImage() { return userImage; }
    public boolean isVerified() { return isVerified; }
}