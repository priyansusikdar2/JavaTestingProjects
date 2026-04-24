package com.testing.demo.unit;

import com.testing.demo.exception.UserNotFoundException;
import com.testing.demo.model.User;
import com.testing.demo.repository.UserRepository;
import com.testing.demo.service.EmailService;
import com.testing.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Tag("unit")
@DisplayName("User Service Unit Tests")
class UserServiceTest {

    private UserRepository userRepository;
    private EmailService emailService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        emailService = mock(EmailService.class);
        userService = new UserService(userRepository, emailService);
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        // Given
        User savedUser = new User(1L, "john_doe", "john@example.com", 25);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(emailService.sendWelcomeEmail(anyString(), anyString())).thenReturn(true);

        // When
        User result = userService.createUser("john_doe", "john@example.com", 25);

        // Then
        assertThat(result)
                .isNotNull()
                .satisfies(user -> {
                    assertThat(user.getId()).isEqualTo(1L);
                    assertThat(user.getUsername()).isEqualTo("john_doe");
                    assertThat(user.getEmail()).isEqualTo("john@example.com");
                    assertThat(user.getAge()).isEqualTo(25);
                });

        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendWelcomeEmail("john@example.com", "john_doe");
    }

    @Test
    @DisplayName("Should throw exception when username is empty")
    void shouldThrowExceptionWhenUsernameIsEmpty() {
        assertThatThrownBy(() -> userService.createUser("", "john@example.com", 25))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username cannot be null or empty");

        verify(userRepository, never()).save(any());
        verify(emailService, never()).sendWelcomeEmail(anyString(), anyString());
    }

    @Test
    @DisplayName("Should throw exception when username is too short")
    void shouldThrowExceptionWhenUsernameIsTooShort() {
        assertThatThrownBy(() -> userService.createUser("jo", "john@example.com", 25))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username must be at least 3 characters");
    }

    @Test
    @DisplayName("Should throw exception when username is null")
    void shouldThrowExceptionWhenUsernameIsNull() {
        assertThatThrownBy(() -> userService.createUser(null, "john@example.com", 25))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username cannot be null or empty");
    }

    @ParameterizedTest(name = "Email {0} should be invalid")
    @ValueSource(strings = {"invalid-email", "missing@", "@domain.com", "plainaddress", "user@", "@.com"})
    @DisplayName("Should throw exception for invalid email formats")
    void shouldThrowExceptionWhenEmailIsInvalid(String invalidEmail) {
        assertThatThrownBy(() -> userService.createUser("john_doe", invalidEmail, 25))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid email format");
    }

    @ParameterizedTest(name = "Age {0} should be invalid")
    @CsvSource({
            "-1, Age must be between 0 and 150",
            "200, Age must be between 0 and 150",
            "151, Age must be between 0 and 150",
            "-10, Age must be between 0 and 150"
    })
    @DisplayName("Should throw exception for invalid age values")
    void shouldThrowExceptionWhenAgeIsInvalid(int age, String expectedMessage) {
        assertThatThrownBy(() -> userService.createUser("john_doe", "john@example.com", age))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    @DisplayName("Should get user by id successfully")
    void shouldGetUserByIdSuccessfully() {
        // Given
        User expectedUser = new User(1L, "john_doe", "john@example.com", 25);
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));

        // When
        User result = userService.getUserById(1L);

        // Then
        assertThat(result)
                .isNotNull()
                .isEqualTo(expectedUser)
                .matches(user -> user.getId().equals(1L))
                .matches(user -> user.getUsername().equals("john_doe"));

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        Long nonExistentId = 999L;
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUserById(nonExistentId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with id: " + nonExistentId);

        verify(userRepository).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should throw exception when user id is invalid")
    void shouldThrowExceptionWhenUserIdIsInvalid() {
        assertThatThrownBy(() -> userService.getUserById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid user id");

        assertThatThrownBy(() -> userService.getUserById(0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid user id");

        assertThatThrownBy(() -> userService.getUserById(-1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid user id");

        verify(userRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should update user email successfully")
    void shouldUpdateUserEmailSuccessfully() {
        // Given
        User existingUser = new User(1L, "john_doe", "old@example.com", 25);
        User updatedUser = new User(1L, "john_doe", "new@example.com", 25);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        User result = userService.updateUserEmail(1L, "new@example.com");

        // Then
        assertThat(result.getEmail()).isEqualTo("new@example.com");
        verify(userRepository).findById(1L);
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Should throw exception when updating email for non-existent user")
    void shouldThrowExceptionWhenUpdatingEmailForNonExistentUser() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUserEmail(999L, "new@example.com"))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent user")
    void shouldThrowExceptionWhenDeletingNonExistentUser() {
        // Given
        when(userRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).existsById(999L);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should get all users")
    void shouldGetAllUsers() {
        // Given
        List<User> users = List.of(
                new User(1L, "user1", "user1@example.com", 20),
                new User(2L, "user2", "user2@example.com", 30),
                new User(3L, "user3", "user3@example.com", 25)
        );
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertThat(result)
                .hasSize(3)
                .containsExactlyElementsOf(users)
                .allMatch(user -> user.getAge() > 0)
                .extracting(User::getUsername)
                .containsExactly("user1", "user2", "user3");

        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should check if user is adult")
    void shouldCheckIfUserIsAdult() {
        // Given
        User adultUser = new User(1L, "adult", "adult@example.com", 18);
        User adultUser2 = new User(2L, "adult2", "adult2@example.com", 25);
        User minorUser = new User(3L, "minor", "minor@example.com", 17);
        User childUser = new User(4L, "child", "child@example.com", 5);

        // When & Then
        assertThat(userService.isAdult(adultUser)).isTrue();
        assertThat(userService.isAdult(adultUser2)).isTrue();
        assertThat(userService.isAdult(minorUser)).isFalse();
        assertThat(userService.isAdult(childUser)).isFalse();
    }

    @Test
    @DisplayName("Should handle email sending failure gracefully")
    void shouldHandleEmailSendingFailure() {
        // Given
        User savedUser = new User(1L, "john_doe", "john@example.com", 25);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(emailService.sendWelcomeEmail(anyString(), anyString())).thenReturn(false);

        // When
        User result = userService.createUser("john_doe", "john@example.com", 25);

        // Then - User should still be created even if email fails
        assertThat(result).isNotNull();
        verify(userRepository).save(any(User.class));
        verify(emailService).sendWelcomeEmail("john@example.com", "john_doe");
    }
}
