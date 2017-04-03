package it.polito.group05.group05;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;

import com.rengwuxian.materialedittext.MaterialEditText;

import it.polito.group05.group05.R;

public class ExpenseActivity extends AppCompatActivity {

    private MaterialEditText et_name, et_description, et_cost, et_file;
    private CheckBox cb_description, cb_addfile, cb_adddeadline, cb_proposal;
    private ImageView im;
    private Spinner spinner;
    private Button addExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_v2);

        im= (ImageView) findViewById(R.id.image1);
        et_name= (MaterialEditText) findViewById(R.id.et_name_expense);
        et_cost= (MaterialEditText) findViewById(R.id.et_cost_expense);
        cb_description= (CheckBox) findViewById(R.id.cb1_description);
        // hide until its title is clicked
        et_description= (MaterialEditText) findViewById(R.id.et_description_expense);
        et_description.setVisibility(View.GONE);


        cb_addfile= (CheckBox) findViewById(R.id.cb2_addfile);
        cb_adddeadline= (CheckBox) findViewById(R.id.cb3_deadline);
        // hide until its title is clicked
        et_file= (MaterialEditText) findViewById(R.id.et_addfile_expense);
        et_file.setVisibility(View.GONE);

        spinner= (Spinner) findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);

        cb_proposal= (CheckBox) findViewById(R.id.cb4_proposal);
        addExpense= (Button) findViewById(R.id.buttonfinal);
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
