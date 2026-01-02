package ir.university.library.service;

import ir.university.library.model.Staff;
import ir.university.library.model.User;
import ir.university.library.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service for staff-specific operations
 */
public class StaffService {
    private final UserRepository userRepository;

    public StaffService() {
        this.userRepository = UserRepository.getInstance();
    }

    /**
     * Create a new staff member (only by manager)
     */
    public Staff createStaff(String username, String password, String staffId, String fullName) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        Staff staff = new Staff(username, password, staffId, fullName);
        userRepository.save(staff);
        return staff;
    }

    /**
     * Get staff by username
     */
    public Optional<Staff> getStaffByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get() instanceof Staff) {
            return Optional.of((Staff) userOpt.get());
        }
        return Optional.empty();
    }

    /**
     * Get all staff members
     */
    public List<Staff> getAllStaff() {
        return userRepository.findAllStaff();
    }

    /**
     * Update staff profile
     */
    public boolean updateStaffProfile(String username, String fullName) {
        Optional<Staff> staffOpt = getStaffByUsername(username);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            if (fullName != null && !fullName.trim().isEmpty()) {
                staff.setFullName(fullName);
            }
            userRepository.save(staff);
            return true;
        }
        return false;
    }

    /**
     * Get staff performance statistics
     */
    public StaffPerformance getStaffPerformance(String username) {
        Optional<Staff> staffOpt = getStaffByUsername(username);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            return new StaffPerformance(
                username,
                staff.getBooksRegistered(),
                staff.getBooksLent(),
                staff.getBooksReceived()
            );
        }
        return null;
    }

    /**
     * Staff performance data class
     */
    public static class StaffPerformance {
        public final String username;
        public final int booksRegistered;
        public final int booksLent;
        public final int booksReceived;

        public StaffPerformance(String username, int booksRegistered, int booksLent, int booksReceived) {
            this.username = username;
            this.booksRegistered = booksRegistered;
            this.booksLent = booksLent;
            this.booksReceived = booksReceived;
        }
    }
}