package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.ContactMessage;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Integer> {
    List<ContactMessage> findAllByOrderBySubmittedAtDesc();
    List<ContactMessage> findByReadFalse();
}
