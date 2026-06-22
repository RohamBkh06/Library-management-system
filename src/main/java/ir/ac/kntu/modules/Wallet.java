package ir.ac.kntu.modules;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private double balance;
    private List<Transaction> transactions;

    public Wallet(){
        this.balance = 0;
        this.transactions = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void charge(double amount){
        this.balance += amount;
    }

    public void withdraw(double amount){
        if (amount > this.balance){
            throw new IllegalStateException("Insufficient balance");
        }
        this.balance -= amount;
    }

    public void addTransaction(Transaction transaction){
        this.transactions.add(transaction);
    }

    public List<Transaction> getTransactions(){
        return new ArrayList<>(transactions);
    }

    public List<Transaction> getTransactions(LocalDate from){
        ArrayList<Transaction> sorted = new ArrayList<>();
        for (Transaction transaction : this.transactions) {
            if (!transaction.getIssueDate().isBefore(from)){
                sorted.add(transaction);
            }
        }
        return sorted;
    }

    public List<Transaction> getTransactions(LocalDate from, LocalDate to){
        ArrayList<Transaction> sorted = new ArrayList<>();
        for (Transaction transaction : this.transactions) {
            if (!transaction.getIssueDate().isBefore(from) && !transaction.getIssueDate().isAfter(to)){
                sorted.add(transaction);
            }
        }
        return sorted;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "balance=" + balance +
                '}';
    }
}
