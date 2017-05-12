package it.polito.group05.group05;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.polito.group05.group05.Utility.BaseClasses.ChartGroupMarker;
import it.polito.group05.group05.Utility.BaseClasses.ChartUserMarker;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;

public class UserBalanceActivity extends AppCompatActivity {

    PieChart pchart;
    MarkerView marker;
    private Entry entry;
    private Highlight highlighted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_balance);
        if(Singleton.getInstance().getCurrentUser().getUserGroups().size() == 0) {
            Snackbar.make(findViewById(R.id.parent_layout), "You're not in any groups, Try to create one!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(UserBalanceActivity.this, NewGroupActivity.class));
                            finish();
                        }
                    })
                    .show();
        }
        pchart = (PieChart)findViewById(R.id.user_piechart);
        marker = new ChartGroupMarker(this, R.layout.item_chart_user_list);
        setupPieChart();
    }

    private void setupPieChart() {
        List<PieEntry> entries = new ArrayList<>();
        for(Object o : Singleton.getInstance().getCurrentUser().getUserGroups().values()) {
            GroupDatabase g = (GroupDatabase)o;
            float value;
            try {
                value = Float.valueOf(String.valueOf((long)g.getMembers().get(Singleton.getInstance().getCurrentUser().getId())));

            } catch (ClassCastException e) {
                value = Float.valueOf(String.valueOf((double)g.getMembers().get(Singleton.getInstance().getCurrentUser().getId())));
            }
            if(value < 0)
                value = -value;
            PieEntry entry = new PieEntry(value, g.getName());
            entry.setData(g);
            entries.add(entry);
        }
        PieDataSet set = new PieDataSet(entries, null);
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setDrawIcons(false);

        set.setSliceSpace(3f);
        set.setIconsOffset(new MPPointF(0, 40));
        set.setSelectionShift(15f);

        PieData data = new PieData(set);
        data.setValueFormatter(new LargeValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        Legend l = pchart.getLegend();
        l.setTextSize(15f);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        pchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                IPieDataSet set;
                pchart.setMarker(marker);
                set = pchart.getData().getDataSetForEntry(e);
                set.setDrawIcons(true);
                entry = e;
                highlighted = h;
                pchart.invalidate();
            }

            @Override
            public void onNothingSelected() {
                onValueSelected(entry, highlighted);
            }
        });
        pchart.setData(data);
        pchart.animateXY(1000, 1000, Easing.EasingOption.EaseInBounce, Easing.EasingOption.EaseInExpo);
        pchart.invalidate();



    }
}
