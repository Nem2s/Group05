package it.polito.group05.group05.Utility.Event;

import it.polito.group05.group05.Utility.BaseClasses.User_expense;

/**
 * Created by user on 05/05/2017.
 */

public class PriceChangedEvent {
    double price;



    public PriceChangedEvent(double price) {
        this.price = price;

    }

    public double getPrice() {
        return price;
    }

}
