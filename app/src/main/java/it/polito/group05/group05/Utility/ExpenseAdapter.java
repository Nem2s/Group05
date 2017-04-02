package it.polito.group05.group05.Utility;

import android.content.Context;
import android.support.design.widget.Snackbar;
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

public class ExpenseAdapter extends ArrayAdapter<Expense> {
    Context context;

    public ExpenseAdapter(Context context, List<Expense> objects) {
        super(context,0, objects);
        this.context = context;
    }


    private static class ViewHolder {
        ImageView expense_image;
        TextView name;
        TextView price;

    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Expense expense = getItem(position);
        ViewHolder holder=null;


        if (convertView==null){
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.item_expense, parent, false);
            holder = new ViewHolder();
            holder.expense_image = (ImageView) convertView.findViewById(R.id.expense_image);
            holder.name= (TextView) convertView.findViewById(R.id.expense_name);
            holder.price= (TextView) convertView.findViewById(R.id.expense_price);
            convertView.setTag(holder);
        }
        if(expense!=null){
            Picasso
                    .with(context)
                    .load(R.drawable.facebook)
                    .placeholder(R.drawable.facebook)
                    .error(R.drawable.ic_visibility_off)
                    .transform(new PicassoRoundTransform())
                    .into(holder.expense_image);

            holder.name.setText(expense.getName());
            holder.price.setText(String.format("%.2f €",expense.getPrice()));
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar
                            .make(v, "To be implemented...", Snackbar.LENGTH_SHORT)
                            .setAction("Ok", null)
                            .show();
                }
            });



        }

        final View finalConvertView = convertView;
        return finalConvertView;
    }


}
