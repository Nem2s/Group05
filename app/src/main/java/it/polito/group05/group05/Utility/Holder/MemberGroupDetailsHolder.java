package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
                                else if (user.getId().equals(expense.getOwner()))
                                    value += expense.getMembers().get(Singleton.getInstance().getCurrentUser().getId());
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
                                    tv_userBalance.setTextColor(context.getResources().getColor(R.color.colorSecondaryText));
                                    button_pay.setVisibility(View.GONE);
                                    button_notify.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
        AnimUtils.exitReveal(button_pay);
        final ProgressBar pb = (ProgressBar) itemView.findViewById(R.id.pb_loading_actions);
        AnimUtils.enterRevealAnimation(pb);
        //Azzero tutti i valori per cui sono in debito in tutte le spese.
        FirebaseDatabase.getInstance().getReference("expenses")
                .child(Singleton.getInstance().getmCurrentGroup().getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Double tmp = 0.0;
                        MaterialDialog dialog;
                        List<ExpenseDatabase> expenseList = new ArrayList<ExpenseDatabase>();
                        List<String> expenseViewList = new ArrayList<String>();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            ExpenseDatabase expense = data.getValue(ExpenseDatabase.class);
                            if (expense.getMembers().containsKey(s) && expense.getOwner().equals(user.getId())) {
                                expenseList.add(expense);
                               /* FirebaseDatabase.getInstance().getReference("expenses")
                                        .child(Singleton.getInstance().getmCurrentGroup().getId())
                                        .child(expense.getId())
                                        .child("members")
                                        .child(s).setValue(0.0);
                                tmp += expense.getMembers().get(s);
                                //DB_Manager.getInstance().updateGroupFlow(s,expense.getMembers().get(s));
                                FirebaseDatabase.getInstance().getReference("expenses")
                                        .child(Singleton.getInstance().getmCurrentGroup().getId())
                                        .child(expense.getId())
                                        .child("members")
                                        .child(expense.getOwner())
                                        .setValue(expense.getMembers().get(expense.getOwner()) + expense.getMembers().get(s));
                                //DB_Manager.getInstance().updateGroupFlow(expense.getOwner(),(-1.00)*expense.getMembers().get(s));
*/
                            }
                        }
                        Integer [] res;
                        dialog = new MaterialDialog.Builder(context)
                                .cancelable(false)
                                .title("Choose to Pay")
                                .content("Are you paying for?")
                                .items(expenseList)
                                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                                    @Override
                                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                        //res = which;
                                        return false;
                                    }
                                })
                                .show();
                        DB_Manager.getInstance().updateGroupFlow(s, tmp);
                        DB_Manager.getInstance().updateGroupFlow(user.getId(), (-1.00) * tmp);
                        AnimUtils.exitRevealAndShowSecond(pb, itemView.findViewById(R.id.iv_loading_end));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
