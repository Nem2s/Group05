package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.Double2;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;

/**
 * Created by andre on 10-May-17.
 */

public class PersonHolder extends GeneralHolder {

    CardView cv;
    RelativeLayout rl_person;
    RelativeLayout rl;
    ImageView img_profile;
    TextView user_name;
    TextView balance_text;
    TextView balance;
    TextView total_balance_text;
    TextView total_balance;
    TextView expenses_text;
    TextView expenses;

    public PersonHolder(View v){
        super(v);
        this.rl = (RelativeLayout) itemView.findViewById(R.id.total_balace);
        this.rl_person = (RelativeLayout) itemView.findViewById(R.id.rl_person);
        rl.setVisibility(View.GONE);
        this.cv = (CardView) itemView.findViewById(R.id.card_person);
        this.img_profile = (ImageView) itemView.findViewById(R.id.person_image);
        this.user_name = (TextView) itemView.findViewById(R.id.person_name);
        this.balance_text = (TextView) itemView.findViewById(R.id.person_personal_credit_debit_text);
        this.balance = (TextView) itemView.findViewById(R.id.person_personal_credit_debit);
        this.total_balance_text = (TextView) itemView.findViewById(R.id.info_total_credit_debit_text);
        this.total_balance = (TextView) itemView.findViewById(R.id.info_total_credit_debit);
        this.expenses_text = (TextView) itemView.findViewById(R.id.info_total_expenses_text);
        this.expenses = (TextView) itemView.findViewById(R.id.info_total_expenses);

    }

    @Override
    public void setData(Object c, Context context) {

    }

    public void setData(Object c, Context context,
                        Map<String, Double> usersBalance,
                        Map<String, Double> totalExpenses,
                        Map<String, Double> usersTotalBalance) {
        if (!(c instanceof UserDatabase)) return;
        UserDatabase u = (UserDatabase) c;
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference("users").child(u.getId()).child(u.getiProfile()))
                .centerCrop()
                .into(img_profile);
        user_name.setText(u.getName());
        if(usersBalance.containsKey(u.getId()))
        {
            balance.setText(String.format("%.2f €",Math.abs(usersBalance.get(u.getId()))));
            if(usersBalance.get(u.getId()) > 0){
                balance_text.setText("You owe: ");
                balance.setTextColor(context.getResources().getColor(R.color.debit_red));
            }
            else if (usersBalance.get(u.getId()) < 0) {
                balance_text.setText("Owes you: ");
                balance.setTextColor(context.getResources().getColor(R.color.credit_gree));
            }
            else {
                balance.setText("");
                balance_text.setText("Break even");
            }
        }
        else {
            balance.setText("");
            balance_text.setText("Break even");
        }

        if(usersTotalBalance.containsKey(u.getId())) {
            total_balance.setText(String.format("%.2f €", Math.abs(Double.parseDouble(usersTotalBalance.get(u.getId()).toString()))));
            if (Double.parseDouble(usersTotalBalance.get(u.getId()).toString()) < 0) {
                total_balance_text.setText("Owes to group: ");
                total_balance.setTextColor(context.getResources().getColor(R.color.debit_red));
            } else if (Double.parseDouble(usersTotalBalance.get(u.getId()).toString()) > 0) {
                total_balance_text.setText("Group owe: ");
                total_balance.setTextColor(context.getResources().getColor(R.color.credit_gree));
            } else {
                total_balance_text.setText("Break even");
                total_balance.setText("");
            }
        }
        else {
            total_balance_text.setText("Break even");
            total_balance.setText("");
        }

            if (totalExpenses.containsKey(u.getId())) {
                expenses.setText(String.format("%.2f €", Math.abs(Double.parseDouble(totalExpenses.get(u.getId()).toString()))));
                expenses_text.setText("Total expenses paid: ");
            } else {
                expenses.setText("");
                expenses_text.setText("No expenses payed");
            }


        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rl.getVisibility() == View.GONE) {
                    rl.setVisibility(View.VISIBLE);
                } else {
                    rl.setVisibility(View.GONE);
                }
            }
        });
    }


}
