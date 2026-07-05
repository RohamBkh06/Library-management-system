package ir.ac.kntu.modules;

import ir.ac.kntu.util.IdGenerator;
import ir.ac.kntu.util.SystemProperties;

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
        this.item.returnBack();
    }

    public boolean isReturned() {
        return isReturned;
    }

    public Borrowed(LibraryItem item, NormalUser borrower) {
        this.item = item;
        this.borrower = borrower;
        this.id = IdGenerator.generateBorrowedId();
        this.borrowDate = LocalDate.now();
        this.returnDueDate = this.borrowDate.plusDays(SystemProperties.getBaseBorrowTime());
        this.isReturned = false;
        this.fine = new Fine(this);
    }

    public Fine getFine() {
        return fine;
    }

    public void extendBorrowTime(long daysToExtend){
        if (!fine.isPaid()){
            throw new IllegalStateException("This borrow record has unpaid fine.");
        }
        returnDueDate.plusDays(daysToExtend);
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
                "borrower= " + borrower.getFirstName() +
                ", id= " + id +
                "\n, item= " + item +
                "\n, borrowDate= " + borrowDate +
                ", returnDueDate= " + returnDueDate +
                ", isReturned= " + isReturned +
                ((fine.isPaid()) ? ", fine= " + fine.getAmount() : "") +
                '}';
    }
}
