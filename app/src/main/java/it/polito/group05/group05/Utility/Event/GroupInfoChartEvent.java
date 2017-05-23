package it.polito.group05.group05.Utility.Event;

import com.github.mikephil.charting.data.PieEntry;

import java.util.List;

/**
 * Created by Marco on 12/05/2017.
 */

public class GroupInfoChartEvent {

    private final List<PieEntry> entries;

    public GroupInfoChartEvent(List<PieEntry> e) {
        this.entries = e;
    }

    public List<PieEntry> getEntry() {
        return entries;
    }
}
