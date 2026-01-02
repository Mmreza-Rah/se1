package ir.university.library.repository;

import ir.university.library.model.Staff;
import ir.university.library.model.Student;
import ir.university.library.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserRepository
 */
class UserRepositoryTest {
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository = UserRepository.getInstance();
        repository.clear();
    }

    @AfterEach
    void tearDown() {
        repository.clear();
    }

    @Test
    void testSaveAndFindUser() {
        Student student = new Student("student1", "pass123", "S001", "John Doe", "john@test.com");
        repository.save(student);

        Optional<User> found = repository.findByUsername("student1");
        assertTrue(found.isPresent());
        assertEquals("student1", found.get().getUsername());
        assertTrue(found.get() instanceof Student);
    }

    @Test
    void testExistsByUsername() {
        Student student = new Student("student1", "pass123");
        repository.save(student);

        assertTrue(repository.existsByUsername("student1"));
        assertFalse(repository.existsByUsername("nonexistent"));
    }

    @Test
    void testFindAllStudents() {
        Student student1 = new Student("student1", "pass123");
        Student student2 = new Student("student2", "pass456");
        Staff staff = new Staff("staff1", "pass789");

        repository.save(student1);
        repository.save(student2);
        repository.save(staff);

        List<Student> students = repository.findAllStudents();
        assertEquals(2, students.size());
    }

    @Test
    void testFindAllStaff() {
        // Note: Repository initializes with 3 default staff (staff1, staff2, staff3)
        // So we need to account for them
        long initialStaffCount = repository.findAllStaff().size();
        
        Staff staff4 = new Staff("staff4", "pass123");
        Staff staff5 = new Staff("staff5", "pass456");
        Student student = new Student("student1", "pass789");

        repository.save(staff4);
        repository.save(staff5);
        repository.save(student);

        List<Staff> staffList = repository.findAllStaff();
        assertEquals(initialStaffCount + 2, staffList.size());
    }

    @Test
    void testCountStudents() {
        Student student1 = new Student("student1", "pass123");
        Student student2 = new Student("student2", "pass456");
        repository.save(student1);
        repository.save(student2);

        assertEquals(2, repository.countStudents());
    }

    @Test
    void testCountActiveStudents() {
        Student student1 = new Student("student1", "pass123");
        Student student2 = new Student("student2", "pass456");
        student2.setActive(false);

        repository.save(student1);
        repository.save(student2);

        assertEquals(1, repository.countActiveStudents());
    }

    @Test
    void testDeleteUser() {
        Student student = new Student("student1", "pass123");
        repository.save(student);

        assertTrue(repository.existsByUsername("student1"));
        repository.delete("student1");
        assertFalse(repository.existsByUsername("student1"));
    }

    @Test
    void testSaveNullUser() {
        assertThrows(IllegalArgumentException.class, () -> {
            repository.save(null);
        });
    }

    @Test
    void testDefaultUsersExist() {
        // After clear, default users should be initialized
        assertTrue(repository.existsByUsername("admin"));
        assertTrue(repository.existsByUsername("staff1"));
        assertTrue(repository.existsByUsername("staff2"));
        assertTrue(repository.existsByUsername("staff3"));
    }
}