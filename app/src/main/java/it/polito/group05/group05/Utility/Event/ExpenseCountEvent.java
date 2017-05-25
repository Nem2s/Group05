package it.polito.group05.group05.Utility.Event;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;

/**
 * Created by Marco on 14/05/2017.
 */

public class ExpenseCountEvent {
    List<Entry> entries;

    public ExpenseCountEvent(List<Entry> l) {
        this.entries = l;
    }

    public List<Entry> getList() {
        return entries;
    }
}
