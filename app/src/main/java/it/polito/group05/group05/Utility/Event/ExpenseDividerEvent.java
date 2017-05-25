package it.polito.group05.group05.Utility.Event;

import android.support.v4.util.Pair;

/**
 * Created by user on 05/05/2017.
 */


    public class ExpenseDividerEvent {
        double totalCustom;
        int customPeople;

        public ExpenseDividerEvent(Pair<Integer, Double> p) {
            this.totalCustom = p.second;
            this.customPeople = p.first;
        }

        public int getNPeople() {
            return customPeople;
        }

        public double getTotal() {
            return totalCustom;
        }

        public void setTotal(double quote) {
            this.totalCustom = quote;
        }
    }
