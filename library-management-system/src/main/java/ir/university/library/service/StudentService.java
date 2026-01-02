package ir.university.library.service;

import ir.university.library.model.Student;
import ir.university.library.model.User;
import ir.university.library.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service for student-specific operations
 */
public class StudentService {
    private final UserRepository userRepository;

    public StudentService() {
        this.userRepository = UserRepository.getInstance();
    }

    /**
     * Get student by username
     */
    public Optional<Student> getStudentByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get() instanceof Student) {
            return Optional.of((Student) userOpt.get());
        }
        return Optional.empty();
    }

    /**
     * Get all students
     */
    public List<Student> getAllStudents() {
        return userRepository.findAllStudents();
    }

    /**
     * Activate or deactivate a student
     */
    public boolean setStudentStatus(String username, boolean active) {
        Optional<Student> studentOpt = getStudentByUsername(username);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            student.setActive(active);
            userRepository.save(student);
            return true;
        }
        return false;
    }

    /**
     * Check if student is active
     */
    public boolean isStudentActive(String username) {
        Optional<Student> studentOpt = getStudentByUsername(username);
        return studentOpt.map(Student::isActive).orElse(false);
    }

    /**
     * Get total student count
     */
    public long getTotalStudentCount() {
        return userRepository.countStudents();
    }

    /**
     * Get active student count
     */
    public long getActiveStudentCount() {
        return userRepository.countActiveStudents();
    }

    /**
     * Update student profile
     */
    public boolean updateStudentProfile(String username, String fullName, String email) {
        Optional<Student> studentOpt = getStudentByUsername(username);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (fullName != null && !fullName.trim().isEmpty()) {
                student.setFullName(fullName);
            }
            if (email != null && !email.trim().isEmpty()) {
                student.setEmail(email);
            }
            userRepository.save(student);
            return true;
        }
        return false;
    }
}