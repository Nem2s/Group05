package it.polito.group05.group05;

import android.app.ProgressDialog;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import ernestoyaquello.com.verticalstepperform.VerticalStepperFormLayout;
import ernestoyaquello.com.verticalstepperform.interfaces.VerticalStepperForm;

import static android.R.attr.digits;


public class Expense_activity_Vertical_stepper extends AppCompatActivity implements VerticalStepperForm {

    private VerticalStepperFormLayout vs;
    private EditText  name, cost, description;
    private ProgressDialog progressDialog;
    public String[] steps = {"Name", "Cost", "Description"};
    private String current= "";
    private String ex_name, ex_description;
    private Double ex_cost;


    public Expense_activity_Vertical_stepper() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense__vertical_stepper);

        int colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        int colorPrimaryDark = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);

        vs = (VerticalStepperFormLayout) findViewById(R.id.vertical_stepper_form);
        VerticalStepperFormLayout.Builder.newInstance(vs, steps, (VerticalStepperForm) this, this)
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .displayBottomNavigation(true)
                .init();
    }

    public View createStepContentView(int stepNumber){
        View view= null;
        switch (stepNumber){
            case 0:
                view = createViewName();
                break;
            case 1:
                view= createCostView();
                break;
            case 2:
                view= createDescription();
                break;
        }
        return view;
    }

    private View createViewName() {
        name = new EditText(this);
        name.setSingleLine(true);
        int w = getWindowManager().getDefaultDisplay().getWidth();
        name.setWidth(w);
        name.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    vs.goToNextStep();
                    return false;
            }
        });
        return name;
    }

    private View createCostView(){
        cost = new EditText(this);
        cost.setInputType(InputType.TYPE_CLASS_NUMBER);
        cost.setTransformationMethod(null);
        cost.setSingleLine(true);
        cost.setHint("Cost here");
        cost.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                vs.goToNextStep();
                return false;
            }
        });

        cost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    cost.removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format(parsed/100);

                    current = formatted;
                    cost.setText(formatted);
                    cost.setSelection(formatted.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
            });


        return cost;
    }

    private View createDescription(){
        description = new EditText(this);
        description.setSingleLine(true);
        int w = getWindowManager().getDefaultDisplay().getWidth();
        description.setWidth(w);
        description.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                vs.goToNextStep();
                return false;
            }
        });
        return description;
    }


    public void onStepOpening(int stepNumber) {
        switch (stepNumber) {
            case 0:
                checkName();
                break;
            case 1:
                checkCost();
                vs.setStepAsCompleted(stepNumber);
                break;
            case 2:
                // As soon as the phone number step is open, we mark it as completed in order to show the "Continue"
                // button (We do it because this field is optional, so the user can skip it without giving any info)
             //   vs.setStepAsCompleted(2);
                // In this case, the instruction above is equivalent to:
                // verticalStepperForm.setActiveStepAsCompleted();
                break;
        }
    }

    @Override
    public void sendData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.setMessage(getString(R.string.vertical_form_stepper_form_sending_data_message));

        //I AM ASSUMING THAT I HAVE ALREADY DONE ALL THE CHECK
        if(name.getText().toString() != null){
            ex_name= name.getText().toString();
        }
        if(description.getText().toString() != null){
            ex_description = description.getText().toString();
        }
        if(cost.getText().toString()!= null){
            ex_cost = Double.parseDouble(cost.getText().toString());
        }


    }

    private void checkCost() {
        if(cost.length() > 0) {
            vs.setActiveStepAsCompleted();
        }
    }

    private void checkName() {
        if(name.length() > 20 ){
            String errorMessage = "The name must have between 2 and 15 characters";
            vs.setActiveStepAsUncompleted(errorMessage);
        }
        else {
            vs.setActiveStepAsCompleted();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


}