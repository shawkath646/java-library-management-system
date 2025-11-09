package utils;

import java.util.regex.Pattern;

public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[0-9]{10,15}$");
    
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
    
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        String cleanPhone = phone.replaceAll("[\\s\\-()]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }
    
    public static boolean isPositive(int value) {
        return value > 0;
    }
    
    public static boolean isNonNegative(int value) {
        return value >= 0;
    }
}
