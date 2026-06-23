package ir.ac.kntu.modules;

import ir.ac.kntu.util.Validator;

public class Book extends LibraryItem{
    private String author;
    private int pageCount;
    private long availableCopies;
    private String isbn;

    public void setIsbn(String isbn) {
        if (!Validator.isValidISBN(isbn)){
            throw new IllegalArgumentException("Invalid ISBN");
        }
        this.isbn = isbn;
    }

    public Book(String title, String id, int publishYear, String category, String author, int pageCount, long availableCopies, String isbn) {
        super(title, id, publishYear, category);
        setIsbn(isbn);
        this.author = author;
        this.pageCount = pageCount;
        this.availableCopies = availableCopies;
    }

    @Override
    protected boolean lend() {
        if (availableCopies > 0){
            availableCopies--;
            return true;
        }
        return false;
    }

    public String getAuthor() {
        return author;
    }

    public int getPageCount() {
        return pageCount;
    }

    public long getAvailableCopies() {
        return availableCopies;
    }

    public String getIsbn() {
        return isbn;
    }
}
