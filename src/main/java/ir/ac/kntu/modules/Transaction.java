package ir.ac.kntu.modules;

import java.io.Serializable;
import java.time.LocalDate;

public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    private double amount;
    private TransactionType type;
    private LocalDate issueDate;
    private String description;

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public Transaction(double amount, TransactionType type, String id){
        this.amount = amount;
        this.type = type;
        this.issueDate = LocalDate.now();
        this.description = "Payment of fine with id: " + id;
    }

    public Transaction(double amount, TransactionType type){
        this.amount = amount;
        this.type = type;
        this.issueDate = LocalDate.now();
        this.description = "Wallet charged by the amount: " + amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "amount=" + amount +
                ", issueDate=" + issueDate +
                ", description='" + description +
                '}';
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
