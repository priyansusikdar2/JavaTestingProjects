package com.testing.demo.integration;

import com.testing.demo.exception.UserNotFoundException;
import com.testing.demo.model.User;
import com.testing.demo.repository.UserRepository;
import com.testing.demo.service.EmailService;
import com.testing.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("integration")
@DisplayName("User Service Integration Tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceIntegrationTest {

    private UserRepository userRepository;
    private EmailService emailService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        emailService = new EmailService();
        userService = new UserService(userRepository, emailService);
    }

    @Test
    @DisplayName("Should create and retrieve user in real database")
    void shouldCreateAndRetrieveUser() {
        // Create user
        User createdUser = userService.createUser("integration_user", "integration@test.com", 30);

        // Retrieve user
        User retrievedUser = userService.getUserById(createdUser.getId());

        // Verify
        assertThat(retrievedUser)
                .isNotNull()
                .isEqualTo(createdUser)
                .satisfies(user -> {
                    assertThat(user.getUsername()).isEqualTo("integration_user");
                    assertThat(user.getEmail()).isEqualTo("integration@test.com");
                    assertThat(user.getAge()).isEqualTo(30);
                });
    }

    @Test
    @DisplayName("Should create multiple users and list them")
    void shouldCreateMultipleUsersAndListThem() {
        // Create multiple users
        User user1 = userService.createUser("user1", "user1@test.com", 25);
        User user2 = userService.createUser("user2", "user2@test.com", 35);
        User user3 = userService.createUser("user3", "user3@test.com", 45);

        // Get all users
        List<User> allUsers = userService.getAllUsers();

        // Verify
        assertThat(allUsers)
                .hasSize(3)
                .containsExactlyInAnyOrder(user1, user2, user3)
                .allMatch(user -> user.getId() != null);
    }

    @Test
    @DisplayName("Should update user email in real database")
    void shouldUpdateUserEmail() {
        // Create user
        User user = userService.createUser("update_user", "old@test.com", 28);

        // Update email
        String newEmail = "new@test.com";
        User updatedUser = userService.updateUserEmail(user.getId(), newEmail);

        // Verify
        assertThat(updatedUser.getEmail()).isEqualTo(newEmail);

        // Retrieve and verify persistence
        User retrievedUser = userService.getUserById(user.getId());
        assertThat(retrievedUser.getEmail()).isEqualTo(newEmail);
    }

    @Test
    @DisplayName("Should delete user from real database")
    void shouldDeleteUser() {
        // Create user
        User user = userService.createUser("delete_user", "delete@test.com", 22);

        // Verify user exists
        assertThat(userService.getUserById(user.getId())).isNotNull();

        // Delete user
        userService.deleteUser(user.getId());

        // Verify user is deleted
        assertThatThrownBy(() -> userService.getUserById(user.getId()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    @DisplayName("Should handle concurrent user operations correctly")
    void shouldHandleConcurrentUserOperations() throws InterruptedException {
        // Create initial user
        User initialUser = userService.createUser("concurrent_user", "concurrent@test.com", 25);

        // Simulate concurrent updates
        Thread thread1 = new Thread(() -> {
            userService.updateUserEmail(initialUser.getId(), "thread1@test.com");
        });

        Thread thread2 = new Thread(() -> {
            userService.updateUserEmail(initialUser.getId(), "thread2@test.com");
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        // Verify final state
        User finalUser = userService.getUserById(initialUser.getId());
        assertThat(finalUser.getEmail()).isIn("thread1@test.com", "thread2@test.com");
    }

    @Test
    @DisplayName("Should maintain user data integrity across operations")
    void shouldMaintainDataIntegrity() {
        // Create user
        User user = userService.createUser("integrity_user", "integrity@test.com", 25);

        // Perform multiple operations
        userService.updateUserEmail(user.getId(), "updated1@test.com");
        userService.updateUserEmail(user.getId(), "updated2@test.com");
        userService.updateUserEmail(user.getId(), "updated3@test.com");

        // Verify all updates were applied
        User finalUser = userService.getUserById(user.getId());
        assertThat(finalUser.getEmail()).isEqualTo("updated3@test.com");
        assertThat(finalUser.getUsername()).isEqualTo("integrity_user");
        assertThat(finalUser.getAge()).isEqualTo(25);
    }

    @Test
    @DisplayName("Should handle error cases gracefully")
    void shouldHandleErrorCasesGracefully() {
        // Try to get non-existent user
        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(UserNotFoundException.class);

        // Create a user
        User user = userService.createUser("error_test", "error@test.com", 25);

        // Delete the user
        userService.deleteUser(user.getId());

        // Try to update deleted user
        assertThatThrownBy(() -> userService.updateUserEmail(user.getId(), "new@test.com"))
                .isInstanceOf(UserNotFoundException.class);

        // Try to delete again
        assertThatThrownBy(() -> userService.deleteUser(user.getId()))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("Should create users with various ages")
    void shouldCreateUsersWithVariousAges() {
        User youngUser = userService.createUser("young", "young@test.com", 18);
        User adultUser = userService.createUser("adult", "adult@test.com", 35);
        User seniorUser = userService.createUser("senior", "senior@test.com", 65);

        assertThat(userService.isAdult(youngUser)).isTrue();
        assertThat(userService.isAdult(adultUser)).isTrue();
        assertThat(userService.isAdult(seniorUser)).isTrue();

        User minorUser = userService.createUser("minor", "minor@test.com", 17);
        assertThat(userService.isAdult(minorUser)).isFalse();
    }
}
