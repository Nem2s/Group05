package it.polito.group05.group05.Utility;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.User;

public class activity_expense extends AppCompatActivity {


    private MaterialEditText et_name, et_description, et_cost, et_file;
    private CheckBox cb_description, cb_addfile, cb_adddeadline, cb_proposal;
    private TextView tv_policy, tv_members;
    private Spinner spinner, spinner_policy;
    private Button addExpense;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        et_name = (MaterialEditText) findViewById(R.id.et_name_expense);
        et_cost = (MaterialEditText) findViewById(R.id.et_cost_expense);
        cb_description = (CheckBox) findViewById(R.id.cb1_description);
        // hide until its title is clicked
        et_description = (MaterialEditText) findViewById(R.id.et_description_expense);
        et_description.setVisibility(View.GONE);
        cb_addfile = (CheckBox) findViewById(R.id.cb2_addfile);
        // hide until its title is clicked
        et_file = (MaterialEditText) findViewById(R.id.et_addfile_expense);
        et_file.setVisibility(View.GONE);
        cb_adddeadline = (CheckBox) findViewById(R.id.cb3_deadline);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
        cb_proposal = (CheckBox) findViewById(R.id.cb4_proposal);
        tv_policy= (TextView) findViewById(R.id.tv_policy);
      //  tv_members=(TextView) findViewById(R.id.tv_members);
       // tv_members.setVisibility(View.GONE);
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
        List<User> members_group= Singleton.getInstance().getmCurrentGroup().getMembers();
        MemberExpenseAdapter adapter= new MemberExpenseAdapter(this, members_group);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);

        spinner_policy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                switch (position){
                    case 0: recyclerView.setVisibility(View.GONE);
                        break;
                    case 1: recyclerView.setVisibility(recyclerView.isShown() ? View.GONE : View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
