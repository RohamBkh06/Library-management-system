package ir.ac.kntu.modules;

public class AudioBook extends DigitalResource{
    public AudioBook(String title, String id, int publishYear, String category, DigitalFormat format, double size, String url, int pageCount) {
        super(title, id, publishYear, category, format, size, url, pageCount);
    }

    @Override
    public String toString() {
        return super.toString() + "Type: Audio Book";
    }
}
