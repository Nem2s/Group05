package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

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
    private ExpenseCardHolder(View itemView, CircleImageView civ, TextView tv, TextView tv_debt) {
        super(itemView);
        this.civ = civ;
        this.tv = tv;
        this.tv_debt =tv_debt;
    }

    public static it.polito.group05.group05.Utility.Holder.ExpenseCardHolder newInstance(View itemView) {

        CircleImageView civ = (CircleImageView) itemView.findViewById(R.id.expense_member_image);
        TextView tv = (TextView) itemView.findViewById(R.id.expense_member_name);
        TextView tv_debt = (TextView) itemView.findViewById(R.id.expense_member_debt);
        return new it.polito.group05.group05.Utility.Holder.ExpenseCardHolder(itemView,civ,tv,tv_debt);
    }
    public void setData(Object u1,Context c){

        if(!(u1 instanceof User_expense)) return;
        User_expense u = (User_expense) u1;
        //civ.setImageBitmap(u.getiProfile());

        tv.setText(u.getId().compareTo(Singleton.getInstance().getId())!=0 ? u.getName():"You");
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