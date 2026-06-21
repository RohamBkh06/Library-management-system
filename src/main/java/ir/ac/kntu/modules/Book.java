package ir.ac.kntu.modules;

import ir.ac.kntu.util.Validator;

public class Book extends LibraryItem{
    private String author;
    private int pageCount;
    private long availableCopies;
    private String ISBN;

    public void setISBN(String ISBN) {
        if (!Validator.isValidISBN(ISBN)){
            throw new IllegalArgumentException("Invalid ISBN");
        }
        this.ISBN = ISBN;
    }

    public Book(String title, String id, int publishYear, String category, String author, int pageCount, long availableCopies, String ISBN) {
        super(title, id, publishYear, category);
        setISBN(ISBN);
        this.author = author;
        this.pageCount = pageCount;
        this.availableCopies = availableCopies;
    }

    @Override
    protected boolean isAvailable() {
        return this.availableCopies > 0;
    }
}
