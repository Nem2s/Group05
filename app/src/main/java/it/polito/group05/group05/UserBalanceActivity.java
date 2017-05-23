package it.polito.group05.group05;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.Utility.Adapter.LegendAdapter;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.Event.ExpenseCountEvent;
import it.polito.group05.group05.Utility.Event.GroupInfoChartEvent;
import it.polito.group05.group05.Utility.HelperClasses.AnimUtils;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;
import it.polito.group05.group05.Utility.HelperClasses.ImageUtils;

public class UserBalanceActivity extends AppCompatActivity {

    PieChart pchart;
    LineChart lchart;
    ProgressBar ppb;
    Snackbar snackbar;
    TextView tv_username;
    CircleImageView cv_userImage;
    Typeface mAppFont;
    ImageView iv_nodata;
    CircleImageView cv_groupImage;
    LinearLayout linearLayout;
    RecyclerView rv;
    LegendAdapter adapter;

    private Entry currEntry;
    private Highlight highlighted;
    private String currUserId = Singleton.getInstance().getCurrentUser().getId();
    private List<Entry> expenseEntries = new ArrayList<>();
    private FloatingActionButton fab_group_color;
    private TextView tv_groupName;
    private TextView tv_groupBalance;


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onDataReady(GroupInfoChartEvent e) {
        GroupInfoChartEvent event = EventBus.getDefault().removeStickyEvent(GroupInfoChartEvent.class);
        if(event != null && e.getEntry().size() > 0) {
            ppb.setVisibility(View.GONE);

            pchart.setVisibility(View.VISIBLE);
            snackbar.dismiss();
            iv_nodata.setVisibility(View.GONE);

            setupPieChart();
            updatePieChart(e.getEntry());
        } else {
            iv_nodata.setVisibility(View.VISIBLE);
            snackbar.show();
            pchart.setVisibility(View.INVISIBLE);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onExpenseDataReady(ExpenseCountEvent e) {
        ExpenseCountEvent event = EventBus.getDefault().removeStickyEvent(ExpenseCountEvent.class);
        if(event.getList().size() > 0) {
            lchart.setVisibility(View.VISIBLE);
            setupLineChart(event.getList());
        } else {
            lchart.setVisibility(View.GONE);
        }

    }

    private void setupLineChart(List<Entry> list) {
        Collections.sort(list, new EntryXComparator());
        LineDataSet set = new LineDataSet(list, "History of Expenses posted by You (last 6 months)");
        lchart.setExtraOffsets(16, 5, 30, 30);
        lchart.getDescription().setEnabled(false);
        XAxis xaxis = lchart.getXAxis();
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setTextSize(10f);
        xaxis.setDrawAxisLine(false);
        xaxis.setDrawGridLines(true);
        xaxis.setTextColor(getResources().getColor(R.color.colorAccent));
        xaxis.setGranularity(1f); // one day
        xaxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM yy");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                long millis = TimeUnit.DAYS.toMillis((long)value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = lchart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setYOffset(-9f);
        leftAxis.setXOffset(8f);
        leftAxis.setTextColor(getResources().getColor(R.color.colorAccent));

        YAxis rightAxis = lchart.getAxisRight();
        rightAxis.setEnabled(false);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColors(getResources().getColor(R.color.colorPrimaryLight));
        set.setValueTextColor(getResources().getColor(R.color.colorPrimaryLight));
        set.setLineWidth(1.5f);
        set.setValueTextSize(10f);
        set.setCircleColor(getResources().getColor(R.color.colorAccent));
        set.setDrawCircles(true);
        set.setCircleRadius(4f);
        set.setDrawValues(true);
        set.setFillAlpha(110);
        set.setHighLightColor(getResources().getColor(R.color.colorAccent));
        set.setDrawCircleHole(false);
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setFillColor(R.color.colorPrimary);
        lchart.setDragEnabled(true);
        lchart.getLegend().setTextSize(11f);
        lchart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        lchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("dd MMMM yyyy");
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                long millis = TimeUnit.DAYS.toMillis((long)e.getX());
                ExpenseDatabase ex = (ExpenseDatabase)e.getData();
                Snackbar.make(findViewById(R.id.parent_layout), (int)e.getY() + " expenses posted on " + mFormat.format(new Date(millis)), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        lchart.setData(new LineData(set));
        lchart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        lchart.invalidate();
    }

    private void updatePieChart(List<PieEntry> entries) {

        final PieDataSet set = new PieDataSet(entries, null);
        List<Integer> colors = new ArrayList<>();
        set.setDrawValues(false);
        pchart.getDescription().setEnabled(false);
        for(int i : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(i);
        for(int i : ColorTemplate.LIBERTY_COLORS)
            colors.add(i);
        for(int i : ColorTemplate.PASTEL_COLORS)
            colors.add(i);
        for(int i : ColorTemplate.MATERIAL_COLORS)
            colors.add(i);
        for(int i : ColorTemplate.COLORFUL_COLORS)
            colors.add(i);
        for(int i : ColorTemplate.JOYFUL_COLORS)
            colors.add(i);

        //if(adapter == null) {
            adapter = new LegendAdapter(entries, colors, this);
            LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rv.setLayoutManager(llm);
            rv.setAdapter(adapter);
       /* } else {
            adapter.updateEntries(entries);
        }*/
        set.setColors(colors);
        set.setDrawIcons(false);
        set.setSelectionShift(5f);
        set.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                if (entry.equals(currEntry)) {
                    GroupDatabase g = (GroupDatabase) entry.getData();

                    float v = Float.valueOf(g.getMembers().get(currUserId).toString());
                    String s = String.format("%.2f", v);
                    String res;
                    if (v > 0)
                        res = "+" + s + " €";
                    else
                        res = s + " €";
                    return res;
                } else {
                    return "";
                }
            }

        });
        set.setValueTypeface(Typeface.SANS_SERIF);
        PieData data = new PieData(set);

        final List<LegendEntry> entryList = new ArrayList<>();

        float tot = 0;
        List<Integer> valueColors = new ArrayList<>();
        for(PieEntry e : set.getValues()) {
            GroupDatabase g = (GroupDatabase)e.getData();
            float v = Float.valueOf(g.getMembers().get(currUserId).toString());
            tot+=v;
            if(v > 0) {
                valueColors.add(getResources().getColor(R.color.green_300));
            }

            else {
                valueColors.add(getResources().getColor(R.color.red_300));

            }
        }
        pchart.setCenterText(tot > 0 ? "+" + String.format("%.2f", tot) + " €" : String.format("%.2f", tot) + " €");
        pchart.setCenterTextColor(tot > 0 ? getResources().getColor(R.color.green_300) : getResources().getColor(R.color.red_300));
        data.setValueTextSize(10f);
        data.setValueTypeface(Typeface.DEFAULT_BOLD);
        pchart.getLegend().setEnabled(false);



        pchart.setData(data);
        pchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {


            @Override
            public void onValueSelected(Entry e, Highlight h) {
                GroupDatabase g = (GroupDatabase)e.getData();
                currEntry = e;
                highlighted = h;
                set.setDrawValues(true);
                Pair<View, String> p = new Pair<View, String>(pchart, "qwe");
                AnimUtils.startActivityWithAnimation(UserBalanceActivity.this, new Intent(UserBalanceActivity.this, GroupDetailsActivity.class),
                        p);
            }

            @Override
            public void onNothingSelected() {
                onValueSelected(currEntry, highlighted);
            }
        });
        pchart.invalidate();
        pchart.notifyDataSetChanged();
    }


