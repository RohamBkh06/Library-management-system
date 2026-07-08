package ir.ac.kntu.util;

import ir.ac.kntu.modules.Admin;

import java.io.Serializable;

public final class SystemProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    private int baseBorrowTime = 14;
    private int reserveLimit=4;
    private int reserveExpireDays=3;
    private double baseFineRate=50_000;
    private double dailyFineRate =10_000;

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
        SystemProperties.baseBorrowTime = baseBorrowTime;
    }

    public void setReserveLimit(Admin admin, int reserveLimit) {
        SystemProperties.reserveLimit = reserveLimit;
    }

    public void setReserveExpireDays(Admin admin, int reserveExpireDays) {
        SystemProperties.reserveExpireDays = reserveExpireDays;
    }

    public void setBaseFineRate(Admin admin, double baseFineRate) {
        SystemProperties.baseFineRate = baseFineRate;
    }

    public void setDailyFineRate(Admin admin, double dailyFineRate) {
        SystemProperties.dailyFineRate = dailyFineRate;
    }
}

