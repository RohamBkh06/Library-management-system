package ir.ac.kntu.modules;

import ir.ac.kntu.util.Validator;

public abstract class LibraryItem {
    private String title;
    private String id;
    private int publishYear;
    private String category;

    protected abstract boolean lend();

    public String getId() {
        return id;
    }

    public String getTitle(){
        return title;
    }

    protected LibraryItem(String title, String id, int publishYear, String category){
        setId(id);
        setPublishYear(publishYear);
        this.title = title;
        this.category = category;
    }

    public void setId(String id) {
        if (!Validator.isValidItemId(id)){
            throw new IllegalArgumentException("Invalid Id");
        }
        this.id = id;
    }

    public void setPublishYear(int publishYear) {
        if (!Validator.isValidPublishYear(publishYear)){
            throw new IllegalArgumentException("PublishYear out of bound");
        }
        this.publishYear = publishYear;
    }
}
