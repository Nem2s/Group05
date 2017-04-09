package it.polito.group05.group05;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
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
import it.polito.group05.group05.Utility.BaseClasses.ChartUserMarker;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.GroupColor;
import it.polito.group05.group05.Utility.ChartUserListAdapter;
import it.polito.group05.group05.Utility.ColorUtils;
import it.polito.group05.group05.Utility.ExpenseCardAdapter;
import it.polito.group05.group05.Utility.ImageUtils;
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
    private Toolbar toolbar;
    private TextView tv_group_name;
    private ImageView expand_cardview;
    private LinearLayout cardView_layout;
    private boolean isExpanded;
    RecyclerView rv_users_under_chart;
    Entry entry;
    Highlight highlighted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        toolbar = (Toolbar)findViewById(R.id.toolbar_header);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        chart = (BarChart)findViewById(R.id.group_chart);
        tv_chart = (TextView)findViewById(R.id.tv_chart);
        partecipants = (TextView)findViewById(R.id.tv_partecipants);
        tv_group_name = (TextView)findViewById(R.id.tv_group_name) ;
        expand_cardview = (ImageView)findViewById(R.id.expand_cardview);
        cardView_layout = (LinearLayout) findViewById(R.id.cardview_layout);
        isExpanded = false;

        final RecyclerView rv = (RecyclerView)findViewById(R.id.rv_group_members);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        customizeToolbar(toolbar);
        setSupportActionBar(toolbar);
        tv_chart.setTextColor(toolbar.getSolidColor());
        partecipants.setTextColor(toolbar.getSolidColor());
        AnimUtils.toggleOn(fab, 500, this);


        ArrayList<User> users = new ArrayList<>();
        UserAdapter adapter = new UserAdapter(users, this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        users.addAll(currentGroup.getMembers());
        populateChart(this, users);


        expand_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isExpanded) {
                    collapse(rv);
                    isExpanded = false;
                    expand_cardview.setImageResource(R.drawable.ic_expand_more);
                }
                else {
                    expand(rv);
                    isExpanded = true;
                    expand_cardview.setImageResource(R.drawable.ic_expand_less);
                }
            }
        });
    }


    public static void expand(final View v) {
        final int initialHeight= v.getHeight();
        v.measure(v.getMinimumWidth(), RecyclerView.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();
        if(targetHeight < 20)
            return;
        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = (int)((targetHeight * interpolatedTime));
                if(v.getLayoutParams().height >= initialHeight)
                    v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight/ v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int currHeight = v.getHeight();
        final int targetHeight = v.getMinimumHeight();
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                    v.getLayoutParams().height = v.getHeight() <= targetHeight ?  targetHeight : (currHeight - (int)(currHeight * interpolatedTime));
                if(v.getLayoutParams().height >= targetHeight)
                     v.requestLayout();

            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(currHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
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
        final XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f); // only intervals of 1 day
        final MarkerView marker = new ChartUserMarker(this, R.layout.item_chart_user_list);

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
        legend.setEnabled(false);

        List<LegendEntry> legends = new ArrayList<>();

        int i = 0;
        List<IBarDataSet> total = new ArrayList<>();
        final ArrayList<String> xvalues = new ArrayList<>();
        for (User u : users) {
            List<BarEntry> barEntries = new ArrayList<>();
            BarEntry b = new BarEntry(i, (float)( u.getBalance().getCredit() - u.getBalance().getDebit()));
            barEntries.add(b);
            b.setData(u);
            xvalues.add(u.getUser_name());
            LegendEntry l = new LegendEntry();
            l.form = Legend.LegendForm.LINE;
            l.label = u.getUser_name();
            l.formColor = u.getUser_color();
            i++;
            BarDataSet set = new BarDataSet(barEntries, "user " + i);
            set.setDrawValues(false);
            set.setColor(getResources().getColor(R.color.colorAccent));
            total.add(set);
            legends.add(l);
        }
        legend.setCustom(legends);
        final BarData data = new BarData(total);
        data.setBarWidth(0.85f);
        data.setValueTextSize(14f);
        chart.setDrawValueAboveBar(true);
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return value + " €";
            }
        });
        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xvalues.get((int)value);
            }
        };

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                IBarDataSet set;
                chart.setMarker(marker);
                set = data.getDataSetForEntry(e);
                set.setDrawValues(true);
                entry = e;
                highlighted = h;
                chart.invalidate();
                set.setDrawValues(false);
            }

            @Override
            public void onNothingSelected() {
                onValueSelected(entry, highlighted);
            }
        });
        chart.highlightValues(null);
        xAxis.setValueFormatter(formatter);
        chart.setData(data);
        chart.setFitBars(true);
        chart.animateXY(750, 1500, Easing.EasingOption.EaseInBounce, Easing.EasingOption.EaseInExpo);
        chart.setVisibleXRangeMaximum(8);


    }

    @Override
    public void onBackPressed() {
        AnimUtils.toggleOff(fab, 250, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorUtils.PaletteBuilder builder = new ColorUtils.PaletteBuilder();
            builder
                    .load(currentGroup.getGroupProfile())
                    .set(toolbar)
                    .set(tv_group_name)
                    .setContext(this)
                    .anim()
                    .comeBack(getResources().getColor(R.color.colorPrimary))
                    .method(ColorUtils.type.VIBRANT)
                    .brighter(ColorUtils.bright.LIGHT)
                    .build();
        }
        super.onBackPressed();
    }

    private void customizeToolbar(final Toolbar toolbar) {
        final CircularImageView c = (CircularImageView)findViewById(R.id.iv_group_image);
        final GroupColor gc = new GroupColor();
            ColorUtils.PaletteBuilder builder = new ColorUtils.PaletteBuilder();
            builder
                    .load(currentGroup.getGroupProfile())
                    .set(toolbar)
                    .set(fab)
                    .set(tv_group_name)
                    .set(tv_chart)
                    .set(partecipants)
                    .anim()
                    .method(ColorUtils.type.VIBRANT)
                    .brighter(ColorUtils.bright.LIGHT)
                    .build();
            c.setImageBitmap(currentGroup.getGroupProfile());

    }


    public class SelfRemovingOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public final void onScrollStateChanged(@NonNull final RecyclerView recyclerView, final int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                recyclerView.removeOnScrollListener(this);
            }
        }
    }

}
