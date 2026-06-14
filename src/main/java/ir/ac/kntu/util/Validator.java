package ir.ac.kntu.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Validator {
    private Validator(){}

    private final static Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*@(?:[a-zA-Z0-9]+(?:-[a-zA-Z0-9]+)*\\.)+[a-zA-Z]{2,6}$");
    private final static Pattern ITEMID_PATTERN = Pattern.compile("(?:EBK|MAG|BOK|AUD)-\\d{8}");
    private final static Pattern MEMBERID_PATTERN = Pattern.compile("(?:GST|STU|FAC)-\\d{6}");

    public static boolean isValidItemId(String id){
        return ITEMID_PATTERN.matcher(id).matches();
    }

    public static boolean isValidPublishYear(int publishYear){
        return publishYear >=1450 && publishYear <=2026;
    }

    public static boolean isValidMemberId(String id){
        return MEMBERID_PATTERN.matcher(id).matches();
    }
}
