package ir.ac.kntu.util;

import ir.ac.kntu.modules.Borrowed;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class FineCalculator {

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
            ans = SystemProperties.getBaseFineRate();
        }
        if (delay > 7) {
            ans += (delay - 7) * SystemProperties.getDailyFineRate();
        }

        return ans;
    }
}
