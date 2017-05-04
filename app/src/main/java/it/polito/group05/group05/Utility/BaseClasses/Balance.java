package it.polito.group05.group05.Utility.BaseClasses;

/**
 * Created by Marco on 30/03/2017.
 */

public class Balance {
    private double credit;
    private double debit;

    public Balance(double credit, double debit) {
        this.credit = credit;
        this.debit = debit;
    }

    public Balance() {
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }
}
