package ir.ac.kntu.LMS;

import ir.ac.kntu.util.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void validEmailShouldReturnTrue() {
        assertTrue(Validator.isValidEmail("test@gmail.com"));
    }

    @Test
    void invalidEmailShouldReturnFalse() {
        assertFalse(Validator.isValidEmail("gmail.com"));
    }

    @Test
    void validItemIdShouldReturnTrue() {
        assertTrue(Validator.isValidItemId("BOK-12345678"));
    }

    @Test
    void invalidItemIdShouldReturnFalse() {
        assertFalse(Validator.isValidItemId("BOOK-123"));
    }

    @Test
    void validMemberIdShouldReturnTrue() {
        assertTrue(Validator.isValidMemberId("STU-123456"));
    }

    @Test
    void invalidMemberIdShouldReturnFalse() {
        assertFalse(Validator.isValidMemberId("123456"));
    }

    @Test
    void validPhoneNumberShouldReturnTrue() {
        assertTrue(Validator.isValidPhoneNum("09123456789"));
    }

    @Test
    void invalidPhoneNumberShouldReturnFalse() {
        assertFalse(Validator.isValidPhoneNum("91234"));
    }

    @Test
    void validPasswordShouldReturnTrue() {
        assertTrue(Validator.isValidPassword("Abcd1234!"));
    }

    @Test
    void invalidPasswordShouldReturnFalse() {
        assertFalse(Validator.isValidPassword("password"));
    }

    @Test
    void validPublishYearShouldReturnTrue() {
        assertTrue(Validator.isValidPublishYear(2025));
    }

    @Test
    void invalidPublishYearShouldReturnFalse() {
        assertFalse(Validator.isValidPublishYear(1300));
    }

    @Test
    void validISBNShouldReturnTrue() {
        assertTrue(Validator.isValidISBN("9781234567890"));
    }

    @Test
    void invalidISBNShouldReturnFalse() {
        assertFalse(Validator.isValidISBN("123"));
    }

    @Test
    void validISSNShouldReturnTrue() {
        assertTrue(Validator.isValidISSN("1234-567X"));
    }

    @Test
    void invalidISSNShouldReturnFalse() {
        assertFalse(Validator.isValidISSN("1234567"));
    }
}