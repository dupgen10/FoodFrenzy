package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entities.User;
import com.example.demo.services.UserServices;

@Controller
public class UserController {

    @Autowired
    private UserServices services;

    // ========== PUBLIC REGISTRATION ==========

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("userRegistration", new User());
        return "register";
    }

    /**
     * Handles user self-registration.
     * Password is hashed by UserServices.addUser() — never stored in plaintext.
     * The u_id field is NOT accepted from the form — it's assigned by the database.
     */
    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("userRegistration") User user) {
        // Explicitly clear any u_id submitted from the form (security: users cannot set their own ID)
        user.setU_id(null);
        this.services.addUser(user);
        return "redirect:/login";
    }

    // ========== ADMIN USER MANAGEMENT ==========

    @PostMapping("/addingUser")
    public String addUser(@ModelAttribute User user) {
        user.setU_id(null); // ID always assigned by DB
        this.services.addUser(user);
        return "redirect:/admin/services";
    }

    /**
     * Fixed: was @GetMapping — state-mutating operations must be POST.
     */
    @PostMapping("/updatingUser/{id}")
    public String updateUser(@ModelAttribute User user, @PathVariable("id") int id) {
        this.services.updateUser(user, id);
        return "redirect:/admin/services";
    }

    /**
     * Fixed: was @GetMapping — DELETE operations must be POST to prevent CSRF.
     */
    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        this.services.deleteUser(id);
        return "redirect:/admin/services";
    }
}
