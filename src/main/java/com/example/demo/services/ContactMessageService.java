package com.example.demo.services;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.ContactMessage;
import com.example.demo.repositories.ContactMessageRepository;

@Service
public class ContactMessageService {

    private static final Logger log = LoggerFactory.getLogger(ContactMessageService.class);

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    public void saveMessage(ContactMessage message) {
        message.setSubmittedAt(LocalDateTime.now());
        message.setRead(false);
        contactMessageRepository.save(message);
        log.info("Contact message saved from: {}", message.getEmail());
    }

    public List<ContactMessage> getAllMessages() {
        return contactMessageRepository.findAllByOrderBySubmittedAtDesc();
    }

    public long countUnread() {
        return contactMessageRepository.findByReadFalse().size();
    }

    public void markAsRead(Integer id) {
        contactMessageRepository.findById(id).ifPresent(msg -> {
            msg.setRead(true);
            contactMessageRepository.save(msg);
        });
    }
}
