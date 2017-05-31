package it.polito.group05.group05.Utility.Holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.ExpenseDetailsActivity;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;
import it.polito.group05.group05.Utility.HelperClasses.ImageUtils;


/**
 * Created by user on 03/05/2017.
 */

public class ExpenseHolder extends GeneralHolder{
    ImageView expense_image;
    CircleImageView cv_owner;
    ImageView iv_bookmark;
    TextView name;
    TextView price;
    TextView owner, timestamp;
    CardView cv;
    Query ref;
    ImageView calendar;
    Context context;
    List<UserDatabase> members = new ArrayList<>();

    private int LEFT_OFFSET;
    private int TOP_OFFSET;
    private int WIDTH;
    private int HEIGHT;

    public ExpenseHolder(View itemView) {
        super(itemView);
        this.expense_image = (ImageView) itemView.findViewById(R.id.cart_image);
        this.name= (TextView) itemView.findViewById(R.id.expense_name);
        this.price= (TextView) itemView.findViewById(R.id.expense_price);
        this.owner = (TextView) itemView.findViewById(R.id.owner);
        this.timestamp = (TextView) itemView.findViewById(R.id.timestamp);
        this.cv = (CardView) itemView.findViewById(R.id.card_expense);
        this.calendar = (ImageView) itemView.findViewById(R.id.calendar);
        this.cv_owner = (CircleImageView) itemView.findViewById(R.id.cv_ownerimage);




    }
    public void setData(Object c, final Context context){
        setData(c, context, View.GONE);
    }

    public void setData(Object c, final Context context, int type) {
        if(!(c instanceof ExpenseDatabase)) return;
        this.context = context;
        final Expense expenseDatabase = new Expense((ExpenseDatabase) c);
        loadOwnerImage(expenseDatabase.getOwner());
        name.setText(expenseDatabase.getName());
        price.setText(String.format("%.2f €",expenseDatabase.getPrice()));
        Date date = new Date(expenseDatabase.getTimestamp());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.timestamp.setText(sdf.format(date));

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
                //iv_bookmark.setVisibility(x.getId().compareTo(id) == 0 ? View.VISIBLE : View.INVISIBLE);
            }
            expenseDatabase.getUsersExpense().add(x);
        }

        setupListener(cv, price, context, expenseDatabase);



    }


    private void loadOwnerImage(String owner) {
        FirebaseDatabase.getInstance().getReference("users").child(owner).child("userInfo").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ImageUtils.LoadUserImageProfile(cv_owner, context, dataSnapshot.getValue(UserDatabase.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    /*private void setupRecyclerViewExpense(RecyclerView rv, final Expense expenseDatabase, final Context context, int visibility) {
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
}*/
    private void setupListener(final CardView cv, final TextView price, final Context context, final Expense expense) {

   /* int cnt=0;
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
    }*/
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent((Activity) context, ExpenseDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable("map", (Serializable) expense.getMembers());
                extras.putString("title", expense.getName());
                extras.putString("id", expense.getId());
                extras.putString("owner", expense.getOwner());
                extras.putString("price", expense.getPrice().toString());
                extras.putLong("timestamp", expense.getTimestamp());
                if(expense.getFile()!= null){
                    extras.putString("file", expense.getFile());
                }
                int location[] = new int[2];
                cv.getLocationInWindow(location);
                LEFT_OFFSET = location[0];
                TOP_OFFSET = location[1];
                WIDTH = cv.getWidth();
                HEIGHT = cv.getHeight();
                extras.putInt("left_offset", LEFT_OFFSET);
                extras.putString("expenseId", expense.getId());
                extras.putInt("top_offset", TOP_OFFSET);
                extras.putInt("width", WIDTH);
                extras.putInt("height", HEIGHT);
                extras.putSerializable("payed", (Serializable) expense.getPayed());
                i.putExtras(extras);
                context.startActivity(i);

            }
        });

   /* rel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                DB_Manager.getInstance().fileDownload(expense.getId(), expense.getFile());
                File f = new File(Environment.getExternalStorageDirectory().getPath() + "/FileAppPoli/"+ expense.getFile());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(f),"**///*");
              /*  intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(intent);
            } catch (FileNotFoundException e) {
                Snackbar.make(v, "File not found", Snackbar.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    });*/

    }

}