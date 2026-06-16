package ir.ac.kntu.modules;

import ir.ac.kntu.util.Validator;

public abstract class LibraryItem {
    private String title;
    private String id;
    private int publishYear;
    private String category;

    public String getId() {
        return id;
    }

    protected LibraryItem(String title, String id, int publishYear, String category){
        if (Validator.isValidItemId(id)){
            this.id = id;
        }
        if (Validator.isValidPublishYear(publishYear)){
            this.publishYear = publishYear;
        }
        this.title = title;
        this.category = category;
    }

}
