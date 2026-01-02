package ir.university.library.util;

import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class InputValidator {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    
    private static final Pattern ISBN_PATTERN = 
        Pattern.compile("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
    
    /**
     * Validate if string is not null and not empty
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Validate if string is empty
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    /**
     * Validate username format
     * Rules: 3-20 characters, alphanumeric and underscore only
     */
    public static boolean isValidUsername(String username) {
        return isNotEmpty(username) && USERNAME_PATTERN.matcher(username).matches();
    }
    
    /**
     * Validate password strength
     * Rules: At least 6 characters
     */
    public static boolean isValidPassword(String password) {
        return isNotEmpty(password) && password.length() >= 6;
    }
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return isNotEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validate ISBN format
     */
    public static boolean isValidISBN(String isbn) {
        if (isEmpty(isbn)) {
            return false;
        }
        return ISBN_PATTERN.matcher(isbn).matches();
    }
    
    /**
     * Validate year
     */
    public static boolean isValidYear(int year) {
        int currentYear = java.time.Year.now().getValue();
        return year >= 1000 && year <= currentYear + 1;
    }
    
    /**
     * Validate positive integer
     */
    public static boolean isPositiveInteger(int value) {
        return value > 0;
    }
    
    /**
     * Validate non-negative integer
     */
    public static boolean isNonNegativeInteger(int value) {
        return value >= 0;
    }
    
    /**
     * Validate string length
     */
    public static boolean isValidLength(String value, int minLength, int maxLength) {
        if (isEmpty(value)) {
            return false;
        }
        int length = value.trim().length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * Validate phone number (simple validation)
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (isEmpty(phoneNumber)) {
            return false;
        }
        String cleaned = phoneNumber.replaceAll("[^0-9]", "");
        return cleaned.length() >= 10 && cleaned.length() <= 15;
    }
    
    /**
     * Sanitize string (remove leading/trailing whitespace)
     */
    public static String sanitize(String value) {
        return value == null ? "" : value.trim();
    }
    
    /**
     * Validate and sanitize username
     */
    public static String validateAndSanitizeUsername(String username) {
        String sanitized = sanitize(username);
        if (!isValidUsername(sanitized)) {
            throw new IllegalArgumentException("Invalid username format. Must be 3-20 characters, alphanumeric and underscore only.");
        }
        return sanitized;
    }
    
    /**
     * Validate and sanitize password
     */
    public static String validateAndSanitizePassword(String password) {
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Invalid password. Must be at least 6 characters.");
        }
        return password;
    }
    
    /**
     * Validate and sanitize email
     */
    public static String validateAndSanitizeEmail(String email) {
        String sanitized = sanitize(email);
        if (!isValidEmail(sanitized)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        return sanitized;
    }
}