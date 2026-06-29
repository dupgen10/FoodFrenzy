package com.example.demo.loginCredentials;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserLogin {

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    private String userEmail;

    @NotBlank(message = "Password is required")
    private String userPassword;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        // Password intentionally excluded to prevent leaking in logs
        return "UserLogin [userEmail=" + userEmail + "]";
    }
}