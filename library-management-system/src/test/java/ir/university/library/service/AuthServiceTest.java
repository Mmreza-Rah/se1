package ir.university.library.service;

import ir.university.library.model.Student;
import ir.university.library.model.User;
import ir.university.library.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for AuthService
 * Covers all scenarios from requirements
 */
class AuthServiceTest {
    private AuthService authService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
        userRepository = UserRepository.getInstance();
        userRepository.clear();
    }

    @AfterEach
    void tearDown() {
        userRepository.clear();
    }

    // سناریو ۱-۱: ثبت‌نام یک کاربر جدید با نام کاربری منحصربه‌فرد
    @Test
    @DisplayName("1-1: Register new student with unique username should return true")
    void testRegisterNewStudentWithUniqueUsername() {
        boolean result = authService.registerStudent(
            "student1", 
            "password123", 
            "S001", 
            "John Doe", 
            "john@test.com"
        );
        
        assertTrue(result, "Registration should succeed with unique username");
        assertTrue(userRepository.existsByUsername("student1"));
    }

    // سناریو ۱-۲: ثبت‌نام با نام کاربری تکراری
    @Test
    @DisplayName("1-2: Register with duplicate username should return false")
    void testRegisterWithDuplicateUsername() {
        // First registration
        authService.registerStudent("student1", "pass123", "S001", "John", "john@test.com");
        
        // Second registration with same username
        boolean result = authService.registerStudent(
            "student1", 
            "pass456", 
            "S002", 
            "Jane", 
            "jane@test.com"
        );
        
        assertFalse(result, "Registration should fail with duplicate username");
    }

    // سناریو ۱-۳: ورود با نام کاربری و رمز عبور صحیح
    @Test
    @DisplayName("1-3: Login with correct username and password should succeed")
    void testLoginWithCorrectCredentials() {
        authService.registerStudent("student1", "password123", "S001", "John", "john@test.com");
        
        Optional<User> result = authService.login("student1", "password123");
        
        assertTrue(result.isPresent(), "Login should succeed with correct credentials");
        assertEquals("student1", result.get().getUsername());
        assertTrue(result.get() instanceof Student);
    }

    // سناریو ۱-۴: ورود با نام کاربری صحیح اما رمز عبور نادرست
    @Test
    @DisplayName("1-4: Login with correct username but wrong password should fail")
    void testLoginWithWrongPassword() {
        authService.registerStudent("student1", "password123", "S001", "John", "john@test.com");
        
        Optional<User> result = authService.login("student1", "wrongpassword");
        
        assertFalse(result.isPresent(), "Login should fail with wrong password");
    }

    // سناریو ۱-۵: ورود با نام کاربری که وجود ندارد
    @Test
    @DisplayName("1-5: Login with non-existent username should fail")
    void testLoginWithNonExistentUsername() {
        Optional<User> result = authService.login("nonexistent", "password123");
        
        assertFalse(result.isPresent(), "Login should fail with non-existent username");
    }

    // Additional tests
    @Test
    @DisplayName("Register with empty username should throw exception")
    void testRegisterWithEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            authService.registerStudent("", "pass123", "S001", "John", "john@test.com");
        });
    }

    @Test
    @DisplayName("Register with null username should throw exception")
    void testRegisterWithNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            authService.registerStudent(null, "pass123", "S001", "John", "john@test.com");
        });
    }

    @Test
    @DisplayName("Register with empty password should throw exception")
    void testRegisterWithEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            authService.registerStudent("student1", "", "S001", "John", "john@test.com");
        });
    }

    @Test
    @DisplayName("Change password successfully")
    void testChangePasswordSuccessfully() {
        authService.registerStudent("student1", "oldpass", "S001", "John", "john@test.com");
        
        boolean result = authService.changePassword("student1", "oldpass", "newpass");
        
        assertTrue(result, "Password change should succeed");
        
        // Verify can login with new password
        Optional<User> loginResult = authService.login("student1", "newpass");
        assertTrue(loginResult.isPresent());
    }

    @Test
    @DisplayName("Change password with wrong old password should fail")
    void testChangePasswordWithWrongOldPassword() {
        authService.registerStudent("student1", "oldpass", "S001", "John", "john@test.com");
        
        boolean result = authService.changePassword("student1", "wrongold", "newpass");
        
        assertFalse(result, "Password change should fail with wrong old password");
    }

    @Test
    @DisplayName("Get user by username")
    void testGetUserByUsername() {
        authService.registerStudent("student1", "pass123", "S001", "John", "john@test.com");
        
        Optional<User> result = authService.getUserByUsername("student1");
        
        assertTrue(result.isPresent());
        assertEquals("student1", result.get().getUsername());
    }
}