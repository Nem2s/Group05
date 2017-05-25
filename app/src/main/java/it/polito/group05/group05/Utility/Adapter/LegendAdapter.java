package it.polito.group05.group05.Utility.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.Holder.LegendHolder;

/**
 * Created by Marco on 23/05/2017.
 */

public class LegendAdapter extends RecyclerView.Adapter<LegendHolder> {


    List<PieEntry> groups;
    List<Integer> colors;
    Context context;

    public LegendAdapter(List<PieEntry> groups, List<Integer> colors, Context context) {
        this.groups = groups;
        this.colors = colors;
        this.context = context;
    }

    @Override
    public LegendHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_legend,
                parent, false);
        return new LegendHolder((view));
    }

    @Override
    public void onBindViewHolder(LegendHolder holder, int position) {
        holder.setData(groups.get(position).getData(), colors.get(position), context);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }


    public void updateEntries(List<PieEntry> entries) {
        List<PieEntry> tmp = new ArrayList<>(groups);
        groups.removeAll(entries);
        int i = groups.size();
        if (i > 0) {
            groups.addAll(tmp);
        }
        notifyItemRangeInserted(0, i);
    }
}
