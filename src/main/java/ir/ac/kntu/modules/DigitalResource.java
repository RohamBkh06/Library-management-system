package ir.ac.kntu.modules;

import ir.ac.kntu.util.Validator;

public class DigitalResource extends LibraryItem {

    private DigitalFormat format;
    private double size;
    private String url;
    private boolean available;
    private int pageCount;

    public DigitalResource(String title, String id, int publishYear, String category, DigitalFormat format, double size, String url, int pageCount, boolean available) {
        super(title, id, publishYear, category);
        setUrl(url);
        this.format = format;
        this.size = size;
        this.pageCount = pageCount;
        this.available = available;
    }

    public void setUrl(String url) {
        if (!Validator.isValidUrl(url)){
            throw new IllegalArgumentException("Invalid download-URL");
        }
        this.url = url;
    }
}
