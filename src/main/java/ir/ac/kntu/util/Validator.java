package ir.ac.kntu.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validator {
    private Validator(){}

    private final static Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*@(?:[a-zA-Z0-9]+(?:-[a-zA-Z0-9]+)*\\.)+[a-zA-Z]{2,6}$");
    private final static Pattern ITEMID_PATTERN = Pattern.compile("(?:EBK|MAG|BOK|AUD)-\\d{8}");
    private final static Pattern MEMBERID_PATTERN = Pattern.compile("(?:GST|STU|FAC)-\\d{6}");
    private final static Pattern PHONENUM_PATTERN = Pattern.compile("(?:0|\\+98|98)\\d{10}");
    private final static Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$");

    public static boolean isValidItemId(String id){
        return ITEMID_PATTERN.matcher(id).matches();
    }

    public static boolean isValidPublishYear(int publishYear){
        return publishYear >=1450 && publishYear <=2026;
    }

    public static boolean isValidMemberId(String id){
        return MEMBERID_PATTERN.matcher(id).matches();
    }

    public static boolean isValidPhoneNum(String phoneNum){
        return PHONENUM_PATTERN.matcher(phoneNum).matches();
    }

    public static boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
