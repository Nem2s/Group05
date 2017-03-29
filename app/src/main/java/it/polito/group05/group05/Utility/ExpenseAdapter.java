package it.polito.group05.group05.Utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.view.ViewGroup;
import java.util.List;
import org.w3c.dom.Text;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import it.polito.group05.group05.R;
/**
 * Created by andrea on 25-Mar-17.
 */

public class ExpenseAdapter  extends ArrayAdapter<Expense> {

    public ExpenseAdapter(Context context, List<Expense> objects) {super(context,0, objects);}

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Expense expense = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.expense_item_sample, parent, false);
        }
        TextView name = (TextView)convertView.findViewById(R.id.tv_expense_name);
        TextView amount = (TextView)convertView.findViewById(R.id.tv_expense_amount);
        TextView owner = (TextView)convertView.findViewById(R.id.tv_expense_owner);
        ImageView e_image = (ImageView)convertView.findViewById(R.id.expense_image);

        name.setText(expense.getName());
        amount.setText(Double.toString(expense.getAmount())+" €");
        owner.setText("From: " + expense.getOwner());
        e_image = expense.getE_image();
        return convertView;
    }
}






