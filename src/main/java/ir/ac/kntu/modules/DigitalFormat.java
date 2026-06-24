package ir.ac.kntu.modules;

public enum DigitalFormat {
    PDF, EPUB, MP3, AAC;

    public static DigitalFormat fromString(String input) {
        for (DigitalFormat format : values()) {
            if (format.name().equalsIgnoreCase(input)) {
                return format;
            }
        }
        throw new IllegalArgumentException("Invalid format");
    }
}
