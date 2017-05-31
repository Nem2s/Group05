package it.polito.group05.group05.Utility.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.Holder.MemberExpenseHolder;

/**
 * Created by Marco on 27/05/2017.
 */

public class MemberExpenseAdapter extends RecyclerView.Adapter<MemberExpenseHolder> {

    private List<UserDatabase> users;
    Context context;
    Map<String, Double> expenseDatabase;

    public MemberExpenseAdapter(List<UserDatabase> users, Context context, Map<String, Double> e) {
        this.users = users;
        this.context = context;
        this.expenseDatabase = e;
    }

    @Override
    public MemberExpenseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_expense_details, parent, false);
        return new MemberExpenseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MemberExpenseHolder holder, int position) {
        holder.setData(users.get(position), context, expenseDatabase);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}