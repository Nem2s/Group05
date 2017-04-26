package it.polito.group05.group05.Utility.EventClasses;

import it.polito.group05.group05.Utility.BaseClasses.User;

/**
 * Created by Marco on 13/04/2017.
 */

public class PartecipantsNumberChangedEvent {
    int n;
    User u;
    int position;
    ExpenseDividerEvent e;

    public PartecipantsNumberChangedEvent(User currentUser, int i, int position, ExpenseDividerEvent event) {
        u = currentUser;
        this.n += i;
        this.position = position;
        this.e = event;
    }

    public int getN() {
        return n;
    }

    public User getUser() {
        return u;
    }

    public int getPosition() {
        return position;
    }

    public boolean hasExpenseEvent() {
        return e != null;
    }

    public ExpenseDividerEvent getEvent() {
        return e;
    }
}
