package ir.ac.kntu.Javafx;

import java.time.LocalDate;

public class DebtorRow {


    private final String userId;
    private final String userName;
    private final int fineCount;
    private final long totalDebt;
    private final LocalDate lastFineDate;

    public DebtorRow(
            String userId,
            String userName,
            int fineCount,
            long totalDebt,
            LocalDate lastFineDate) {

        this.userId = userId;
        this.userName = userName;
        this.fineCount = fineCount;
        this.totalDebt = totalDebt;
        this.lastFineDate = lastFineDate;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getFineCount() {
        return fineCount;
    }

    public long getTotalDebt() {
        return totalDebt;
    }

    public LocalDate getLastFineDate() {
        return lastFineDate;
    }

}
