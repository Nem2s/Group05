package it.polito.group05.group05;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.group05.group05.Utility.AnimUtils;
import it.polito.group05.group05.Utility.BaseClasses.Balance;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.GroupColor;
import it.polito.group05.group05.Utility.ColorUtils;
import it.polito.group05.group05.Utility.PicassoRoundTransform;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.UserAdapter;

public class GroupDetailsActivity extends AppCompatActivity {

    final Group currentGroup = Singleton.getInstance().getmCurrentGroup();
    private static final String TAG = GroupDetailsActivity.class.getSimpleName();
    private TextView partecipants;
    private TextView tv_chart;
    private FloatingActionButton fab;
    private BarChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_header);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        chart = (BarChart)findViewById(R.id.group_chart);
        tv_chart = (TextView)findViewById(R.id.tv_chart);
        partecipants = (TextView)findViewById(R.id.tv_partecipants);
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv_group_members);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        customizeToolbar(toolbar);
        setSupportActionBar(toolbar);
        AnimUtils.toggleOn(fab, 500, this);

        ArrayList<User> users = new ArrayList<>();
        UserAdapter adapter = new UserAdapter(users, this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        users.addAll(currentGroup.getMembers());
        populateChart(this, users);
    }

    private void populateChart(Context context, ArrayList<User> users) {
        YAxis yAxisL = chart.getAxisLeft();
        chart.getDescription().setEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);
        yAxisL.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxisL.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                DecimalFormat price = new DecimalFormat("#.##");
                return price.format(value) + " €";
            }
        });
        YAxis yAxisR = chart.getAxisRight();
        yAxisR.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                DecimalFormat price = new DecimalFormat("#.##");
                return price.format(value) + " €";
            }
        });
        //yAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day


        Legend legend = chart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(21f);
        legend.setFormLineWidth(8f);
        legend.setTextSize(13f);
        legend.setWordWrapEnabled(true);
        legend.setXEntrySpace(22f);
        legend.setEnabled(true);

        List<LegendEntry> legends = new ArrayList<>();

        int i = 0;
        List<IBarDataSet> total = new ArrayList<>();
        final ArrayList<String> xvalues = new ArrayList<>();
        for (User u : users) {
            List<BarEntry> barEntries = new ArrayList<>();
            barEntries.add(new BarEntry(i, u.getTot_expenses()));
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
        chart.animateXY(750, 1500, Easing.EasingOption.EaseInBounce, Easing.EasingOption.EaseInExpo);
        chart.setVisibleXRangeMaximum(8);


    }

    @Override
    public void onBackPressed() {
        AnimUtils.toggleOff(fab, 250, this);
        super.onBackPressed();
    }

    private void customizeToolbar(final Toolbar toolbar) {
        final CircularImageView c = (CircularImageView)findViewById(R.id.iv_group_image);
        final GroupColor gc = new GroupColor();
            Picasso
                    .with(getApplicationContext())
                    .load(Integer.parseInt(currentGroup.getGroupProfile()))
                    .transform(new PicassoRoundTransform())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            ColorUtils.PaletteBuilder builder = new ColorUtils.PaletteBuilder();
                            builder
                                    .load(bitmap)
                                    .set(toolbar)
                                    .set(fab)
                                    .anim()
                                    .method(ColorUtils.type.VIBRANT)
                                    .brighter(ColorUtils.bright.LIGHT)
                                    .build();
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
