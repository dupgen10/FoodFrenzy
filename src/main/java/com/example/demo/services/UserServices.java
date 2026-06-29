package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserServices {

    private static final Logger log = LoggerFactory.getLogger(UserServices.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }

    /**
     * Returns user by ID, or throws EntityNotFoundException.
     * Previously called Optional.get() without checking isPresent().
     */
    public User getUser(int id) {
        Optional<User> optional = this.userRepository.findById(id);
        return optional.orElseThrow(() ->
                new EntityNotFoundException("User not found with id: " + id));
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findUserByUemail(email);
    }

    public void updateUser(User user, int id) {
        User existing = getUser(id);
        existing.setUname(user.getUname());
        existing.setUemail(user.getUemail());
        existing.setUnumber(user.getUnumber());
        // Only update password if a new one is provided
        if (user.getUpassword() != null && !user.getUpassword().isBlank()) {
            existing.setUpassword(passwordEncoder.encode(user.getUpassword()));
        }
        this.userRepository.save(existing);
        log.info("Updated user with id: {}", id);
    }

    public void deleteUser(int id) {
        this.userRepository.deleteById(id);
        log.info("Deleted user with id: {}", id);
    }

    /**
     * Saves a new user with BCrypt-hashed password.
     */
    public void addUser(User user) {
        user.setUpassword(passwordEncoder.encode(user.getUpassword()));
        this.userRepository.save(user);
        log.info("Registered new user: {}", user.getUemail());
    }

    /**
     * Validates user login credentials using BCrypt.
     *
     * Previously: loaded ALL users and iterated (O(n) full table scan).
     * Now: queries by email (O(1) indexed lookup), then BCrypt checks password.
     */
    public boolean validateLoginCredentials(String email, String password) {
        User user = this.userRepository.findUserByUemail(email);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(password, user.getUpassword());
    }
}