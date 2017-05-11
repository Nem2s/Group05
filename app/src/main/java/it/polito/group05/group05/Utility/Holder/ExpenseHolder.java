package it.polito.group05.group05.Utility.Holder;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;


/**
 * Created by user on 03/05/2017.
 */

public class ExpenseHolder extends GeneralHolder{
    ImageView expense_image;
    TextView name;
    TextView price;
    RecyclerView rv;
    TextView description;
    CardView cv;
    Query ref;
    TextView menu;
    //Toolbar toolbar;
    public ExpenseHolder(View itemView) {
        super(itemView);
        this.expense_image = (ImageView) itemView.findViewById(R.id.expense_image);
        this.name= (TextView) itemView.findViewById(R.id.expense_name);
        this.price= (TextView) itemView.findViewById(R.id.expense_price);
        this.description=(TextView) itemView.findViewById(R.id.expense_owner);
        this.cv = (CardView) itemView.findViewById(R.id.card_expense);
        this.rv = (RecyclerView) itemView.findViewById(R.id.expense_rv);
        this.menu = (TextView) itemView.findViewById(R.id.textViewOptions);
    }
    public void setData(Object c, Context context){
        if(!(c instanceof ExpenseDatabase)) return;
        Expense expenseDatabase = new Expense((ExpenseDatabase) c);
        expense_image.setImageResource(R.drawable.idea);
        name.setText(expenseDatabase.getName());
        price.setText(String.format("%.2f €",expenseDatabase.getPrice()));
        Date date = new Date(System.currentTimeMillis());

        String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);


        for (String i : expenseDatabase.getMembers().keySet()){
            if(!(Singleton.getInstance().getmCurrentGroup().getMembers().get(i)instanceof UserDatabase)) continue;
            User_expense x = new User_expense((UserDatabase) Singleton.getInstance().getmCurrentGroup().getMembers().get(i));
                x.setCustomValue(expenseDatabase.getMembers().get(i));
                x.setExpense(expenseDatabase);
            if(x.getId().compareTo(expenseDatabase.getOwner())==0)
                description.setText("Posted by "+x.getName()+" on "+ ((expenseDatabase.getTimestamp()!=null)?expenseDatabase.getTimestamp(): timestamp));
            expenseDatabase.getUsersExpense().add(x);
        }
      if(!(expenseDatabase.isMandatory())) {
            price.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
          setupListener(cv,price,context,expenseDatabase);
        setupRecyclerViewExpense(rv, expenseDatabase,context);
    }
private void setupRecyclerViewExpense(RecyclerView rv, final Expense expenseDatabase, final Context context){
    RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(context).inflate(R.layout.expense_card_expansion,parent,false);
             GeneralHolder holder = new ExpenseCardHolder(rootView);
            return holder;

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            ((GeneralHolder)holder).setData(expenseDatabase.getUsersExpense().get(position),context);
        }

        @Override
        public int getItemCount() {
            return expenseDatabase.getUsersExpense().size();
        }
    };

    rv.setAdapter(adapter);
    rv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
    rv.setVisibility(View.GONE);
}
private void setupListener(CardView cv, final TextView price, final Context context, final Expense expense){

    int cnt=0;
    try {
        final PopupMenu popup = new PopupMenu(context, menu);
        //inflating menu from xml resource

    popup.inflate(R.menu.expense_card_menu);
    //adding click listener
    Menu popupMenu = popup.getMenu();
    MenuItem pay= popupMenu.findItem(R.id.action_pay);
    MenuItem subscribe= popupMenu.findItem(R.id.action_subscribe);
    MenuItem delete= popupMenu.findItem(R.id.action_delete);
    if(expense.getOwner().compareTo(Singleton.getInstance().getCurrentUser().getId())!=0) {
        delete.setVisible(false);
        cnt++;
    }
    if(expense.isMandatory()) {
        subscribe.setVisible(false);
        cnt++;
        if(expense.getMembers().get(Singleton.getInstance().getCurrentUser().getId())!=null)
            if(expense.getMembers().get(Singleton.getInstance().getCurrentUser().getId()).compareTo(0.0)==0) {
                cnt++;
                pay.setVisible(false);
            }

    }
    if(!expense.isMandatory() || expense.getOwner().compareTo(Singleton.getInstance().getCurrentUser().getId())==0) {
        pay.setVisible(false);
        cnt++;
    }

    if(cnt>=3)
        menu.setVisibility(View.INVISIBLE);
    else
    menu.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //creating a popup menu
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_delete:
                            //handle menu1 click
                                FirebaseDatabase.getInstance().getReference("expenses")
                                    .child(Singleton.getInstance().getmCurrentGroup().getId())
                                    .child(expense.getId()).removeValue();
                                DB_Manager.getInstance().updateGroupFlow(new HashMap<String, Double>(expense.getMembers()));

                                break;
                            case R.id.action_pay:
                            //handle menu2 click
                                String s = Singleton.getInstance().getCurrentUser().getId();
                            FirebaseDatabase.getInstance().getReference("expenses")
                                    .child(Singleton.getInstance().getmCurrentGroup().getId())
                                    .child(expense.getId())
                                    .child("members")
                                    .child(s).setValue(0.0);
                                DB_Manager.getInstance().updateGroupFlow(s,expense.getMembers().get(s));
                            FirebaseDatabase.getInstance().getReference("expenses")
                                    .child(Singleton.getInstance().getmCurrentGroup().getId())
                                    .child(expense.getId())
                                    .child("members")
                                    .child(expense.getOwner())
                                    .setValue(expense.getMembers().get(expense.getOwner()) + expense.getMembers().get(s));
                                DB_Manager.getInstance().updateGroupFlow(expense.getOwner(),(-1.00)*expense.getMembers().get(s));
                            break;
                        case R.id.action_subscribe:
                            final AlertDialog dialog = new AlertDialog.Builder(context).create();
                            View s1 = LayoutInflater.from(context).inflate(R.layout.layout, null, false);
                            dialog.setView(s1);
                            dialog.setTitle("Choose your amount");
                            dialog.setCanceledOnTouchOutside(true);
                            dialog.setButton(Dialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Double d = Double.valueOf(((EditText) dialog.findViewById(R.id.expense_amount_not_mandatory)).getText().toString());
                                    if(d > expense.getPrice())
                                        d=expense.getPrice();
                                    d = d * (-1.00);
                                    FirebaseDatabase.getInstance().getReference("expenses")
                                            .child(Singleton.getInstance().getmCurrentGroup().getId())
                                            .child(expense.getId())
                                            .child("members")
                                            .child(Singleton.getInstance().getCurrentUser().getId()).setValue(d);
                                }
                            });
                            dialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialog.cancel();
                                }
                            });
                            dialog.show();
                            //handle menu3 click
                            break;
                    }
                    return false;
                }
            });
            //displaying the popup
            popup.show();

        }
    });
    }
    catch(Exception c ){

    }
        cv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(description.getVisibility()==View.GONE) {
                description.setVisibility(View.VISIBLE);
               // file.setVisibility(View.VISIBLE);
                rv.setVisibility(View.VISIBLE);

            }
            else {
                description.setVisibility(View.GONE);
                rv.setVisibility(View.GONE);
                //file.setVisibility(View.GONE);
            }
        }
    });




}

}
