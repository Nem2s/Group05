package it.polito.group05.group05.Utility.EventClasses;

/**
 * Created by Marco on 13/04/2017.
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
