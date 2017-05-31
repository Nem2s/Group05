package it.polito.group05.group05;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.aesthetic.Aesthetic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.Utility.Adapter.MemberExpenseAdapter;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;
import it.polito.group05.group05.Utility.HelperClasses.ImageUtils;


public class ExpenseDetailsActivity extends SlidingActivity {

    private int LEFT_OFFSET;
    private int TOP_OFFSET;
    private int WIDTH;
    private int HEIGHT;
    private String dateFormat = "dd/MM";
    CircleImageView cv;
    TextView tv_price;
    TextView tv_date;
    TextView tv_name;
    TextView tv_expense;
    TextView tv_members;
    ImageView iv_arrow;
    Button button_pay;
    Button button_notify;
    Bundle b;
    RecyclerView rv;
    ScrollView sv;

    @Override
    protected void configureScroller(MultiShrinkScroller scroller) {
        super.configureScroller(scroller);
        // scroller.setScroll(MultiShrinkScroller.FOCUS_DOWN);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void init(Bundle bundle) {

        b = getIntent().getExtras();


        setContent(R.layout.activity_expense_details);
        LEFT_OFFSET = b.getInt("left_offset");
        TOP_OFFSET = b.getInt("top_offset");
        WIDTH = b.getInt("width");
        HEIGHT = b.getInt("height");
        expandFromPoints(LEFT_OFFSET, TOP_OFFSET, WIDTH, HEIGHT);
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        enableFullscreen();
        cv = (CircleImageView) findViewById(R.id.cv_owner_image);
        sv = (ScrollView) findViewById(R.id.scroll_view_to);
        tv_date = (TextView) findViewById(R.id.tv_day_month);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_name = (TextView) findViewById(R.id.tv_owner_name);
        tv_expense = (TextView) findViewById(R.id.tv_expense_name);
        tv_members = (TextView) findViewById(R.id.tv_static_members);
        iv_arrow = (ImageView) findViewById(R.id.iv_arrow);
        rv = (RecyclerView) findViewById(R.id.rv_expense_members);
        button_notify = (Button) findViewById(R.id.button_notify);
        button_pay = (Button) findViewById(R.id.button_pay);
        iv_arrow.setColorFilter(Aesthetic.get().colorAccent().take(1).blockingFirst());
        tv_members.setTextColor(Aesthetic.get().colorAccent().take(1).blockingFirst());
        LinearLayoutManager ll = new LinearLayoutManager(this);
        rv.setLayoutManager(ll);
        rv.setItemAnimator(new DefaultItemAnimator());
        double d = Double.parseDouble(b.getString("price"));
        tv_price.setText(String.format("%.2f", d) + " €");
        tv_expense.setText(b.getString("title"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(b.getLong("timestamp"));
        tv_date.setText(format.format(calendar.getTime()));
        retriveUsersExpense((HashMap<String, Double>) b.getSerializable("map"), (HashMap<String, Object>) b.getSerializable("payed"));

        sv.fullScroll(ScrollView.FOCUS_DOWN);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void retriveUsersExpense(final Map<String, Double> map, final Map<String, Object> payed) {
        final List<UserDatabase> users = new ArrayList<>();
        final Context context = this;

        if (!b.getString("owner").equals(Singleton.getInstance().getCurrentUser().getId())) {
            users.add(Singleton.getInstance().getCurrentUser());
            button_pay.setVisibility(View.VISIBLE);

            button_notify.setVisibility(View.GONE);

            //Boolean b =(Boolean) payed.get(Singleton.getInstance().getCurrentUser().getId());
            //if(!b){
            button_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = Singleton.getInstance().getCurrentUser().getId();
                    String gid = Singleton.getInstance().getmCurrentGroup().getId();
                    DB_Manager.getInstance().notifyPayment(gid, getIntent().getStringExtra("expenseId"), s);
                    Toast.makeText(context, "Your request is sent, now the owner must confirm your payment", Toast.LENGTH_LONG);
                    finish();
                }
            });
            //}
            //  else {
            //    button_pay.setBackgroundColor(getResources().getColor(R.color.grey_600));
            //  button_pay.setText("Payed");

            //}
        } else {
            button_pay.setVisibility(View.GONE);
            button_notify.setVisibility(View.VISIBLE);
            button_notify.setText("Remind to others");
            button_notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = Singleton.getInstance().getCurrentUser().getId();
                    String gid = Singleton.getInstance().getmCurrentGroup().getId();
                    DB_Manager.getInstance().reminderToAll(gid, getIntent().getStringExtra("expenseId"), s);
                    Toast.makeText(context, "A Reminder is sent to all expense members", Toast.LENGTH_LONG);

                    finish();
                }
            });
        }
        final MemberExpenseAdapter adapter = new MemberExpenseAdapter(users, getApplicationContext(), map);
        ;
        for (final String s : map.keySet()) {
            FirebaseDatabase.getInstance().getReference("users").child(s).child("userInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserDatabase u = dataSnapshot.getValue(UserDatabase.class);
                    if (u.getId().equals(b.getString("owner"))) {
                        ImageUtils.LoadUserImageProfile(cv, getApplicationContext(), u);
                        tv_name.setText(u.getName());

                    } else {
                        if (u.getId().equals(Singleton.getInstance().getCurrentUser().getId()))
                            return;
                        users.add(u);
                        adapter.notifyItemChanged(users.size());


                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        rv.setAdapter(adapter);


    }
}
