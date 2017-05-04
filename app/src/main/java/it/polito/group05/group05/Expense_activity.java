package it.polito.group05.group05;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import de.hdodenhof.circleimageview.CircleImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.group05.group05.Utility.BaseClasses.Expense;

import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.TYPE_EXPENSE;

import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.BaseClasses.*;
import it.polito.group05.group05.Utility.Holder.GeneralHolder;
import it.polito.group05.group05.Utility.Holder.MemberHolder;
import it.polito.group05.group05.Utility.Holder.MemberIncludedHolder;

public class Expense_activity extends AppCompatActivity {

    private CoordinatorLayout parent;
    private MaterialEditText et_name, et_description, et_cost;
    private CheckBox cb_description, cb_addfile, cb_adddeadline, cb_proposal, cb_details;
    private TextView tv_policy, tv_members;
    private Spinner  spinner_deadline, spinner_policy;
    private Button addExpense;
    private RecyclerView recyclerView;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private CircleImageView iv_group_image;
    private CardView cardView;
    private TextView tv_group_name;
    private FloatingActionButton fab;
    private ImageView image_network;

    ////////////////////////////////////////
    private ExpenseDatabase expense;
    private String expense_name;
    private String expense_description;
    private Double expense_price;
    private int expense_deadline;
    private TYPE_EXPENSE expense_type;
    private UserDatabase expense_owner;
    private String id_owner;
    private String id_expense;
    private GroupDatabase actual_group;

    private List<UserDatabase> partecipants = new ArrayList<>();
    private double costPerUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_v2);
        expense= new ExpenseDatabase();
        parent = (CoordinatorLayout)findViewById(R.id.parent_layout);
        setUpLayout();
        setupListener();

        costPerUser = 0;

        for(String s : Singleton.getInstance().getmCurrentGroup().getMembers().keySet()){

            if(!(Singleton.getInstance().getmCurrentGroup().getMembers().get(s)instanceof UserDatabase)) return;
           partecipants.add((UserDatabase) Singleton.getInstance().getmCurrentGroup().getMembers().get(s));
        }
        List<String> array= new ArrayList<>();
        array.add("Equal part");
        array.add("Unequal part");
        ArrayAdapter<String> dataAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_policy.setAdapter(dataAdapter);

    /*    List<UserDatabase> list = new ArrayList<>();
        //list.addAll(Singleton.getInstance().getmCurrentGroup().getMembers());
        final RecyclerView.Adapter r = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new MemberIncludedHolder(parent);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((GeneralHolder)holder).setData(partecipants.get(position),getApplicationContext());
            }

            @Override
            public int getItemCount() {
                return partecipants.size();
            }
        };
        recyclerView.setAdapter(r); */
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setVisibility(View.GONE);
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        final java.sql.Timestamp expense_timestamp = new java.sql.Timestamp(now.getTime());




    }

    private void setupListener() {
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                expense.setName(et_name.getText().toString());
                expense_name=et_name.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        expense_price = 0.0;
        et_cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                if (s != null) {
                    try {

                        expense.setPrice(Double.parseDouble(s.toString().replace(',', '.')));
                        Map<String,Double> map = new TreeMap<>();
                        for(UserDatabase udb : partecipants){
                            map.put(udb.getId(),expense.getPrice()/((double)partecipants.size()));
                        }
                        expense.setMembers(map);

                       /* costPerUser = (expense.getPrice()/partecipants.size());
                        DecimalFormat df = new DecimalFormat("0.00");
                        String formate = df.format(costPerUser);
                        double finalValue = Double.parseDouble(formate.replace(',', '.'));
*/


                    } catch (NumberFormatException e) {
                        expense.setPrice(0.0);
                    }
                }
            }
        });

        et_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                expense.setDescription(et_description.getText().toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        spinner_deadline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expense.setDeadline( 12 + 12*position); //Measured in hours
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        cb_proposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_proposal.isChecked()){
                    expense.setType(1);//; = TYPE_EXPENSE.NOTMANDATORY;
                }
                else
                    expense.setType(0);
                    //expense_type = TYPE_EXPENSE.MANDATORY;
            }
        });

        cb_addfile.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
            }
        });
        spinner_policy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                switch (position){
                    case 0: recyclerView.setVisibility(View.GONE);
                        break;
                    case 1: recyclerView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });

        cb_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_details.isChecked()){
                    cardView.setVisibility(View.VISIBLE);
                }
                else {
                    cardView.setVisibility(View.GONE);
                }
            }
        });

    }

    private void setUpLayout() {
        et_name = (MaterialEditText) findViewById(R.id.et_name_expense);
        et_name.setImeOptions(EditorInfo.IME_ACTION_DONE);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        iv_group_image= (CircleImageView) findViewById(R.id.iv_group_image);
        tv_group_name = (TextView) findViewById(R.id.tv_group_name);
        image_network = (ImageView) findViewById(R.id.image_network);
        et_cost = (MaterialEditText) findViewById(R.id.et_cost_expense);
        cb_details= (CheckBox) findViewById(R.id.more_details);
        cardView = (CardView) findViewById(R.id.card_view2_toshow);
        cardView.setVisibility(View.GONE);
        cb_description = (CheckBox) findViewById(R.id.cb1_description);
        et_description = (MaterialEditText) findViewById(R.id.et_description_expense);
        et_description.setVisibility(View.GONE);
        cb_addfile = (CheckBox) findViewById(R.id.cb2_addfile);
        cb_adddeadline = (CheckBox) findViewById(R.id.cb3_deadline);
        spinner_deadline = (Spinner) findViewById(R.id.spinner);
        spinner_deadline.setVisibility(View.GONE);
        cb_proposal = (CheckBox) findViewById(R.id.cb4_proposal);
        expense_type=TYPE_EXPENSE.MANDATORY;
        tv_policy= (TextView) findViewById(R.id.tv_policy);
        spinner_policy = (Spinner) findViewById(R.id.spinner_policy);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_members);
        fab = (FloatingActionButton) findViewById(R.id.fab_new_expense);
        setSupportActionBar(toolbar);
        iv_group_image.setImageResource(R.drawable.network);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tv_group_name.setText(Singleton.getInstance().getmCurrentGroup().getName());
    }

    public void description_handler(View v){
        et_description.setVisibility(et_description.isShown() ? View.GONE : View.VISIBLE);
    }

    public void deadline_handler(View v){
        spinner_deadline.setVisibility(spinner_deadline.isShown() ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupListener();
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if( expense_name.toString().length() == 0 || expense.getPrice()==0.0 ) {
                    Snackbar.make(view,"Insert values",Snackbar.LENGTH_SHORT).show();
                    }
                else if(expense.getPrice().toString().length()>6) Snackbar.make(view,"Price on max 6 characters",Snackbar.LENGTH_SHORT).show();
                else {

                    DatabaseReference fdb = FirebaseDatabase.getInstance().getReference("expenses/"+
                            Singleton.getInstance().getIdCurrentGroup()).push();
                    expense.setId(fdb.getKey());
                    fdb.setValue(expense);

                    finish();
                }
            }
        });

    }

    @Override
    protected void onStop() {

        super.onStop();
    }



}