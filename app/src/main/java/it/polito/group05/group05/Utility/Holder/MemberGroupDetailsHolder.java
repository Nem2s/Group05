package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.ui.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
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
    private double value = 0;

    public MemberGroupDetailsHolder(View itemView) {
        super(itemView);
        this.cv_userImage = (CircleImageView)itemView.findViewById(R.id.cv_userimage);
        this.tv_userName = (TextView)itemView.findViewById(R.id.tv_user_name);
        this.tv_userBalance = (TextView)itemView.findViewById(R.id.tv_user_balance);
        this.button_pay = (Button)itemView.findViewById(R.id.button_pay);
        this.button_notify = (Button)itemView.findViewById(R.id.button_notify);
    }

    @Override
    public void setData(Object c, final Context context) {
        if(!(c instanceof UserDatabase)) return;

        final UserDatabase user = (UserDatabase)c;
        ImageUtils.LoadUserImageProfile(cv_userImage, context, user);
        tv_userName.setText(user.getName());

        FirebaseDatabase.getInstance().getReference("expenses/" + Singleton.getInstance().getmCurrentGroup().getId())
                .orderByKey()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ExpenseDatabase expense = dataSnapshot.getValue(ExpenseDatabase.class);
                        if(expense == null) {
                            tv_userBalance.setText("No debits/credits");
                            button_pay.setVisibility(View.GONE);
                            return;
                        }
                        if (expense.getMembers().containsKey(user.getId())) {
                            value += expense.getMembers().get(user.getId());
                            if (value < 0) {
                                tv_userBalance.setText("You will receive " + String.format("%.2f €", (-1) * value));
                                tv_userBalance.setTextColor(context.getResources().getColor(R.color.green_400));
                                button_pay.setVisibility(View.GONE);
                                button_notify.setVisibility(View.VISIBLE);
                            } else if (value > 0) {
                                tv_userBalance.setText("You must pay " + String.format("%.2f €", value));
                                tv_userBalance.setTextColor(context.getResources().getColor(R.color.red_400));
                            } else {
                                tv_userBalance.setText("No debits/credits");
                                button_pay.setVisibility(View.GONE);
                            }
                        } else {
                            tv_userBalance.setText("No debits/credits");
                            button_pay.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    public void setData(Object c,Map<String, Double> usersBalance, Context context) {
        if(!(c instanceof UserDatabase)) return;
        final UserDatabase user = (UserDatabase)c;
        ImageUtils.LoadUserImageProfile(cv_userImage, context, user);
        tv_userName.setText(user.getName());
        if(usersBalance.containsKey(user.getId())) {
            double value = usersBalance.get(user.getId());
            if (value < 0) {
                tv_userBalance.setText("Debit with you by " + String.format("%.2f €",  (-1)*value));
                tv_userBalance.setTextColor(context.getResources().getColor(R.color.green_400));
                button_pay.setVisibility(View.GONE);
                button_notify.setVisibility(View.VISIBLE);
            } else if (value > 0) {
                tv_userBalance.setText("You must pay for " + String.format("%.2f €", value));
                tv_userBalance.setTextColor(context.getResources().getColor(R.color.red_400));
            }else {
                tv_userBalance.setText("No debits/credits");
                button_pay.setVisibility(View.GONE);
            }
        } else {
            tv_userBalance.setText("No debits/credits");
            button_pay.setVisibility(View.GONE);
        }


    }
}
