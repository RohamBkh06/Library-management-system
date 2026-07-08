package ir.ac.kntu.LMS;

import ir.ac.kntu.modules.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private Book book;

    @BeforeEach
    void setup() {

        book = new Book("Java", "BOK-12345678", 2024, "Programming", "James", 500, 2, "9781234567890");

    }

    @Test
    void constructorShouldInitializeFields() {

        assertEquals("Java", book.getTitle());
        assertEquals("James", book.getAuthor());
        assertEquals(500, book.getPageCount());
        assertEquals(2, book.getAvailableCopies());
        assertEquals("9781234567890", book.getIsbn());

    }

    @Test
    void lendShouldDecreaseAvailableCopies() {

        assertTrue(book.lend());

        assertEquals(1, book.getAvailableCopies());

    }

    @Test
    void lendShouldReturnFalseWhenNoCopiesRemain() {

        book.lend();
        book.lend();

        assertFalse(book.lend());

        assertEquals(0, book.getAvailableCopies());

    }

    @Test
    void returnBackShouldIncreaseCopies() {

        book.lend();

        book.returnBack();

        assertEquals(2, book.getAvailableCopies());

    }

    @Test
    void invalidIsbnShouldThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> book.setIsbn("12345")
        );

    }

    @Test
    void validIsbnShouldBeAccepted() {

        book.setIsbn("9791234567890");

        assertEquals("9791234567890", book.getIsbn());

    }

    @Test
    void invalidPublishYearShouldThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Book(
                        "Java",
                        "BOK-12345678",
                        1300,
                        "Programming",
                        "James",
                        100,
                        1,
                        "9781234567890"
                )
        );

    }

    @Test
    void invalidItemIdShouldThrowException() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Book(
                        "Java",
                        "ABC",
                        2024,
                        "Programming",
                        "James",
                        100,
                        1,
                        "9781234567890"
                )
        );

    }

    @Test
    void toStringShouldContainTitle() {

        assertTrue(book.toString().contains("Java"));

    }

}