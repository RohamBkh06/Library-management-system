package ir.ac.kntu.modules;

import java.io.Serializable;

public class AudioBook extends DigitalResource implements Serializable {

    private static final long serialVersionUID = 1L;

    public AudioBook(String title, String id, int publishYear, String category, DigitalFormat format, double size, String url, int pageCount) {
        super(title, id, publishYear, category, format, size, url, pageCount);
    }

    @Override
    public String toString() {
        return super.toString() + "Type: Audio Book";
    }
}
