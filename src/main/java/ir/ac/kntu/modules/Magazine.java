package ir.ac.kntu.modules;

import ir.ac.kntu.util.Validator;

public class Magazine extends LibraryItem{
    private long availableCopies;
    private String ISSN;
    private PublicationFrequency frequency;

    public void setISSN(String ISSN) {
        if (!Validator.isValidISSN(ISSN)){
            throw new IllegalArgumentException("Invalid ISSN");
        }
        this.ISSN = ISSN;
    }

    public Magazine(String title, String id, int publishYear, String category, long availableCopies, String ISSN, PublicationFrequency frequency) {
        super(title, id, publishYear, category);
        setISSN(ISSN);
        this.availableCopies = availableCopies;
        this.frequency = frequency;
    }

    @Override
    protected boolean lend() {
        if (availableCopies > 0){
            availableCopies--;
            return true;
        }
        return false;
    }
}
