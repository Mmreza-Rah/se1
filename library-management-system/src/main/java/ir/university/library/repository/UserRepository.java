package ir.university.library.repository;

import ir.university.library.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository for managing users in memory
 */
public class UserRepository {
    private final Map<String, User> users;
    private static UserRepository instance;

    private UserRepository() {
        this.users = new HashMap<>();
        initializeDefaultUsers();
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    private void initializeDefaultUsers() {
        // Create default manager
        Manager manager = new Manager("admin", "admin123", "Library Manager");
        users.put(manager.getUsername(), manager);

        // Create default staff members
        Staff staff1 = new Staff("staff1", "staff123", "S001", "John Doe");
        Staff staff2 = new Staff("staff2", "staff123", "S002", "Jane Smith");
        Staff staff3 = new Staff("staff3", "staff123", "S003", "Bob Johnson");
        
        users.put(staff1.getUsername(), staff1);
        users.put(staff2.getUsername(), staff2);
        users.put(staff3.getUsername(), staff3);
    }

    public void save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        users.put(user.getUsername(), user);
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public List<Student> findAllStudents() {
        return users.values().stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .collect(Collectors.toList());
    }

    public List<Staff> findAllStaff() {
        return users.values().stream()
                .filter(user -> user instanceof Staff)
                .map(user -> (Staff) user)
                .collect(Collectors.toList());
    }

    public long countStudents() {
        return users.values().stream()
                .filter(user -> user instanceof Student)
                .count();
    }

    public long countActiveStudents() {
        return users.values().stream()
                .filter(user -> user instanceof Student && user.isActive())
                .count();
    }

    public void delete(String username) {
        users.remove(username);
    }

    public void clear() {
        users.clear();
        initializeDefaultUsers();
    }
}