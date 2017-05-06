package it.polito.group05.group05.Utility.Event;

import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;

/**
 * Created by user on 05/05/2017.
 */

public class PartecipantsNumberChangedEvent {
    int n=0;
    User_expense u;
    int position;
    ExpenseDividerEvent e;

    public PartecipantsNumberChangedEvent(User_expense currentUser, int i, int position, ExpenseDividerEvent event) {
        u = currentUser;
        this.n += i;
        this.position = position;
        this.e = event;
    }

    public int getN() {
        return n;
    }

    public User_expense getUser() {
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
