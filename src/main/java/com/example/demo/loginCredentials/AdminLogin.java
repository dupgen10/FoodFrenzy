package com.example.demo.loginCredentials;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AdminLogin {

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        // Password intentionally excluded to prevent leaking in logs
        return "AdminLogin [email=" + email + "]";
    }
}