package ir.ac.kntu.modules;

import ir.ac.kntu.util.Validator;

import java.io.Serializable;

public class Magazine extends LibraryItem implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Override
    public void returnBack() {
        this.availableCopies++;
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

    @Override
    public String toString() {
        return super.toString()+ "\n" + "Type: Magazine, " + "Available copies: " + availableCopies;
    }
}
