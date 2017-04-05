package it.polito.group05.group05.Utility;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import java.util.List;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.TYPE_EXPENSE;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;

/**
 * Created by antonino on 03/04/2017.
 */

public class ExpenseCardAdapter extends RecyclerView.Adapter<ExpenseCardAdapterHolder> {

    List<User> list;
    LayoutInflater li;


    public ExpenseCardAdapter(Context c, List<User> data){
        li = LayoutInflater.from(c);
        list=data;
    }
    @Override
    public ExpenseCardAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = li.inflate(R.layout.expense_card_expansion,parent,false);
        ExpenseCardAdapterHolder holder = new ExpenseCardAdapterHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ExpenseCardAdapterHolder holder, int position) {

        holder.setData(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class ExpenseCardAdapterHolder extends RecyclerView.ViewHolder {
    CircularImageView civ;
    TextView tv,tv_debt;




    public ExpenseCardAdapterHolder(View itemView) {
        super(itemView);
        civ = (CircularImageView) itemView.findViewById(R.id.expense_member_image);
        tv = (TextView) itemView.findViewById(R.id.expense_member_name);
        tv_debt = (TextView) itemView.findViewById(R.id.expense_member_debt);
    }
    public void setData(User u){

        civ.setImageResource(Integer.parseInt(u.getProfile_image()));
        tv.setText(u.getId().compareTo(Singleton.getInstance().getId())!=0 ? u.getUser_name():"You");
        if(u instanceof User_expense) {
            Double c = ((User_expense) u).getDebt();
            tv_debt.setText(String.format("%.2f",c));
            if(((User_expense) u).getExpense().getType()== TYPE_EXPENSE.MANDATORY)
            if(c<0)
                tv_debt.setTextColor(Color.RED);
            else if(c>0)
                tv_debt.setTextColor(Color.GREEN);
        }
    }



}
