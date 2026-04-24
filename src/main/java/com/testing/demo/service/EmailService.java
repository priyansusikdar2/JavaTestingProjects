package com.testing.demo.service;

import java.util.logging.Logger;

public class EmailService {
    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    public boolean sendWelcomeEmail(String email, String username) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        // Simulate email sending
        logger.info("Sending welcome email to " + email + " for user " + username);

        // In real scenario, this would send an actual email
        return true;
    }

    public boolean sendNotification(String email, String subject, String body) {
        if (email == null || !email.contains("@")) {
            return false;
        }

        logger.info("Sending notification to " + email + ": " + subject);
        return true;
    }
}
