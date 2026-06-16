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
        long delay = ChronoUnit.DAYS.between(borrowed.getReturnDueDate(), LocalDate.now());
        if (delay > 0) {
            ans = BASERATE;
        }
        if (delay > 7) {
            ans += (delay - 7) * DAILYFINE;
        }

        return ans;
    }
}
