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
        this.paid = false;
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

    public void pay(){
        if (this.isPaid()){
            throw new IllegalStateException("fine already paid");
        }
        this.paid = true;
        this.paymentDate = LocalDate.now();
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }
}
