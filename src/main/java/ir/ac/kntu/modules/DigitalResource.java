package ir.ac.kntu.modules;

import ir.ac.kntu.util.Validator;

public abstract class DigitalResource extends LibraryItem {

    private DigitalFormat format;
    private double size;
    private String url;
    private int pageCount;

    public DigitalResource(String title, String id, int publishYear, String category, DigitalFormat format, double size, String url, int pageCount) {
        super(title, id, publishYear, category);
        setUrl(url);
        this.format = format;
        this.size = size;
        this.pageCount = pageCount;
    }

    public void setUrl(String url) {
        if (!Validator.isValidUrl(url)) {
            throw new IllegalArgumentException("Invalid download-URL");
        }
        this.url = url;
    }

    @Override
    protected boolean lend() {
        return true;
    }

    public DigitalFormat getFormat() {
        return format;
    }

    public double getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public int getPageCount() {
        return pageCount;
    }

    @Override
    public String toString() {
        return super.toString() + "*Digital Resource* ";
    }
}