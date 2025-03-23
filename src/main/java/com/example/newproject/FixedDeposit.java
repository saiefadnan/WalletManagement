package com.example.newproject;


import java.time.LocalDate;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class FixedDeposit {
    public String dpst_BankName;
    public double Saving_amnt;
    public double Invested;
    public double Maturity_val;
    public double Earned_Interest;
    public LocalDate Init_date;
    public LocalDate Final_date;
    public ScheduledExecutorService Notification_checker;
    public ScheduledFuture<?> scheduledTask;
    public boolean notify;
    public double Comp_freq;
    public double Maturity_unit;
    public double Maturity_duration;
    public double interest;

    public FixedDeposit(){

    }

    public FixedDeposit(String bankName, double depositAmount, double investedAmount, double maturityValue, double earnedInterest, LocalDate parse, LocalDate parse1, boolean notify, double compFreq, double maturityUnit, double maturityDuration, double interest) {
        this.dpst_BankName = bankName;
        this.Saving_amnt = depositAmount;
        this.Invested = investedAmount;
        this.Maturity_val = maturityValue;
        this.Earned_Interest = earnedInterest;
        this.Init_date = parse;
        this.Final_date = parse1;
        this.notify = notify;
        this.Comp_freq = compFreq;
        this.Maturity_unit = maturityUnit;
        this.Maturity_duration = maturityDuration;
        this.interest = interest;
    }
}
