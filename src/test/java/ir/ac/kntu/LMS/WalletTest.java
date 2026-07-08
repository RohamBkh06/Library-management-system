package ir.ac.kntu.LMS;


import ir.ac.kntu.modules.Wallet;
import ir.ac.kntu.modules.Transaction;
import ir.ac.kntu.modules.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    private Wallet wallet;

    @BeforeEach
    void setup() {
        wallet = new Wallet();
    }

    @Test
    void initialBalanceShouldBeZero() {
        assertEquals(0, wallet.getBalance());
    }

    @Test
    void chargeShouldIncreaseBalance() {

        wallet.charge(500);

        assertEquals(500, wallet.getBalance());

    }

    @Test
    void withdrawShouldDecreaseBalance() {

        wallet.charge(1000);

        wallet.withdraw(250);

        assertEquals(750, wallet.getBalance());

    }

    @Test
    void withdrawWholeBalanceShouldLeaveZero() {

        wallet.charge(500);

        wallet.withdraw(500);

        assertEquals(0, wallet.getBalance());

    }

    @Test
    void withdrawMoreThanBalanceShouldThrowException() {

        wallet.charge(100);

        assertThrows(
                IllegalStateException.class,
                () -> wallet.withdraw(101)
        );

    }

    @Test
    void addTransactionShouldStoreTransaction() {

        Transaction transaction =
                new Transaction(500, TransactionType.CHARGE);

        wallet.addTransaction(transaction);

        assertEquals(1, wallet.getTransactions().size());

    }

    @Test
    void getTransactionsShouldReturnCopy() {

        wallet.addTransaction(
                new Transaction(100, TransactionType.CHARGE));

        var list = wallet.getTransactions();

        list.clear();

        assertEquals(1, wallet.getTransactions().size());

    }

    @Test
    void getTransactionsFromDateShouldReturnMatchingTransactions() {

        Transaction t =
                new Transaction(100, TransactionType.CHARGE);

        wallet.addTransaction(t);

        assertEquals(
                1,
                wallet.getTransactions(LocalDate.now()).size()
        );

    }

    @Test
    void getTransactionsBetweenDatesShouldReturnMatchingTransactions() {

        wallet.addTransaction(
                new Transaction(100, TransactionType.CHARGE));

        assertEquals(
                1,
                wallet.getTransactions(
                        LocalDate.now().minusDays(1),
                        LocalDate.now().plusDays(1)
                ).size()
        );

    }

}