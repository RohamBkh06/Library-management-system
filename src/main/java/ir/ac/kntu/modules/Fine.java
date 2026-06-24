package ir.ac.kntu.modules;

import ir.ac.kntu.util.FineCalculator;
import ir.ac.kntu.util.IdGenerator;
import java.time.LocalDate;


public class Fine {
    private final String id;
    private final Borrowed borrowedItem;
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
        return this.getAmount() == 0 || this.getPaymentDate() != null;
    }

    public void pay(){
        if (this.isPaid()){
            throw new IllegalStateException("fine already paid");
        }
        this.paymentDate = LocalDate.now();
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    @Override
    public String toString() {
        return "Fine{ " +
                "id= " + id +
                ", borrowedItem= " + borrowedItem +
                ", is paid= " + ((this.getPaymentDate() == null) ? "No" : "Yes") +
                ", paymentDate= " + paymentDate +
                " }";
    }
}
