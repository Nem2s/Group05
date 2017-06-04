package it.polito.group05.group05;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.aesthetic.Aesthetic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

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
    ImageView iv_expense;
    TextView tv_price;
    TextView tv_date;
    TextView tv_name;

    ImageView iv_arrow;
    TextView tv_members;
    TextView tv_expense;
    Button button_pay;
    Button download;
    Button button_notify;
    RelativeLayout rel_lay;
    TextView nameFile;
    ImageView imageFile;
    Bundle b;
    RecyclerView rv;
    ScrollView sv;
    String nomeF, idFile;
    Boolean correct_download;
     HashMap<String, Double> map;

    private long fileSize = 0;
    private HashMap<String, Object> payed;

    @Override
    protected void configureScroller(MultiShrinkScroller scroller) {
        super.configureScroller(scroller);
        // scroller.setScroll(MultiShrinkScroller.FOCUS_DOWN);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void init(Bundle bundle) {


        b = getIntent().getExtras();
         map = (HashMap<String, Double>)b.getSerializable("map");
       payed= (HashMap<String, Object>)b.getSerializable("payed");
        nomeF = b.getString("file");
        idFile= b.getString("id");

        //correct_download = b.getBoolean("correct_download");

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
        iv_expense = (ImageView)findViewById(R.id.iv_expense_image);
        tv_expense = (TextView) findViewById(R.id.tv_expense_name);
        tv_members = (TextView)findViewById(R.id.tv_static_members);
        iv_arrow = (ImageView)findViewById(R.id.iv_arrow);
        rv = (RecyclerView) findViewById(R.id.rv_expense_members);
        button_notify = (Button) findViewById(R.id.button_notify);
        button_pay = (Button) findViewById(R.id.button_pay);
        rel_lay = (RelativeLayout) findViewById(R.id.relative_file);
        rel_lay.setVisibility(View.GONE);
        nameFile = (TextView) findViewById(R.id.nome_file);
        download = (Button) findViewById(R.id.download);
        iv_arrow.setColorFilter(Aesthetic.get().colorAccent().take(1).blockingFirst());
        tv_members.setTextColor(Aesthetic.get().colorAccent().take(1).blockingFirst());
        ImageUtils.LoadExpenseImage(b.getString("img"), this, iv_expense);
        if(b.getString("file") != null){
            if (!(b.getString("file").equals("Fail")))
                {
                    if (rel_lay.getVisibility() == View.GONE) {
                        rel_lay.setVisibility(View.VISIBLE);
                        nameFile.setText(b.getString("file"));
                    }
                }
                else {
                rel_lay.setVisibility(View.GONE);
            }
        }else {
            rel_lay.setVisibility(View.GONE);
        }

        /*

        if(nomeF.equals("Fail") || nomeF.equals(null)){
            rel_lay.setVisibility(View.GONE);
        }else {
            if (rel_lay.getVisibility() == View.GONE) {
                rel_lay.setVisibility(View.VISIBLE);
                nameFile.setText(b.getString("file"));
            }
        }
*/
     /*   if(nomeF ){
            if (rel_lay.getVisibility() == View.GONE){
                rel_lay.setVisibility(View.VISIBLE);
                nameFile.setText(b.getString("file"));
            }
        }else{
            rel_lay.setVisibility(View.GONE);}
*/
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                DB_Manager.getInstance().fileDownload(idFile,nomeF);
                }
                catch (FileNotFoundException fnfe){
                  //  Snackbar.make( ExpenseDetailsActivity.this, "File not found", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        nameFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DB_Manager.getInstance().fileDownload(idFile,nomeF);
                    File f = new File(Environment.getExternalStorageDirectory().getPath() + "/FileAppPoli/"+ nomeF);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(f),"*/*");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                } catch (FileNotFoundException e) {
                    Snackbar.make(v, "File not found", Snackbar.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        LinearLayoutManager ll = new LinearLayoutManager(this);
        rv.setLayoutManager(ll);
        rv.setItemAnimator(new DefaultItemAnimator());
        tv_price.setText( b.getString("price") + " €");
        tv_expense.setText(b.getString("title"));
        retriveUsersExpense(map,payed);
       // retriveUsersExpense((HashMap<String, Object>)b.getSerializable("payed"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    private void retriveUsersExpense(final HashMap<String, Double> map,final HashMap<String,Object> payed ) {
        final List<UserDatabase> users = new ArrayList<>();
        final Context context = this;
        final Map<View,String[]> map_tuto = new HashMap<>();

        if (!b.getString("owner").equals(Singleton.getInstance().getCurrentUser().getId())) {
            users.add(Singleton.getInstance().getCurrentUser());
            button_pay.setVisibility(View.VISIBLE);

            button_notify.setVisibility(View.GONE);
            map_tuto.put(button_pay,new String[]{"Pay","Pay your debit and wait for owner confirmation"});
            ImageUtils.showTutorial(this,map_tuto);
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

        } else {
Boolean b = false;
            for(String c : payed.keySet()){
                if(!(payed.get(c) instanceof Boolean)) continue;
                if(c.equals(Singleton.getInstance().getCurrentUser().getId())) continue;
                if((Boolean)payed.get(c))
                    b = true;

            }
            if(!b) {
                setFab(Aesthetic.get().colorAccent().take(1).blockingFirst(), R.drawable.ic_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DB_Manager.getInstance().updateGroupFlow(map);
                    }
                });
            }
            button_pay.setVisibility(View.GONE);
            button_notify.setVisibility(View.VISIBLE);
            button_notify.setText("Remind to others");


            map_tuto.put(button_notify,new String[]{"Reminder","Send a reminder to those who did not pay you"});
            ImageUtils.showTutorial(this,map_tuto);

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
        final MemberExpenseAdapter adapter = new MemberExpenseAdapter(users, getApplicationContext(), map,payed);

        for (final String s : map.keySet()) {
            FirebaseDatabase.getInstance().getReference("users").child(s).child("userInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserDatabase u = dataSnapshot.getValue(UserDatabase.class);
                    if (u.getId().equals(b.getString("owner"))) {
                        ImageUtils.LoadUserImageProfile(cv, getApplicationContext(), u);
                        tv_name.setText(u.getName());
                    } else {
                        if(!u.getId().equals(Singleton.getInstance().getCurrentUser().getId())) {
                            users.add(u);
                            adapter.notifyItemChanged(users.size());
                        }
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
