package ir.ac.kntu.modules;

import ir.ac.kntu.util.IdGenerator;

import java.time.LocalDate;

public class Borrowed {
    private NormalUser borrower;
    private String id;
    private LibraryItem item;
    private LocalDate borrowDate;
    private LocalDate returnDueDate;
    private boolean isReturned;
    private Fine fine;

    public String getId() {
        return id;
    }

    public LibraryItem getItem() {
        return item;
    }

    public void returnBorrow(){
        this.isReturned = true;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public Borrowed(LibraryItem item, NormalUser borrower) {
        this.item = item;
        this.borrower = borrower;
        this.id = IdGenerator.generateBorrowedId();
        this.borrowDate = LocalDate.now();
        this.returnDueDate = this.borrowDate.plusDays(14L);
        this.isReturned = false;
        this.fine = new Fine(this);
    }

    public Fine getFine() {
        return fine;
    }

    public boolean extendBorrowTime(long daysToExtend){
        if (fine.isPaid()){
            borrowDate.plusDays(daysToExtend);
            return true;
        } else{
            return false;
        }
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDueDate() {
        return returnDueDate;
    }

    @Override
    public String toString() {
        return "Borrowed{" +
                "borrower=" + borrower.getFirstName() +
                ", id='" + id +
                ", item=" + item +
                ", borrowDate=" + borrowDate +
                ", returnDueDate=" + returnDueDate +
                ", isReturned=" + isReturned +
                ", fine=" + fine.getAmount() +
                '}';
    }
}
