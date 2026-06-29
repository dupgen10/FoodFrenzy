package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

/**
 * Global exception handler.
 * Catches specific exception types and provides appropriate error responses.
 */
@ControllerAdvice
public class Exceptions {

    private static final Logger log = LoggerFactory.getLogger(Exceptions.class);

    /**
     * Handles EntityNotFoundException (e.g., admin/user/product not found by ID).
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFound(EntityNotFoundException ex, Model model) {
        log.warn("Entity not found: {}", ex.getMessage());
        model.addAttribute("errorTitle", "Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("backUrl", "/admin/services");
        return "exception";
    }

    /**
     * Catch-all for unexpected errors.
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {
        log.error("Unexpected exception: {}", ex.getMessage(), ex);
        model.addAttribute("errorTitle", "Something went wrong");
        model.addAttribute("errorMessage",
                "An unexpected error occurred. Please try again or contact support.");
        model.addAttribute("backUrl", "/home");
        return "exception";
    }
}