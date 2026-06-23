package ir.ac.kntu.util;

import java.util.regex.Pattern;

public final class Validator {
    private Validator(){}

    private final static Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*@(?:[a-zA-Z0-9]+(?:-[a-zA-Z0-9]+)*\\.)+[a-zA-Z]{2,6}$");
    private final static Pattern ITEMID_PATTERN = Pattern.compile("(?:EBK|MAG|BOK|AUD)-\\d{8}");
    private final static Pattern MEMBERID_PATTERN = Pattern.compile("(?:GST|STU|FAC)-\\d{6}");
    private final static Pattern PHONENUM_PATTERN = Pattern.compile("(?:0|\\+98|98)\\d{10}");
    private final static Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$");
    private final static Pattern URL_PATTERN = Pattern.compile("^https://.*");
    private final static Pattern ISBN_PATTERN = Pattern.compile("^(?:979|978)\\d{10}");
    private final static Pattern ISSN_PATTERN = Pattern.compile("^\\d{4}-\\d{3}[\\dX]");


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

    public static boolean isValidUrl(String url) {
        return URL_PATTERN.matcher(url).matches();
    }

    public static boolean isValidISBN(String isbn) {
        return ISBN_PATTERN.matcher(isbn).matches();
    }

    public static boolean isValidISSN(String issn) {
        return ISSN_PATTERN.matcher(issn).matches();
    }

}
