package ir.ac.kntu.util;

import ir.ac.kntu.modules.Admin;

import java.io.Serializable;

public final class SystemProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    private static int baseBorrowTime = 14;
    private static int reserveLimit=4;
    private static int reserveExpireDays=3;
    private static double baseFineRate=50_000;
    private static double dailyFineRate =10_000;

    private static SystemProperties instance;

    private SystemProperties(){}

    public static SystemProperties getInstance(){
        if (instance == null){
            instance = new SystemProperties();
        }
        return instance;
    }

    public static void setInstance(SystemProperties systemProperties){
        instance = systemProperties;
    }

    public int getBaseBorrowTime() {
        return baseBorrowTime;
    }

    public int getReserveLimit() {
        return reserveLimit;
    }

    public int getReserveExpireDays() {
        return reserveExpireDays;
    }

    public double getBaseFineRate() {
        return baseFineRate;
    }

    public double getDailyFineRate() {
        return dailyFineRate;
    }

    public void setBaseBorrowTime(Admin admin, int baseBorrowTime) {
        SystemProperties.getInstance().baseBorrowTime = baseBorrowTime;
    }

    public void setReserveLimit(Admin admin, int reserveLimit) {
        SystemProperties.getInstance().reserveLimit = reserveLimit;
    }

    public void setReserveExpireDays(Admin admin, int reserveExpireDays) {
        SystemProperties.getInstance().reserveExpireDays = reserveExpireDays;
    }

    public void setBaseFineRate(Admin admin, double baseFineRate) {
        SystemProperties.getInstance().baseFineRate = baseFineRate;
    }

    public void setDailyFineRate(Admin admin, double dailyFineRate) {
        SystemProperties.getInstance().dailyFineRate = dailyFineRate;
    }
}

