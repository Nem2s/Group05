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
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.Utility.Adapter.MemberExpandedAdapter;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.TYPE_EXPENSE;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;
import it.polito.group05.group05.Utility.Event.ExpenseDividerEvent;
import it.polito.group05.group05.Utility.Event.PartecipantsNumberChangedEvent;
import it.polito.group05.group05.Utility.Event.PriceChangedEvent;
import it.polito.group05.group05.Utility.Event.PriceErrorEvent;



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
    private ImageView image_network, info;

    ////////////////////////////////////////
    private ExpenseDatabase expense;
    private String expense_name;
    private Double expense_price;
    private String expense_description;
    private int expense_deadline;
    private TYPE_EXPENSE expense_type;
    private UserDatabase expense_owner;
    private GroupDatabase actual_group;
    private RelativeLayout rel_info;
    private Double costPerUser = 0.0;
    private DatabaseReference fdb;
    private List<User_expense> partecipants = new ArrayList<>();
    private Map<String, User_expense> list = new TreeMap<>();
    private boolean isEqualPart=true;

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Subscribe
    public void onPartecipantsChanged(PartecipantsNumberChangedEvent event) {
        int n_custom = 0;
        double p_custom = 0;
        if(event.hasExpenseEvent()) {
            n_custom = event.getEvent().getNPeople();
            p_custom = event.getEvent().getTotal();
        }
        if(event.getN() < 0) {
            list.remove(list.get(event.getUser().getId()));
            Toast.makeText(getApplicationContext(), "Removed " + event.getUser().getName(), Toast.LENGTH_SHORT).show();
        }
        else {
            list.put(event.getUser().getId(), event.getUser());
            Toast.makeText(getApplicationContext(), "Added " + event.getUser().getName(), Toast.LENGTH_SHORT).show();
        }

        costPerUser = ((expense.getPrice() - p_custom)/(list.size() - n_custom));
       DecimalFormat df=new DecimalFormat("0.00");
        String formate = df.format(costPerUser);
        double finalValue = Double.parseDouble(formate.replace(',', '.'));
        EventBus.getDefault().post(new PriceChangedEvent(finalValue));
      // EventBus.getDefault().post(new PriceChangedEvent(costPerUser));
    }

    @Subscribe
    public void onCustomValueSetted(ExpenseDividerEvent event) {
        if(event.getTotal() > expense.getPrice()) {
            Snackbar.make(parent, "Defined cost is higher than the total expense cost!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            costPerUser =  (expense.getPrice()/(list.size()));
                            DecimalFormat df=new DecimalFormat("0.00");
                            String formate = df.format(costPerUser);
                            double finalValue = Double.parseDouble(formate.replace(',', '.'));
                            EventBus.getDefault().post(new PriceErrorEvent());
                            EventBus.getDefault().post(new PriceChangedEvent(finalValue));
                        }
                    })
                    .show();
        }
        double total = expense.getPrice() - event.getTotal();
        costPerUser =  (total/(list.size() - event.getNPeople()));
        DecimalFormat df=new DecimalFormat("0.00");
        String formate = df.format(costPerUser);
        double finalValue = Double.parseDouble(formate.replace(',', '.'));
        EventBus.getDefault().post(new PriceChangedEvent(finalValue));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_v2);
        costPerUser = 0.0;
        expense_price = 0.0;
        expense= new ExpenseDatabase();
        expense.setPrice(0.0);
        //expense.setOwner(Singleton.getInstance().getCurrentUser().getId());
        parent = (CoordinatorLayout)findViewById(R.id.parent_layout);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        iv_group_image= (CircleImageView) findViewById(R.id.iv_group_image);
        tv_group_name = (TextView) findViewById(R.id.tv_group_name);
        image_network = (ImageView) findViewById(R.id.image_network);
        et_name = (MaterialEditText) findViewById(R.id.et_name_expense);
        et_name.setImeOptions(EditorInfo.IME_ACTION_DONE);
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
        //info = (ImageView) findViewById(R.id.InfoButton);
        tv_policy= (TextView) findViewById(R.id.tv_policy);
        spinner_policy = (Spinner) findViewById(R.id.spinner_policy);
        //rel_info= (RelativeLayout) findViewById(R.id.relative_info);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_members);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        iv_group_image.setImageResource(R.drawable.network);
        tv_group_name.setText(Singleton.getInstance().getmCurrentGroup().getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        for(String s : Singleton.getInstance().getmCurrentGroup().getMembers().keySet()){
            if(!(Singleton.getInstance().getmCurrentGroup().getMembers().get(s)instanceof UserDatabase)) return;
            User_expense ue=new User_expense((UserDatabase)Singleton.getInstance().getmCurrentGroup().getMembers().get(s));
            ue.setExpense(expense);
            partecipants.add(ue);
            list.put(ue.getId(),ue);
       }
        List<String> array= new ArrayList<>();
        array.add("Equal part");
        array.add("Unequal part");
        ArrayAdapter<String> dataAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_policy.setAdapter(dataAdapter);

        final MemberExpandedAdapter memberAdapter = new MemberExpandedAdapter(partecipants, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(memberAdapter);
        recyclerView.setVisibility(View.GONE);
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        final java.sql.Timestamp expense_timestamp = new java.sql.Timestamp(now.getTime());

        expense.setTimestamp(expense_timestamp.toString());
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

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if( expense.getName().toString().length() == 0 /*|| expense.getPrice()==0.0 */) {
                    Snackbar.make(view,"Invalid name",Snackbar.LENGTH_SHORT).show();
                }
                else if(expense.getPrice().toString().length()>6) Snackbar.make(view,"Price on max 6 characters",Snackbar.LENGTH_SHORT).show();
                else {
                    fdb =   FirebaseDatabase.getInstance()
                                            .getReference("expenses")
                                            .child(Singleton.getInstance().getIdCurrentGroup())
                                            .push();
                    expense.setId(fdb.getKey());
/*
                    if(!isEqualPart){
                        for(User_expense u : memberAdapter.getList()){
                            expense.getMembers().put(u.getId(), u.getCustomValue());
                        }
                    }*/
                        fdb.setValue(expense);
                    finish();
                }
            }
        });

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

        et_cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              //  expense.setPrice(9.00);
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s != null) {
                    try {
                        expense.setPrice(Double.parseDouble(s.toString().replace(',', '.')));
                        expense_price =  Double.parseDouble(s.toString().replace(',', '.'));
                        costPerUser = (expense.getPrice()/(list.size()));
                        for(UserDatabase u : partecipants) {
                     /*       if(u.getId().compareTo(expense.getOwner())==0)
                                expense.getMembers().put(u.getId(), expense.getPrice()-costPerUser);
                            else*/
                                expense.getMembers().put(u.getId(), costPerUser);
                        }
                        DecimalFormat df = new DecimalFormat("0.00");
                        String formate = df.format(costPerUser);
                        double finalValue = Double.parseDouble(formate.replace(',', '.'));
                        //et_cost.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                        EventBus.getDefault().post(new PriceChangedEvent(finalValue));
                    } catch (NumberFormatException e) {
                        expense.setPrice(0.0);
                        expense_price = 0.0;
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
                    expense.setType(1); //; = TYPE_EXPENSE.NOTMANDATORY;
                }
                else
                    expense.setType(0); //expense_type = TYPE_EXPENSE.MANDATORY;
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
                        isEqualPart=true;
                        break;
                    case 1: recyclerView.setVisibility(View.VISIBLE);
                        isEqualPart=false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });


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




}