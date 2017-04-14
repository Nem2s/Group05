package it.polito.group05.group05.Utility.EventClasses;

import java.util.List;
import java.util.Objects;

/**
 * Created by Marco on 13/04/2017.
 */

public class ObjectChangedEvent {
    Object object;

    public ObjectChangedEvent(Object o) {
        this.object = o;
    }

    public Object retriveObject() {
        if(object != null) {
            Object o = object;
            object = null;
            return o;
        }
        return null;
    }
}
