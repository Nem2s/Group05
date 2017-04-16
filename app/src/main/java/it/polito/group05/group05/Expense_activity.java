package it.polito.group05.group05;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.TYPE_EXPENSE;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;
import it.polito.group05.group05.Utility.MemberExpenseAdapter;

import static it.polito.group05.group05.Utility.ColorUtils.context;

public class Expense_activity extends AppCompatActivity {


    private MaterialEditText et_name, et_description, et_cost;
    private CheckBox cb_description, cb_addfile, cb_adddeadline, cb_proposal, cb_details;
    private TextView tv_policy, tv_members;
    private Spinner  spinner_deadline, spinner_policy;
    private RecyclerView recyclerView;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private CardView cardView;
    private TextView tv_group_name;
    private FloatingActionButton fab;
    private ImageView image_network;
    private ParcelFileDescriptor mInputPFD;
    private Intent intent_file;
    private FileDescriptor fd;
    private Uri returnUri;
    private boolean newFile = false;

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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_v2);
        et_name = (MaterialEditText) findViewById(R.id.et_name_expense);
        et_name.setImeOptions(EditorInfo.IME_ACTION_DONE);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        //iv_group_image= (CircularImageView) findViewById(R.id.iv_group_image);
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
        fab = (FloatingActionButton) findViewById(R.id.fab);

        setSupportActionBar(toolbar);
    //    iv_group_image.setImageBitmap(Singleton.getInstance().getmCurrentGroup().getGroupProfile());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tv_group_name.setText(Singleton.getInstance().getmCurrentGroup().getName());

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
                        expense_price = Double.parseDouble(s.toString().replace(',', '.'));
                        et_cost.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                    } catch (NumberFormatException e) {
                        expense_price=0.0;
                    }
                }
            }
        });

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

        spinner_deadline.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expense_deadline= 12 + 12*position; //Measured in hours
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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

        cb_addfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent,0);
            }
        });

        List<String> array= new ArrayList<>();
        array.add("Equal part");
        array.add("Unequal part");
        ArrayAdapter<String> dataAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_policy.setAdapter(dataAdapter);
        final MemberExpenseAdapter adapter= new MemberExpenseAdapter(this, Singleton.getInstance().getmCurrentGroup().getMembers(),
                new MemberExpenseAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClicked(User position) {
                        //et_name.setText("CIAO" + position.getId().toString());
                        //et_cost.setText("0.0");
                    }
                });
        adapter.notifyDataSetChanged();
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
                if(expense_name == null || expense_price==0 ) {
                    Snackbar.make(view,"Insert values",Snackbar.LENGTH_SHORT).show();}
                else if(expense_price.toString().length()>6) Snackbar.make(view,"Price on max 6 characters",Snackbar.LENGTH_SHORT).show();
                else {
                    Expense e = new Expense(id_expense, expense_owner, expense_name, expense_description == null ? "" : expense_description,
                            expense_price, expense_type, expense_deadline, expense_timestamp);
                    if(newFile){
                        e.addFile(returnUri);
                        e.setFileTrue();}
                    List<User> l = User_expense.createListUserExpense(Singleton.getInstance().getmCurrentGroup(), e);
                    e.setPartecipants(l);
                    Singleton.getInstance().getmCurrentGroup().addExpense(e);
                    finish();
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            returnUri = data.getData();
            newFile= true;
            try {
                mInputPFD = getContentResolver().openFileDescriptor(returnUri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e("MainActivity", "File not found.");
                return;
            }
            fd = mInputPFD.getFileDescriptor();
        }
        }
    }


