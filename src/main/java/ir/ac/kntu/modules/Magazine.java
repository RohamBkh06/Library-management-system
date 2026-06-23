package ir.ac.kntu.modules;

import ir.ac.kntu.util.Validator;

public class Magazine extends LibraryItem{
    private long availableCopies;
    private String issn;
    private PublicationFrequency frequency;

    public void setIssn(String issn) {
        if (!Validator.isValidISSN(issn)){
            throw new IllegalArgumentException("Invalid ISSN");
        }
        this.issn = issn;
    }

    public Magazine(String title, String id, int publishYear, String category, long availableCopies, String issn, PublicationFrequency frequency) {
        super(title, id, publishYear, category);
        setIssn(issn);
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

    public long getAvailableCopies() {
        return availableCopies;
    }

    public String getIssn() {
        return issn;
    }

    public PublicationFrequency getFrequency() {
        return frequency;
    }
}
