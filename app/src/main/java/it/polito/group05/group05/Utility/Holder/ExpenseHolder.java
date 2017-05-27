package it.polito.group05.group05.Utility.Holder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
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
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    TextView owner, timestamp, nf;
    RecyclerView rv;
    RelativeLayout rel;
    TextView description;
    ImageView clip,expand;
    CardView cv;
    Query ref;
    TextView menu;
    ImageView calendar;
    boolean filePresent;
    String nomeF;


    public ExpenseHolder(View itemView) {
        super(itemView);
        this.expense_image = (ImageView) itemView.findViewById(R.id.expense_image);
        this.name= (TextView) itemView.findViewById(R.id.expense_name);
        this.price= (TextView) itemView.findViewById(R.id.expense_price);
        this.owner = (TextView) itemView.findViewById(R.id.owner);
        this.timestamp = (TextView) itemView.findViewById(R.id.timestamp);
        this.cv = (CardView) itemView.findViewById(R.id.card_expense);
        this.rv = (RecyclerView) itemView.findViewById(R.id.expense_rv);
        this.menu = (TextView) itemView.findViewById(R.id.textViewOptions);
        this.calendar = (ImageView) itemView.findViewById(R.id.calendar);
        this.rel = (RelativeLayout) itemView.findViewById(R.id.rel_file);
        this.rel.setVisibility(View.GONE);
        this.nf = (TextView) itemView.findViewById(R.id.name_File);
        this.expand = (ImageView) itemView.findViewById(R.id.expand);
        this.clip = (ImageView) itemView.findViewById(R.id.clip);
        filePresent = false;
    }
    public void setData(Object c, final Context context){
        setData(c, context, View.GONE);
    }

    public void setData(Object c, final Context context, int type) {
        if(!(c instanceof ExpenseDatabase)) return;
        final Expense expenseDatabase = new Expense((ExpenseDatabase) c);
        expense_image.setImageResource(R.drawable.idea);
        name.setText(expenseDatabase.getName());
        price.setText(String.format("%.2f €",expenseDatabase.getPrice()));
        Date date = new Date(expenseDatabase.getTimestamp());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.timestamp.setText(sdf.format(date));

        if(expenseDatabase.getFile() != null){
            this.nf.setText(expenseDatabase.getFile());}
        else nf.setText("");

        String id = Singleton.getInstance().getCurrentUser().getId();
        for (String i : expenseDatabase.getMembers().keySet()){
            /**Aggiunto da andrea**/
            if(expenseDatabase.getMembers().containsKey(Singleton.getInstance().getCurrentUser().getId()) && expenseDatabase.getMembers().get(i) > 0 ) {
                if (Singleton.getInstance().getUsersBalance().containsKey(i))
                    Singleton.getInstance().getUsersBalance().put(i, Singleton.getInstance().getUsersBalance().get(i) + expenseDatabase.getMembers().get(Singleton.getInstance().getCurrentUser().getId()));
                else
                    Singleton.getInstance().getUsersBalance().put(i, expenseDatabase.getMembers().get(Singleton.getInstance().getCurrentUser().getId()));
            }

            if(!(Singleton.getInstance().getmCurrentGroup().getMembers().get(i)instanceof UserDatabase)) continue;
            User_expense x = new User_expense((UserDatabase) Singleton.getInstance().getmCurrentGroup().getMembers().get(i));
                x.setCustomValue(expenseDatabase.getMembers().get(i));
                x.setExpense(expenseDatabase);
            if (x.getId().compareTo(expenseDatabase.getOwner()) == 0) {
                owner.setText(x.getId().compareTo(id) == 0 ? "You" : x.getName());
            }
            expenseDatabase.getUsersExpense().add(x);
        }

        setupListener(cv, price, context, expenseDatabase);
        setupRecyclerViewExpense(rv, expenseDatabase, context, type);


    }

    public void setData(Object c, final Context context, String eid) {
        if (!(c instanceof ExpenseDatabase)) return;
        final Expense expenseDatabase = new Expense((ExpenseDatabase) c);
        //
        if (eid != null)
            if (eid.equals(expenseDatabase.getId()))
                setData(c, context, View.VISIBLE);
            else
                setData(c, context, View.GONE);
    }

    private void setupRecyclerViewExpense(RecyclerView rv, final Expense expenseDatabase, final Context context, int visibility) {
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
        rv.setVisibility(visibility);
}
private void setupListener(CardView cv, final TextView price, final Context context, final Expense expense){

    int cnt=0;
    try {
        final PopupMenu popup = new PopupMenu(context, menu);

    popup.inflate(R.menu.expense_card_menu);
    //adding click listener
    Menu popupMenu = popup.getMenu();
    MenuItem pay= popupMenu.findItem(R.id.action_pay);
    MenuItem subscribe= popupMenu.findItem(R.id.action_subscribe);
    MenuItem delete= popupMenu.findItem(R.id.action_delete);
        MenuItem download = popupMenu.findItem(R.id.file_download);
        subscribe.setVisible(false);
    if(expense.getOwner().compareTo(Singleton.getInstance().getCurrentUser().getId())!=0) {
        delete.setVisible(false);

    } else {
        delete.setVisible(true);
    }
        if (expense.getOwner().compareTo(Singleton.getInstance().getCurrentUser().getId()) == 0) {
        pay.setVisible(false);
        } else {
            pay.setVisible(true);
        }

        if (expense.getFile() == null) {
        download.setVisible(false);
        } else {
            download.setVisible(true);
    }

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
                            case R.id.file_download:
                                try {
                                    DB_Manager.getInstance().setContext(context).fileDownload(expense.getId(), expense.getFile());
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                break;
                    }
                    return false;
                }
            });
            popup.show();
        }
    });
    }
    catch(Exception c ){
       // Toast.makeText(context, "Error"+c.getMessage(), Toast.LENGTH_SHORT).show();
    }
        cv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (rv.getVisibility() == View.GONE) {
                rv.setVisibility(View.VISIBLE);
                expand.setImageResource(R.drawable.ic_expand_less);
                if(expense.getFile() != null) {
                    rel.setVisibility(View.VISIBLE);
                }
            }
            else {
                expand.setImageResource(R.drawable.ic_expand_more);
                rv.setVisibility(View.GONE);
                rel.setVisibility(View.GONE);
            }


        }
    });

    rel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                DB_Manager.getInstance().fileDownload(expense.getId(), expense.getFile());
                File f = new File(Environment.getExternalStorageDirectory().getPath() + "/FileAppPoli/"+ expense.getFile());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(f),"*/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(intent);
            } catch (FileNotFoundException e) {
                Snackbar.make(v, "File not found", Snackbar.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    });

}

}
