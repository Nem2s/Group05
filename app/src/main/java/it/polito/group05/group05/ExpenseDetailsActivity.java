package it.polito.group05.group05;

import android.support.v4.util.TimeUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.Utility.Adapter.MemberExpenseAdapter;
import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
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
    Button button_pay;
    Button button_notify;
    Bundle b;
    RecyclerView rv;


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
        cv = (CircleImageView) findViewById(R.id.cv_owner_image);
        tv_date = (TextView) findViewById(R.id.tv_day_month);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_name = (TextView) findViewById(R.id.tv_owner_name);
        tv_expense = (TextView) findViewById(R.id.tv_expense_name);
        rv = (RecyclerView) findViewById(R.id.rv_expense_members);
        button_notify = (Button) findViewById(R.id.button_notify);
        button_pay = (Button) findViewById(R.id.button_pay);


        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        tv_price.setText(b.getString("price") + " €");
        tv_expense.setText(b.getString("title"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(b.getLong("timestamp"));
        tv_date.setText(format.format(calendar.getTime()));
        retriveUsersExpense((HashMap<String, Double>) b.getSerializable("map"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    private void retriveUsersExpense(final HashMap<String, Double> map) {
        final List<UserDatabase> users = new ArrayList<>();
        if (!b.getString("owner").equals(Singleton.getInstance().getCurrentUser().getId())) {
            users.add(Singleton.getInstance().getCurrentUser());
            button_pay.setVisibility(View.VISIBLE);
            button_notify.setVisibility(View.GONE);
        } else {
            button_pay.setVisibility(View.GONE);
            button_notify.setVisibility(View.VISIBLE);
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
