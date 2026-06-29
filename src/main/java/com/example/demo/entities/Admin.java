package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminId;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100)
    private String adminName;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    private String adminEmail;

    @NotBlank(message = "Password is required")
    private String adminPassword;

    private String adminNumber;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminNumber() {
        return adminNumber;
    }

    public void setAdminNumber(String adminNumber) {
        this.adminNumber = adminNumber;
    }

    @Override
    public String toString() {
        // Password intentionally excluded to prevent leaking in logs
        return "Admin [adminId=" + adminId + ", adminName=" + adminName
                + ", adminEmail=" + adminEmail + ", adminNumber=" + adminNumber + "]";
    }
}