package it.polito.group05.group05.Utility;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.User;

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
