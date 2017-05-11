package it.polito.group05.group05.Utility.Event;

import android.content.Context;

/**
 * Created by Marco on 05/05/2017.
 */

public class SelectionChangedEvent {
    private static int selectionIsValid = 0;
    private static boolean textIsValid = false;

    private SelectionChangedEvent() {}

    public static synchronized SelectionChangedEvent onSelectionChangedEvent(boolean valid) {
        if(valid)
            selectionIsValid++;
        else
            selectionIsValid--;
        return new SelectionChangedEvent();
    }
    public static synchronized SelectionChangedEvent onTextChangedEvent(boolean valid) {
        if(valid)
            textIsValid = true;
        else
            textIsValid = false;
        return new SelectionChangedEvent();
    }
    public static boolean isValid() {
        return selectionIsValid > 0 && textIsValid;
    }
    public static boolean isValidForNewMember() {
        return selectionIsValid > 0 ;
    }
    public static void getValues(Context context) {
        //Toast.makeText(context, "selection: " + selectionIsValid + " text: " + textIsValid, Toast.LENGTH_SHORT).show();
    }
    public static void resetValues() {
        selectionIsValid = 0; textIsValid = false;
    }
}
