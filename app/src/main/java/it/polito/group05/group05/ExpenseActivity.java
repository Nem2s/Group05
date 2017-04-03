package it.polito.group05.group05;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import it.polito.group05.group05.R;

import static android.widget.AdapterView.*;

public class ExpenseActivity extends AppCompatActivity {

    private MaterialEditText et_name, et_description, et_cost, et_file;
    private CheckBox cb_description, cb_addfile, cb_adddeadline, cb_proposal;
    private TextView tv_policy;
    private Spinner spinner, spinner_policy;
    private Button addExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_v2);
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
        spinner_policy = (Spinner) findViewById(R.id.spinner_policy);

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
