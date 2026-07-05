package ir.ac.kntu.util;

import ir.ac.kntu.modules.Admin;

public final class SystemProperties {
    private static int baseBorrowTime = 14;
    private static int reserveLimit=4;
    private static int reserveExpireDays=3;
    private static double baseFineRate=50_000;
    private static double dailyFineRate =10_000;

    public static int getBaseBorrowTime() {
        return baseBorrowTime;
    }

    public static int getReserveLimit() {
        return reserveLimit;
    }

    public static int getReserveExpireDays() {
        return reserveExpireDays;
    }

    public static double getBaseFineRate() {
        return baseFineRate;
    }

    public static double getDailyFineRate() {
        return dailyFineRate;
    }

    public static void setBaseBorrowTime(Admin admin, int baseBorrowTime) {
        SystemProperties.baseBorrowTime = baseBorrowTime;
    }

    public static void setReserveLimit(Admin admin, int reserveLimit) {
        SystemProperties.reserveLimit = reserveLimit;
    }

    public static void setReserveExpireDays(Admin admin, int reserveExpireDays) {
        SystemProperties.reserveExpireDays = reserveExpireDays;
    }

    public static void setBaseFineRate(Admin admin, double baseFineRate) {
        SystemProperties.baseFineRate = baseFineRate;
    }

    public static void setDailyFineRate(Admin admin, double dailyFineRate) {
        SystemProperties.dailyFineRate = dailyFineRate;
    }
}

