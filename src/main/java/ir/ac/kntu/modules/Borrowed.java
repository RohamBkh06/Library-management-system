package ir.ac.kntu.modules;

import java.time.LocalDate;

public class Borrowed {
    private NormalUser borrower;
    private String id;
    private LibraryItem item;
    private LocalDate borrowDate;
    private LocalDate returnDueDate;
    private boolean isReturned;
    private Fine fine;

    private boolean extendBorrowTime(long daysToExtend){
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
}
