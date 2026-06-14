package ir.ac.kntu.modules;

import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private NormalUser user;
    private double balance;
    private List<Transaction> transactions;

    public Wallet(NormalUser user){
        this.user = user;
        this.balance = 0;
        this.transactions = new ArrayList<>();
    }


}
