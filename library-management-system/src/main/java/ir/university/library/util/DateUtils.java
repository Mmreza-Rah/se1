package ir.university.library.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

/**
 * Utility methods for date operations
 */
public class DateUtils {
    
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Parse date string to LocalDate
     */
    public static LocalDate parseDate(String dateStr) throws DateTimeParseException {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }
    
    /**
     * Format LocalDate to string
     */
    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }
    
    /**
     * Format LocalDateTime to string
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATETIME_FORMATTER);
    }
    
    /**
     * Check if date is today
     */
    public static boolean isToday(LocalDate date) {
        return date.equals(LocalDate.now());
    }
    
    /**
     * Check if date is yesterday
     */
    public static boolean isYesterday(LocalDate date) {
        return date.equals(LocalDate.now().minusDays(1));
    }
    
    /**
     * Check if date is today or yesterday
     */
    public static boolean isTodayOrYesterday(LocalDate date) {
        return isToday(date) || isYesterday(date);
    }
    
    /**
     * Check if date is in the past
     */
    public static boolean isInPast(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }
    
    /**
     * Check if date is in the future
     */
    public static boolean isInFuture(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }
    
    /**
     * Calculate days between two dates
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }
    
    /**
     * Calculate days from date to now
     */
    public static long daysFromNow(LocalDate date) {
        return daysBetween(LocalDate.now(), date);
    }
    
    /**
     * Calculate days from now to date
     */
    public static long daysUntil(LocalDate date) {
        return daysBetween(LocalDate.now(), date);
    }
    
    /**
     * Get current date
     */
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }
    
    /**
     * Get current date time
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
    
    /**
     * Add days to a date
     */
    public static LocalDate addDays(LocalDate date, long days) {
        return date.plusDays(days);
    }
    
    /**
     * Subtract days from a date
     */
    public static LocalDate subtractDays(LocalDate date, long days) {
        return date.minusDays(days);
    }
    
    /**
     * Validate date string format
     */
    public static boolean isValidDateFormat(String dateStr) {
        try {
            parseDate(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}