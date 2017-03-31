package it.polito.group05.group05;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.group05.group05.Utility.AnimUtils;
import it.polito.group05.group05.Utility.BaseClasses.Balance;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.PicassoRoundTransform;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.UserAdapter;

public class GroupDetailsActivity extends AppCompatActivity {

    final Group currentGroup = Singleton.getInstance().getmCurrentGroup();
    private static final String TAG = GroupDetailsActivity.class.getSimpleName();
    private TextView partecipants;
    private FloatingActionButton fab;
    private BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_header);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        chart = (BarChart)findViewById(R.id.group_chart);
        customizeToolbar(toolbar);
        setSupportActionBar(toolbar);
        AnimUtils.toggleOn(fab, 750, this);

        partecipants = (TextView)findViewById(R.id.tv_partecipants);
        ArrayList<User> users = new ArrayList<>();
        UserAdapter adapter = new UserAdapter(this, users);
        ListView listView = (ListView)findViewById(R.id.lv_group_members);
        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        chart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        listView.setAdapter(adapter);
        adapter.addAll(currentGroup.getMembers());

        populateChart(this, users);
    }

    private void populateChart(Context context, ArrayList<User> users) {
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //yAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day


        Legend legend = chart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setFormSize(9f);
        legend.setTextSize(11f);
        legend.setWordWrapEnabled(true);
        legend.setXEntrySpace(4f);
        legend.setEnabled(true);

        List<LegendEntry> legends = new ArrayList<>();

        int i = 0;
        List<IBarDataSet> total = new ArrayList<>();
        final ArrayList<String> xvalues = new ArrayList<>();
        for (User u : users) {
            List<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(new BarEntry(i,(float)u.getBalance().getCredit()));
            xvalues.add(u.getUser_name());
            LegendEntry l = new LegendEntry();
            l.form = Legend.LegendForm.LINE;
            l.label = u.getUser_name();
            l.formColor = u.getUser_color();
            i++;
            BarDataSet set = new BarDataSet(barEntries, "user " + i);
            set.setColor(u.getUser_color());
            total.add(set);
            legends.add(l);
        }
        legend.setCustom(legends);
        BarData data = new BarData(total);
        data.setBarWidth(0.9f);
        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xvalues.get((int)value);
            }
        };
        xAxis.setValueFormatter(formatter);
        chart.setData(data);
        chart.setFitBars(true);
        chart.animateXY(1250, 3000, Easing.EasingOption.EaseInBounce, Easing.EasingOption.EaseInExpo);


    }

    @Override
    public void onBackPressed() {
        AnimUtils.toggleOff(fab, 250, this);
        super.onBackPressed();
    }

    private void customizeToolbar(final Toolbar toolbar) {
        final CircularImageView c = (CircularImageView)findViewById(R.id.iv_group_image);
        final Palette.PaletteAsyncListener paletteListener = new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                int toolbar_default = getResources().getColor(R.color.colorPrimary);
                int fab_default = getResources().getColor(R.color.colorAccent);
                final int _text = 0x000000;
                final int color = palette.getLightVibrantColor(toolbar_default);
                final int fab_color = palette.getMutedColor(fab_default);
                Palette.Swatch tx_swatch = palette.getVibrantSwatch();
                int tx_color = getResources().getColor(R.color.colorPrimaryText);
                if (tx_swatch != null) {
                    tx_color = tx_swatch.getBodyTextColor();
                }
                ValueAnimator colorAnimation_toolbar = ValueAnimator.ofObject(new ArgbEvaluator(), toolbar_default, color);
                colorAnimation_toolbar.setDuration(250); // milliseconds
                colorAnimation_toolbar.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        toolbar.setBackgroundColor(color);
                        toolbar.setTitleTextColor(_text);

                    }

                });
                ValueAnimator colorAnimation_fab = ValueAnimator.ofObject(new ArgbEvaluator(), toolbar_default, color);
                colorAnimation_fab.setDuration(250); // milliseconds
                colorAnimation_fab.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        fab.setBackgroundColor(fab_color);
                    }
                });

                if(color != toolbar_default) {
                    colorAnimation_toolbar.start();
                    partecipants.setTextColor(tx_color);
                }
                if(fab_color != fab_default)
                    colorAnimation_fab.start();

            }
        };
        Picasso
                .with(getApplicationContext())
                .load(Integer.parseInt(currentGroup.getGroupProfile()))
                .transform(new PicassoRoundTransform())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Palette.from(bitmap).generate(paletteListener);
                        c.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });


    }


}
