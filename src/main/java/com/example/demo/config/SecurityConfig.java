package com.example.demo.config;

import com.example.demo.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                // Public pages — accessible by anyone
                .requestMatchers(
                    "/", "/home", "/products", "/about", "/location",
                    "/login", "/register",
                    "/adminLogin", "/userLogin",
                    "/css/**", "/Images/**", "/JavaScript/**",
                    "/error"
                ).permitAll()
                // Admin-only pages
                .requestMatchers(
                    "/admin/services",
                    "/addAdmin", "/addingAdmin",
                    "/updateAdmin/**", "/updatingAdmin/**", "/deleteAdmin/**",
                    "/addProduct", "/addingProduct",
                    "/updateProduct/**", "/updatingProduct/**", "/deleteProduct/**",
                    "/addUser", "/addingUser",
                    "/updateUser/**", "/updatingUser/**", "/deleteUser/**",
                    "/contact/messages"
                ).hasRole("ADMIN")
                // User pages (authenticated users + admins)
                .requestMatchers("/product/**").hasAnyRole("USER", "ADMIN")
                // Everything else requires authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            // CSRF is enabled by default — Thymeleaf auto-injects tokens on th:action forms
            .csrf(csrf -> csrf.ignoringRequestMatchers("/adminLogin", "/userLogin"));

        return http.build();
    }
}
