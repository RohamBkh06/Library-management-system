package ir.ac.kntu.modules;

public class Ebook extends DigitalResource{
    public Ebook(String title, String id, int publishYear, String category, DigitalFormat format, double size, String url, int pageCount) {
        super(title, id, publishYear, category, format, size, url, pageCount);
    }
}
