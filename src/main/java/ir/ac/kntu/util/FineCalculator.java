package ir.ac.kntu.util;

import ir.ac.kntu.modules.Borrowed;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class FineCalculator {
    private final static double BASERATE = 50_000;
    private final static double DAILYFINE = 10_000;

    private FineCalculator(){}

    public static double calculate(Borrowed borrowed) {
        double ans = 0;
        long delay;
        if (borrowed.getFine().getPaymentDate() == null){
        delay = ChronoUnit.DAYS.between(borrowed.getReturnDueDate(), LocalDate.now());
        } else {
            delay = ChronoUnit.DAYS.between(borrowed.getReturnDueDate(), borrowed.getFine().getPaymentDate());
        }
        if (delay > 0) {
            ans = BASERATE;
        }
        if (delay > 7) {
            ans += (delay - 7) * DAILYFINE;
        }

        return ans;
    }
}