    private void setupPieChart() {
        pchart.setTransparentCircleColor(getResources().getColor(R.color.grey_300));
        pchart.setTransparentCircleAlpha(110);
        pchart.setTransparentCircleRadius(55);
        pchart.setExtraOffsets(20, 0, 20, 5);
        pchart.setCenterTextSize(22f);
        pchart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        pchart.setHoleRadius(50);
        pchart.setDrawSlicesUnderHole(false);
        pchart.setDrawCenterText(true);
        pchart.setRotationAngle(0);
        pchart.setDrawEntryLabels(false);
        pchart.setEntryLabelColor(getResources().getColor(R.color.colorSecondaryText));
        pchart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_balance);
        pchart = (PieChart)findViewById(R.id.user_piechart);
        ppb = (ProgressBar)findViewById(R.id.pb_loading_pchart);
        tv_username = (TextView)findViewById(R.id.tv_userName_balance);
        cv_userImage = (CircleImageView)findViewById(R.id.cv_userimage);
        lchart = (LineChart)findViewById(R.id.user_linechart);
        iv_nodata = (ImageView)findViewById(R.id.iv_nodata);
        tv_username.setText(Singleton.getInstance().getCurrentUser().getName());
        rv = (RecyclerView)findViewById(R.id.rv_legend);

        snackbar = Snackbar.make(findViewById(R.id.parent_layout), "You're not in any groups, Try to create one!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(UserBalanceActivity.this, NewGroupActivity.class));
                            finish();
                        }
                    });
        mAppFont = Typeface.createFromAsset(getAssets(), "fonts/Streetwear.otf");
        ((TextView)findViewById(R.id.tv_hello)).setTypeface(mAppFont);

        initializeUI();
    }

    private void initializeUI() {
        ImageUtils.LoadMyImageProfile(cv_userImage, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    DB_Manager.getInstance().retriveGroups();
                    DB_Manager.getInstance().retriveExpenses();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });

            scheduleStartPostponedTransition(cv_userImage);
        } else {
            DB_Manager.getInstance().retriveGroups();
            DB_Manager.getInstance().retriveExpenses();
        }
    }

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }


    @Override

    protected void onStop() {

        EventBus.getDefault().unregister(this);
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override

    protected void onStart() {

        super.onStart();
        EventBus.getDefault().register(this);

    }

    private final void focusOnView(final NestedScrollView scroll, final View view) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                int vLeft = view.getTop();
                int vRight = view.getBottom();
                int sWidth = scroll.getWidth();
                scroll.smoothScrollTo(((vLeft + vRight - sWidth) / 2), 0);
            }
        });
    }


}
