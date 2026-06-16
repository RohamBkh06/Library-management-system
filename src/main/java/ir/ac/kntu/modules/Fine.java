package ir.ac.kntu.modules;

import ir.ac.kntu.util.FineCalculator;
import ir.ac.kntu.util.IdGenerator;
import java.time.LocalDate;


public class Fine {
    private final String id;
    private final Borrowed borrowedItem;
    private boolean paid;
    private LocalDate paymentDate;

    public Fine(Borrowed borrowedItem){
        this.borrowedItem = borrowedItem;
        this.id = IdGenerator.generateFineId();
    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return FineCalculator.calculate(this.borrowedItem);
    }

    public boolean isPaid(){
        return paid;
    }

    public boolean pay(){
        if (this.isPaid()){
            return false;
        }

        this.paid = true;
        this.paymentDate = LocalDate.now();
        return true;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }
}
