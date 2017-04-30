package it.polito.group05.group05.Utility.EventClasses;

/**
 * Created by Marco on 13/04/2017.
 */

public class SelectionChangedEvent {
    boolean isValid = false;

    public SelectionChangedEvent(boolean valid) {
        this.isValid = valid;
    }

    public boolean isValid() {
        return isValid;
    }
}
