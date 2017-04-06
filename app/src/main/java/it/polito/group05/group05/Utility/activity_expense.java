package it.polito.group05.group05.Utility;

import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import java.sql.Timestamp;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.TYPE_EXPENSE;
import it.polito.group05.group05.Utility.BaseClasses.User;

public class activity_expense extends AppCompatActivity {


    private MaterialEditText et_name, et_description, et_cost, et_file;
    private CheckBox cb_description, cb_addfile, cb_adddeadline, cb_proposal;
    private TextView tv_policy, tv_members;
    private Spinner spinner, spinner_policy;
    private Button addExpense;
    private RecyclerView recyclerView;
   /* private String id;
    private User owner;
    private String name;
    private String description;
    private Double price;
    private TYPE_EXPENSE type;
    private File file;
    private int deadline;           //days
    private Timestamp timestamp;
    private TreeMap<String, User> lista_partecipanti=new TreeMap<>();
    private String image;
*/
    private String expense_name;
    private String expense_description;
    private Double expense_price;
    private int expense_deadline;
    private TYPE_EXPENSE expense_type;
    private Group actual_group;
    private List<User> members_group ;
    private User expense_owner;
    private String id_owner;
    private String id_expense;
    //private java.security.Timestamp expense_timestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        et_name = (MaterialEditText) findViewById(R.id.et_name_expense);
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            expense_name = et_name.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        et_cost = (MaterialEditText) findViewById(R.id.et_cost_expense);
        et_cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                expense_price = 0.0;
                if (s != null) {
                    try {
                        expense_price = Double.parseDouble(s.toString().replace(',', '.'));
                    } catch (NumberFormatException e) {
                        //Error
                    }
                }
            }
        });
        cb_description = (CheckBox) findViewById(R.id.cb1_description);
        // hide until its title is clicked
        et_description = (MaterialEditText) findViewById(R.id.et_description_expense);
        et_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            expense_description = et_description.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        et_description.setVisibility(View.GONE);
        cb_addfile = (CheckBox) findViewById(R.id.cb2_addfile);
        // hide until its title is clicked
        et_file = (MaterialEditText) findViewById(R.id.et_addfile_expense);
        et_file.setVisibility(View.GONE);
        cb_adddeadline = (CheckBox) findViewById(R.id.cb3_deadline);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               expense_deadline= 12 + 12*position; //Measured in hours
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        cb_proposal = (CheckBox) findViewById(R.id.cb4_proposal);
        cb_proposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_proposal.isChecked()){
                        expense_type = TYPE_EXPENSE.NOTMANDATORY;
                    }
                    else
                        expense_type = TYPE_EXPENSE.MANDATORY;
                    }
        });
        tv_policy= (TextView) findViewById(R.id.tv_policy);
        //THE CONTENT OF THE SPINNER IS DYNAMIC
        spinner_policy = (Spinner) findViewById(R.id.spinner_policy);
        List<String> array= new ArrayList<String>();
        array.add("Equal part");
        array.add("Unequal part");
        ArrayAdapter<String> dataAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                array);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_policy.setAdapter(dataAdapter);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_members);

        MemberExpenseAdapter adapter= new MemberExpenseAdapter(this, members_group);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);

        id_owner= Singleton.getInstance().getId();
        expense_owner = Singleton.getInstance().getmCurrentGroup().getMember(id_owner);
        id_expense=String.valueOf( Singleton.getInstance().getmCurrentGroup().getExpenses().size() +1) ;

        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        final java.sql.Timestamp expense_timestamp = new java.sql.Timestamp(now.getTime());

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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Expense e = new Expense(id_expense,expense_owner,expense_name,expense_description,
                            expense_price, expense_type, expense_deadline, expense_timestamp);
                Singleton.getInstance().getmCurrentGroup().addExpense(e);
            }
        });

    }

    public void description_handler(View v){
        et_description.setVisibility(et_description.isShown() ? View.GONE : View.VISIBLE);
    }
    public void file_handler(View v){
        et_file.setVisibility(et_file.isShown() ? View.GONE : View.VISIBLE);
    }
    public void deadline_handler(View v){
        spinner.setVisibility(spinner.isShown() ? View.GONE : View.VISIBLE);
    }






}
