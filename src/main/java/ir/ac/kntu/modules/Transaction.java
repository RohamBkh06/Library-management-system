package ir.ac.kntu.modules;

import java.time.LocalDate;

public class Transaction {
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
        switch (type){
            case FINE_PAYMENT:
                this.description = "Payment of fine with id: "+id;
                break;
            case PURCHASE:
                this.description = "Purchase made for loan with id: "+id;
                break;
            default:
                this.description = "Not a valid transaction";
                break;

        }
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

}
