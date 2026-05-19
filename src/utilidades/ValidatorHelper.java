package utilidades;

import java.util.regex.Pattern;

public class ValidatorHelper {
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE = Pattern.compile("^[0-9\\-+() ]{6,20}$");

    public static boolean isEmpty(String s) { return s == null || s.trim().isEmpty(); }
    public static boolean isValidEmail(String s) { return s != null && EMAIL.matcher(s).matches(); }
    public static boolean isValidPhone(String s) { return s != null && PHONE.matcher(s).matches(); }
    public static boolean isValidDNI(String s) { return s != null && s.matches("^[0-9A-Za-z-]{4,20}$"); }
    public static boolean isValidAge(int years) { return years >= 0 && years < 200; }
}
