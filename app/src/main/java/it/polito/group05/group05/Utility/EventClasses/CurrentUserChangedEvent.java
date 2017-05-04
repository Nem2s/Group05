package it.polito.group05.group05.Utility.EventClasses;

import it.polito.group05.group05.Utility.BaseClasses.User;

/**
 * Created by Marco on 26/04/2017.
 */

public class CurrentUserChangedEvent {
    User u;
    public CurrentUserChangedEvent(User u) {
        this.u = u;
    }

    public User getUser() {
        return u;
    }
}
