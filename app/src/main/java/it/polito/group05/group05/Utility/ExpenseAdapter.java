package it.polito.group05.group05.Utility;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Expense;

/**
 * Created by antonino on 03/04/2017.
 */

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapterHolder> {

    List<Expense> list;
    LayoutInflater li;
    Context c;

    public ExpenseAdapter(Context c, List<Expense> data){
        li = LayoutInflater.from(c);
        this.c = c;
        list=data;
    }
    @Override
    public ExpenseAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = li.inflate(R.layout.item_expense,parent,false);
        ExpenseAdapterHolder holder = new ExpenseAdapterHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ExpenseAdapterHolder holder, int position) {
        holder.setData(list.get(position),c);
    }



    @Override
    public int getItemCount() {
        return list.size();
    }
}



class ExpenseAdapterHolder extends RecyclerView.ViewHolder {
    ImageView expense_image;
    TextView name;
    TextView price;
    RecyclerView rv;
    TextView description;
    CardView cv;



    public ExpenseAdapterHolder(View itemView) {
        super(itemView);
        expense_image = (ImageView) itemView.findViewById(R.id.expense_image);
        name= (TextView) itemView.findViewById(R.id.expense_name);
        price= (TextView) itemView.findViewById(R.id.expense_price);
        description=(TextView) itemView.findViewById(R.id.expense_owner);
        // holder.description.setText("drbgbdfvdjvbksd\nujgub\niuguyguy\n");
         cv = (CardView) itemView.findViewById(R.id.card_expense);
        //  View rootView = LayoutInflater.from(context).inflate(R.layout.expense_card_expansion,parent,false);
        rv = (RecyclerView) itemView.findViewById(R.id.expense_rv);




    }




    public void setData(Expense expense,Context context){


        expense_image.setImageResource(Integer.parseInt(expense.getImage()));

        //  holder.expense_image.setImageResource(R.drawable.facebook);
        name.setText(expense.getName());
        description.setText("Posted by "+expense.getOwner().getUser_name()+" on "+expense.getTimestamp().toString());
      //  description.setText("kjebanwineineoxowxmoxownxe");
        LinearLayoutManager l = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
        rv.setLayoutManager(l);
        price.setText(String.format("%.2f €",expense.getPrice()));
        ExpenseCardAdapter adapter = new ExpenseCardAdapter(context,expense.getPartecipants());
        rv.setAdapter(adapter);
        rv.setAdapter(adapter);
        rv.setVisibility(View.GONE);
        description.setVisibility(View.GONE);

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(description.getVisibility()==View.GONE) {
                    description.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.VISIBLE);

                }
                else {
                    description.setVisibility(View.GONE);
                    rv.setVisibility(View.GONE);
                }
            }
        });

    }



}
