package it.polito.group05.group05;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.pkmmte.view.CircularImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.Utility.AnimUtils;
import it.polito.group05.group05.Utility.BaseClasses.ChartUserMarker;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.GroupColor;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.BaseClasses.UserGroup;
import it.polito.group05.group05.Utility.ColorUtils;
import it.polito.group05.group05.Utility.EventClasses.ObjectChangedEvent;
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
    private CardView cardView;
    private List<UserGroup> users;
    private boolean isExpanded;
    private UserAdapter adapter;
    private Context context;
    CircularImageView cv_group;
    RecyclerView rv;
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
        cardView = (CardView) findViewById(R.id.card_view);
        cv_group = (CircularImageView)findViewById(R.id.iv_group_image);
        isExpanded = false;

        rv = (RecyclerView)findViewById(R.id.rv_group_members);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        //customizeToolbar(toolbar);
        setSupportActionBar(toolbar);
        tv_chart.setTextColor(toolbar.getSolidColor());
        partecipants.setTextColor(toolbar.getSolidColor());
        AnimUtils.toggleOn(fab, 350, this);
        tv_group_name.setText(currentGroup.getName());
        cv_group.setImageBitmap(currentGroup.getGroupProfile());

        users = UserGroup.listUserGroup(currentGroup.getMembers());
        adapter = new UserAdapter(users, this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);
        rv.setAdapter(adapter);
        context = this;
        populateChart(this, users);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), AddMember.class);
                Pair<View, String> p2 = Pair.create((View)toolbar, getString(R.string.transition_toolbar));
                Pair<View, String> p3 = Pair.create((View)cv_group, getString(R.string.transition_group_image));
                Pair<View, String> p4 = Pair.create((View)tv_group_name, getString(R.string.transition_text));

                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context,p2, p3, p4);
                startActivity(i, options.toBundle());
            }
        });

    }

    public void onCardClick(View view) {
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

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        Log.d("Details", "Registered for " + this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        Log.d("Details", "Unregistered for " + this);
        super.onDestroy();
    }

    @Subscribe
    public void onObjectAdded(ObjectChangedEvent event) {
       Log.d("Details", "New member in the group!");
        User u = (User)event.retriveObject();

        users.add(new UserGroup(u));
        adapter.notifyDataSetChanged();
        Singleton.getInstance().getmCurrentGroup().addMember(u);
        cardView.callOnClick();
        addEntry(u);

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

    private void addEntry(User user) {
        UserGroup u;
        int position = chart.getData().getEntryCount();
        final BarData data = chart.getData();
        XAxis xaxis = chart.getXAxis();
        xaxis.setTextSize(11f);
        List<BarEntry> barEntries = new ArrayList<>();
        if(user instanceof UserGroup) u = (UserGroup) user;
        else u= new UserGroup(user);
        BarEntry b = new BarEntry(position, (float)( u.getBalance().getCredit() - u.getBalance().getDebit()));
        barEntries.add(b);
        b.setData(u);
        BarDataSet set = new BarDataSet(barEntries, u.getUser_name());
        set.setDrawValues(false);
        set.setColor(getResources().getColor(R.color.colorAccent));
        data.setBarWidth(0.85f);
        data.setValueTextSize(14f);
        chart.setDrawValueAboveBar(true);
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return value + " €";
            }
        });
        xaxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return data.getDataSetByIndex((int)value).getLabel();
            }
        });
        data.addDataSet(set);
        chart.notifyDataSetChanged();
        chart.invalidate();

    }

    private void populateChart(Context context, List<UserGroup> users) {
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

        final ArrayList<String> xvalues = new ArrayList<>();
        chart.setData(new BarData());
        for (UserGroup u : users) {
            xvalues.add(u.getUser_name());
            LegendEntry l = new LegendEntry();
            l.form = Legend.LegendForm.LINE;
            l.label = u.getUser_name();
            l.formColor = u.getUser_color();
            addEntry(u);
            legends.add(l);
        }
        legend.setCustom(legends);


        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                IBarDataSet set;
                chart.setMarker(marker);
                set = chart.getData().getDataSetForEntry(e);
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
        chart.setFitBars(true);
        chart.animateXY(750, 1500, Easing.EasingOption.EaseInBounce, Easing.EasingOption.EaseInExpo);
        chart.setVisibleXRangeMaximum(8);


    }

    @Override
    public void onBackPressed() {
        AnimUtils.toggleOff(fab, 350, this);
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
