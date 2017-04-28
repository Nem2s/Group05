package it.polito.group05.group05.Utility;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Expense;

/**
 * Created by antonino on 31/03/2017.
 */

public class ExpenseAdapterR extends ArrayAdapter<Expense> {
    Context context;
    List<Expense> obj;

    public ExpenseAdapterR(Context context, List<Expense> objects) {
        super(context,0, objects);
        this.context = context;
        this.obj = objects;

    }


    private static class ViewHolder {
        ImageView expense_image;
        TextView name;
        TextView price;
        RecyclerView rv;
        TextView description;
        CardView cv;
    }
    public int getCout(){return obj.size();}
    public Expense getItem(int position){return obj.get(position);}
    public long getItemId(int position){return Long.valueOf(obj.get(position).getId());}


    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Expense expense = getItem(position);
        final ExpenseAdapterR.ViewHolder holder;
        CardView cv;

        if (convertView==null){
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.item_expense, parent, false);
            holder = new ViewHolder();
            holder.expense_image = (ImageView) convertView.findViewById(R.id.expense_image);
            holder.name= (TextView) convertView.findViewById(R.id.expense_name);
            holder.price= (TextView) convertView.findViewById(R.id.expense_price);
             holder.cv = (CardView) convertView.findViewById(R.id.card_expense);
            holder.rv = (RecyclerView) convertView.findViewById(R.id.expense_rv);

            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(holder.rv.getVisibility()==View.GONE) {
                        holder.description.setVisibility(View.VISIBLE);
                        holder.rv.setVisibility(View.VISIBLE);

                    }
                    else {
                        holder.description.setVisibility(View.GONE);
                        holder.rv.setVisibility(View.GONE);
                    }
                }
            });
            convertView.setTag(holder);
        }
        else
            holder = (ExpenseAdapterR.ViewHolder)convertView.getTag();
        if(expense!=null){
            Picasso
                    .with(context)
                    .load(Integer.parseInt(expense.getImage()))
                    .placeholder(R.drawable.facebook)
                    .error(R.drawable.ic_visibility_off)
                    .transform(new PicassoRoundTransform())
                    .into(holder.expense_image);
          //  holder.expense_image.setImageResource(R.drawable.facebook);
            holder.name.setText(expense.getName());
            holder.description.setText("Posted by "+expense.getOwner().getUser_name()+" on "+expense.getTimestamp().toString());
            LinearLayoutManager l = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
            holder.rv.setLayoutManager(l);
            holder.price.setText(String.format("%.2f €",expense.getPrice()));
            ExpenseCardAdapter adapter = new ExpenseCardAdapter(context,expense.getPartecipants());
            holder.rv.setAdapter(adapter);
            holder.rv.setVisibility(View.GONE);
            holder.description.setVisibility(View.GONE);

        }
        final View finalConvertView = convertView;
        return finalConvertView;
    }


}
