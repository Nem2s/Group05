package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.TYPE_EXPENSE;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;


/**
 * Created by user on 03/05/2017.
 */

public class ExpenseCardHolder extends GeneralHolder {

    CircleImageView civ;
    TextView tv,tv_debt;


    public ExpenseCardHolder(View itemView) {
        super(itemView);
        civ = (CircleImageView) itemView.findViewById(R.id.expense_member_image);
        tv = (TextView) itemView.findViewById(R.id.expense_member_name);
        tv_debt = (TextView) itemView.findViewById(R.id.expense_member_debt);

    }
    public void setData(Object u1,Context c){

        if(!(u1 instanceof User_expense)) return;
        User_expense u = (User_expense) u1;

        Glide.with(c)
                .load(u.getiProfile())
                .centerCrop()
                .placeholder(R.drawable.user_placeholder)
                .crossFade()
                .into(civ);

        tv.setText(u.getId());
        if(u instanceof User_expense) {
            Double c1 = u.getDebt();
            tv_debt.setText(String.format("%.2f",c));
            if(u.getExpense().getType()== TYPE_EXPENSE.MANDATORY)
                if(c1<0)
                    tv_debt.setTextColor(Color.RED);
                else if(c1>0)
                    tv_debt.setTextColor(Color.GREEN);
        }


    }
}