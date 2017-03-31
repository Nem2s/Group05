import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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
    public View getView(final int position, View convertView, final ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.item_expense,parent,false);


        return null;
    }


}
