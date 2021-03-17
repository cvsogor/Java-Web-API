package com.aditor.dashboard.spring;

/**
 * Created by art on 12/2/15.
 */
public class DailyData {

    private long fds;
    private long deposits;
    private double revenue;
    private double fdsum;

    public DailyData(long fds, long deposits, double revenue, double fdsum) {
        this.fds = fds;
        this.deposits = deposits;
        this.revenue = revenue;
        this.fdsum = fdsum;
    }

    public long getFds() { return fds; }

    public long getDeposits() { return deposits;
    }

    public double getRevenue() {
        return revenue;
    }

    public double getFdsum() { return fdsum; }

    public void setFds(long fds) { this.fds = fds; }

    public void setDeposits(long deposits) {
        this.deposits = deposits;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public void setFdsum(double fdsum) { this.fdsum = fdsum; }
    }

