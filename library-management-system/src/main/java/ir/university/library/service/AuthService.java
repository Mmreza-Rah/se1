package ir.university.library.service;

import ir.university.library.model.Student;
import ir.university.library.model.User;
import ir.university.library.repository.UserRepository;

import java.util.Optional;

/**
 * Service for authentication and user management
 */
public class AuthService {
    private final UserRepository userRepository;

    public AuthService() {
        this.userRepository = UserRepository.getInstance();
    }

    /**
     * Register a new student
     */
    public boolean registerStudent(String username, String password, String studentId, 
                                   String fullName, String email) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        
        if (userRepository.existsByUsername(username)) {
            return false;
        }

        Student student = new Student(username, password, studentId, fullName, email);
        userRepository.save(student);
        return true;
    }

    /**
     * Login a user
     */
    public Optional<User> login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    /**
     * Change user password
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    /**
     * Get user by username
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}