package it.polito.group05.group05.Utility.EventClasses;

/**
 * Created by Marco on 13/04/2017.
 */

public class TextChangedEvent {
    boolean valid = false;

    public TextChangedEvent(boolean b) {
        valid = b;
    };
    public boolean isValid() {
        return valid;
    }



}
