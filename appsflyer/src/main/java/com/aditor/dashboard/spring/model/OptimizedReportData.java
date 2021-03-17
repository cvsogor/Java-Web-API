package com.aditor.dashboard.spring.model;

/**
 * Created by dgordiichuk on 23.12.2015.
 */
public class OptimizedReportData {
    private String groupByFild;
    private String offerName;
    private double revenue;
    private double cost;
    private double impressions;
    private double clicks;
    private double ctr;
    private double installs;
    private double ecpi;
    private double fdCount;
    private double fdAmount;
    private double depositsCount;
    private double depositsAmount;
    private double instalsDepositAmount;
    private double ecpa;
    private double instalToRfd;
    private double arpu;
    private double arppu;
    private double cpc;
    private double roi;
    private double cti;
    private double ctd;
    private double ecpm;

    public OptimizedReportData(String groupByFild, String offerName, double revenue, double cost, double impressions, double clicks, double ctr, double installs, double ecpi, double fdCount, double fdAmount, double depositsCount, double depositsAmount, double instalsDepositAmount, double ecpa, double instalToRfd, double arpu, double arppu, double cpc, double roi, double cti, double ctd, double ecpm) {
        this.groupByFild = groupByFild;
        this.offerName = offerName;
        this.revenue = revenue;
        this.cost = cost;
        this.impressions = impressions;
        this.clicks = clicks;
        this.ctr = ctr;
        this.installs = installs;
        this.ecpi = ecpi;
        this.fdCount = fdCount;
        this.fdAmount = fdAmount;
        this.depositsCount = depositsCount;
        this.depositsAmount = depositsAmount;
        this.instalsDepositAmount = instalsDepositAmount;
        this.ecpa = ecpa;
        this.instalToRfd = instalToRfd;
        this.arpu = arpu;
        this.arppu = arppu;
        this.cpc = cpc;
        this.roi = roi;
        this.cti = cti;
        this.ctd = ctd;
        this.ecpm = ecpm;
    }

    public String toCSV() {
        return groupByFild +","+ offerName +","+ revenue +","+ cost +","+ impressions +","+ clicks +","+ ctr +","+ installs +","+ ecpi +","+ fdCount +","+ fdAmount +","+ depositsCount +","+ depositsAmount +","+ instalsDepositAmount +","+ ecpa +","+ instalToRfd +","+ arpu +","+ arppu +","+ cpc +","+ roi +","+ cti +","+ ctd +","+ ecpm;
    }
}
