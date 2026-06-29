package com.example.demo.services;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Admin;
import com.example.demo.entities.User;
import com.example.demo.repositories.AdminRepository;
import com.example.demo.repositories.UserRepository;

/**
 * Implements Spring Security's UserDetailsService to load both Admins and Users
 * by their email. Admins get ROLE_ADMIN, Users get ROLE_USER.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // First check if it's an admin
        Admin admin = adminRepository.findByAdminEmail(email);
        if (admin != null) {
            Collection<GrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new org.springframework.security.core.userdetails.User(
                    admin.getAdminEmail(),
                    admin.getAdminPassword(),
                    authorities
            );
        }

        // Then check if it's a regular user
        User user = userRepository.findUserByUemail(email);
        if (user != null) {
            Collection<GrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            return new org.springframework.security.core.userdetails.User(
                    user.getUemail(),
                    user.getUpassword(),
                    authorities
            );
        }

        throw new UsernameNotFoundException("No account found with email: " + email);
    }
}
