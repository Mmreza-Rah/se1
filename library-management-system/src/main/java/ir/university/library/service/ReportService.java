package ir.university.library.service;

import ir.university.library.model.*;
import ir.university.library.repository.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for generating reports and statistics
 */
public class ReportService {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;

    public ReportService() {
        this.userRepository = UserRepository.getInstance();
        this.bookRepository = BookRepository.getInstance();
        this.borrowRepository = BorrowRepository.getInstance();
    }

    /**
     * Get student statistics
     */
    public StudentStats getStudentStats(String studentUsername) {
        List<BorrowRecord> records = borrowRepository.findRecordsByStudent(studentUsername);
        
        long totalBorrows = records.size();
        long notReturned = records.stream().filter(r -> !r.isReturned()).count();
        long lateReturns = records.stream().filter(BorrowRecord::isReturnedLate).count();

        return new StudentStats(studentUsername, totalBorrows, notReturned, lateReturns);
    }

    /**
     * Get staff performance
     */
    public StaffPerformance getStaffPerformance(String staffUsername) {
        Optional<User> userOpt = userRepository.findByUsername(staffUsername);
        if (userOpt.isEmpty() || !(userOpt.get() instanceof Staff)) {
            return null;
        }

        Staff staff = (Staff) userOpt.get();
        return new StaffPerformance(
            staffUsername,
            staff.getBooksRegistered(),
            staff.getBooksLent(),
            staff.getBooksReceived()
        );
    }

    /**
     * Get borrow statistics
     */
    public BorrowStats getBorrowStats() {
        long totalRequests = borrowRepository.countAllRequests();
        long approvedBorrows = borrowRepository.findAllRecords().size();
        
        List<BorrowRecord> returnedRecords = borrowRepository.findAllRecords().stream()
                .filter(BorrowRecord::isReturned)
                .collect(Collectors.toList());

        double avgDuration = 0;
        if (!returnedRecords.isEmpty()) {
            avgDuration = returnedRecords.stream()
                    .mapToLong(BorrowRecord::getBorrowDuration)
                    .average()
                    .orElse(0.0);
        }

        return new BorrowStats(totalRequests, approvedBorrows, avgDuration);
    }

    /**
     * Get top 10 students with most delays
     */
    public List<StudentDelayInfo> getTop10StudentsWithMostDelays() {
        Map<String, Long> studentDelays = new HashMap<>();
        
        for (BorrowRecord record : borrowRepository.findAllRecords()) {
            if (record.isReturnedLate()) {
                String student = record.getStudentUsername();
                long delay = record.getDelayDays();
                studentDelays.put(student, studentDelays.getOrDefault(student, 0L) + delay);
            }
        }

        return studentDelays.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(entry -> new StudentDelayInfo(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /**
     * Get general statistics
     */
    public GeneralStats getGeneralStats() {
        long totalStudents = userRepository.countStudents();
        long totalBooks = bookRepository.countAll();
        long totalBorrows = borrowRepository.findAllRecords().size();
        long activeBorrows = borrowRepository.countActiveRecords();

        return new GeneralStats(totalStudents, totalBooks, totalBorrows, activeBorrows);
    }

    // Inner classes for statistics
    public static class StudentStats {
        public final String username;
        public final long totalBorrows;
        public final long notReturned;
        public final long lateReturns;

        public StudentStats(String username, long totalBorrows, long notReturned, long lateReturns) {
            this.username = username;
            this.totalBorrows = totalBorrows;
            this.notReturned = notReturned;
            this.lateReturns = lateReturns;
        }
    }

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

    public static class BorrowStats {
        public final long totalRequests;
        public final long approvedBorrows;
        public final double avgBorrowDuration;

        public BorrowStats(long totalRequests, long approvedBorrows, double avgBorrowDuration) {
            this.totalRequests = totalRequests;
            this.approvedBorrows = approvedBorrows;
            this.avgBorrowDuration = avgBorrowDuration;
        }
    }

    public static class StudentDelayInfo {
        public final String username;
        public final long totalDelayDays;

        public StudentDelayInfo(String username, long totalDelayDays) {
            this.username = username;
            this.totalDelayDays = totalDelayDays;
        }
    }

    public static class GeneralStats {
        public final long totalStudents;
        public final long totalBooks;
        public final long totalBorrows;
        public final long activeBorrows;

        public GeneralStats(long totalStudents, long totalBooks, long totalBorrows, long activeBorrows) {
            this.totalStudents = totalStudents;
            this.totalBooks = totalBooks;
            this.totalBorrows = totalBorrows;
            this.activeBorrows = activeBorrows;
        }
    }
}