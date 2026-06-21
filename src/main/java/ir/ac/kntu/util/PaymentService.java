package ir.ac.kntu.util;

import ir.ac.kntu.modules.*;

public final class PaymentService {

    public static void payFine(Fine fine, NormalUser user){
        user.getWallet().withdraw(fine.getAmount());
        fine.pay();
        Transaction transaction = new Transaction(fine.getAmount(), TransactionType.FINE_PAYMENT, fine.getId());
        user.getWallet().addTransaction(transaction);
        }

    public static void chargeWallet(double amount,NormalUser user){
        user.getWallet().charge(amount);
        Transaction transaction = new Transaction(amount, TransactionType.CHARGE);
        user.getWallet().addTransaction(transaction);
    }
}
