package ir.ac.kntu.modules;

import java.time.LocalDate;

public class Transaction {
    private double amount;
    private TransactionType type;
    private LocalDate issueDate;
    private String description;

    public Transaction(double amount, TransactionType type){
        this.amount = amount;
        this.type = type;
        this.issueDate = LocalDate.now();
    }
}
