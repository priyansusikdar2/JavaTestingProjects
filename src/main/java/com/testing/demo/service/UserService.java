package com.testing.demo.service;

import com.testing.demo.exception.UserNotFoundException;
import com.testing.demo.model.User;
import com.testing.demo.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public User createUser(String username, String email, int age) {
        validateUserData(username, email, age);

        User user = new User(null, username, email, age);
        User savedUser = userRepository.save(user);

        // Send welcome email
        emailService.sendWelcomeEmail(email, username);

        return savedUser;
    }

    public User getUserById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid user id");
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUserEmail(Long id, String newEmail) {
        validateEmail(newEmail);

        User user = getUserById(id);
        user.setEmail(newEmail);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    public boolean isAdult(User user) {
        return user.getAge() >= 18;
    }

    private void validateUserData(String username, String email, int age) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        if (username.length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters");
        }

        validateEmail(email);

        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}
