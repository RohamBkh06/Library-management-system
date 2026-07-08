package ir.ac.kntu.modules;

import ir.ac.kntu.util.Validator;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Queue;

public abstract class LibraryItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String id;
    private int publishYear;
    private String category;
    private Queue<Reservation> waitingQueue;

    protected abstract boolean lend();

    public abstract void returnBack();

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
        this.waitingQueue = new ArrayDeque<>();
    }

    public Reservation getActiveReservation(){
        return this.waitingQueue.peek();
    }

    public void addToAQueue(Reservation reservation){
        this.waitingQueue.add(reservation);
    }

    public void removeFromQueue(Reservation reservation){
        this.waitingQueue.remove(reservation);
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

    public int getPublishYear() {
        return publishYear;
    }

    public String getCategory() {
        return category;
    }



    @Override
    public String toString() {
        return  title + " {" +
                "id= " + id +
                ", publishYear= " + publishYear +
                ", category= " + category +
                '}';
    }
}
