package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Admin;
import com.example.demo.repositories.AdminRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AdminServices {

    private static final Logger log = LoggerFactory.getLogger(AdminServices.class);

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Admin> getAll() {
        return this.adminRepository.findAll();
    }

    /**
     * Returns the admin by ID, or throws EntityNotFoundException if not found.
     * Previously threw NoSuchElementException — now gives a meaningful error.
     */
    public Admin getAdmin(int id) {
        Optional<Admin> optional = this.adminRepository.findById(id);
        return optional.orElseThrow(() ->
                new EntityNotFoundException("Admin not found with id: " + id));
    }

    /**
     * Updates admin fields by ID. Uses direct findById instead of O(n) iteration.
     */
    public void update(Admin updatedAdmin, int id) {
        Admin existing = getAdmin(id); // throws EntityNotFoundException if missing
        existing.setAdminName(updatedAdmin.getAdminName());
        existing.setAdminEmail(updatedAdmin.getAdminEmail());
        existing.setAdminNumber(updatedAdmin.getAdminNumber());
        // Only update password if a new one is provided
        if (updatedAdmin.getAdminPassword() != null && !updatedAdmin.getAdminPassword().isBlank()) {
            existing.setAdminPassword(passwordEncoder.encode(updatedAdmin.getAdminPassword()));
        }
        this.adminRepository.save(existing);
        log.info("Updated admin with id: {}", id);
    }

    public void delete(int id) {
        this.adminRepository.deleteById(id);
        log.info("Deleted admin with id: {}", id);
    }

    /**
     * Saves a new admin with BCrypt-hashed password.
     */
    public void addAdmin(Admin admin) {
        admin.setAdminPassword(passwordEncoder.encode(admin.getAdminPassword()));
        this.adminRepository.save(admin);
        log.info("Added new admin: {}", admin.getAdminEmail());
    }

    /**
     * Validates admin credentials using BCrypt.
     * Previously compared passwords in plaintext — now uses secure hashing.
     */
    public boolean validateAdminCredentials(String email, String password) {
        Admin admin = adminRepository.findByAdminEmail(email);
        if (admin == null) {
            return false;
        }
        return passwordEncoder.matches(password, admin.getAdminPassword());
    }
}