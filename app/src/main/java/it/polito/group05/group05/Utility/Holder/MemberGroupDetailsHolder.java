package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.aesthetic.Aesthetic;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.HelperClasses.AnimUtils;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;
import it.polito.group05.group05.Utility.HelperClasses.ImageUtils;

/**
 * Created by Marco on 17/05/2017.
 */

public class MemberGroupDetailsHolder extends GeneralHolder {
    private CircleImageView cv_userImage;
    private TextView tv_userName;
    private TextView tv_userBalance;
    private Button button_pay;
    private Button button_notify;
    private ProgressBar pb;
    private double value = 0;
    private Context context;

    public MemberGroupDetailsHolder(View itemView) {
        super(itemView);
        this.cv_userImage = (CircleImageView) itemView.findViewById(R.id.cv_userimage);
        this.tv_userName = (TextView) itemView.findViewById(R.id.tv_user_name);
        this.tv_userBalance = (TextView) itemView.findViewById(R.id.tv_user_balance);
        this.button_pay = (Button) itemView.findViewById(R.id.button_pay);
        this.button_notify = (Button) itemView.findViewById(R.id.button_notify);
        this.pb = (ProgressBar) itemView.findViewById(R.id.pb_loading_actions);
    }

    @Override
    public void setData(Object c, final Context context) {
        if (!(c instanceof UserDatabase)) return;
        this.context = context;
        final UserDatabase user = (UserDatabase) c;
        ImageUtils.LoadUserImageProfile(cv_userImage, context, user);
        tv_userName.setText(user.getName());
        tv_userBalance.setText("No debits/credits");
        button_pay.setVisibility(View.GONE);
        FirebaseDatabase.getInstance().getReference("expenses/" + Singleton.getInstance().getmCurrentGroup().getId())
                .orderByKey()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        value = 0;
                        tv_userBalance.setText("Already Payed!");
                        button_pay.setVisibility(View.GONE);
                        button_notify.setVisibility(View.GONE);
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            ExpenseDatabase expense = dsp.getValue(ExpenseDatabase.class);
                            if (expense == null) {
                                continue;
                            }
                            if (!expense.getMembers().containsKey(user.getId()))
                                continue;
                            if (expense.getMembers().containsKey(Singleton.getInstance().getCurrentUser().getId())) {
                                if (expense.getOwner().equals(Singleton.getInstance().getCurrentUser().getId()) &&
                                        expense.getMembers().containsKey(user.getId()))
                                    value -= expense.getMembers().get(user.getId());
                                else if (user.getId().equals(expense.getOwner()) &&
                                        !(boolean)expense.getPayed().get(Singleton.getInstance().getCurrentUser().getId()))
                                    value += expense.getMembers().get(Singleton.getInstance().getCurrentUser().getId());
                            }
                            if (value > 0) {
                                tv_userBalance.setText("You will receive " + String.format("%.2f €", value));
                                tv_userBalance.setTextColor(context.getResources().getColor(R.color.green_400));
                                button_pay.setVisibility(View.GONE);
                                button_notify.setVisibility(View.VISIBLE);
                            } else if (value < 0) {
                                tv_userBalance.setText("You must pay " + String.format("%.2f €", (-1) * value));
                                tv_userBalance.setTextColor(context.getResources().getColor(R.color.red_400));

                                button_pay.setVisibility(View.VISIBLE);
                                button_notify.setVisibility(View.GONE);

                            } else {
                                tv_userBalance.setText("Already Payed!");
                                button_pay.setVisibility(View.GONE);
                                button_notify.setVisibility(View.GONE);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Double d = Double.parseDouble(tv_userBalance.getText().toString());
                    String myId = Singleton.getInstance().getCurrentUser().getId();
                    DB_Manager.getInstance().reminderTo(Singleton.getInstance().getmCurrentGroup().getId(), myId, user.getId(), d);
                } catch (Exception c) {
                }
                Snackbar.make(itemView, "Reminder sent to " + user.getName(), Snackbar.LENGTH_INDEFINITE)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .show();

                final Handler handler = new Handler(context.getMainLooper());
                AnimUtils.exitReveal(button_notify);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(button_notify.getVisibility() == View.VISIBLE)
                        AnimUtils.enterRevealAnimation(button_notify);
                    }
                }, 30000);
            }
        });
        button_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pb.startAnimation(animation);
                payDebit(user);
            }
        });
    }


    private void payDebit(final UserDatabase user) {

        final String s = Singleton.getInstance().getCurrentUser().getId();
        //AnimUtils.exitReveal(button_pay);
        final ProgressBar pb = (ProgressBar) itemView.findViewById(R.id.pb_loading_actions);
        //AnimUtils.enterRevealAnimation(pb);
        //Azzero tutti i valori per cui sono in debito in tutte le spese.
        FirebaseDatabase.getInstance().getReference("expenses")
                .child(Singleton.getInstance().getmCurrentGroup().getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Double tmp = 0.0;
                        MaterialDialog dialog;
                        final List<ExpenseDatabase> expenseList = new ArrayList<ExpenseDatabase>();
                        List<String> expenseViewList = new ArrayList<String>();
                        final List<ExpenseDatabase> expensePayed = new ArrayList<ExpenseDatabase>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            ExpenseDatabase expense = data.getValue(ExpenseDatabase.class);
                            if (expense.getPayed().containsKey(s) && expense.getOwner().equals(user.getId())) {
                                if(!(boolean)expense.getPayed().get(s)) {
                                    expenseList.add(expense);
                                    expenseViewList.add(expense.getName() + " " + expense.getMembers().get(s) * -1 + " €");
                                }
                            }
                        }

                        dialog = new MaterialDialog.Builder(context)
                                .cancelable(true)
                                .title("Choose to Pay")
                                .content("Are you paying for?")
                                .items(expenseViewList)
                                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                        double var = 0;
                                        for(int i = 0; i < which.length; i++) {
                                            expensePayed.add(expenseList.get(which[i]));
                                            var+=expensePayed.get(i).getMembers().get(s);
                                            DB_Manager.getInstance().updateGroupFlow(s, expensePayed.get(i).getMembers().get(s));
                                            DB_Manager.getInstance().updateGroupFlow(user.getId(), (-1.00) * expensePayed.get(i).getMembers().get(s));
                                        }

                                        DB_Manager.getInstance().expensesPayment(s, Singleton.getInstance().getmCurrentGroup().getId(),  expensePayed);
                                        return true;
                                    }
                                })
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Snackbar.make(itemView, user.getName() + " need to convalidate the payment. Let's look back here later.", Snackbar.LENGTH_INDEFINITE)
                                                .setAction("Ok", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                    }
                                                })
                                                .show();
                                        AnimUtils.exitReveal(button_pay);
                                        dialog.dismiss();
                                    }
                                })
                                .positiveText("Ok")
                                .negativeText("Cancel")
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                        //AnimUtils.exitRevealAndShowSecond(pb, itemView.findViewById(R.id.iv_loading_end));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}