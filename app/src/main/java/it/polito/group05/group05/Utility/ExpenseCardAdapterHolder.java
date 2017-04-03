package it.polito.group05.group05.Utility;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.User;

/**
 * Created by antonino on 03/04/2017.
 */

public class ExpenseCardAdapterHolder extends RecyclerView.ViewHolder {
    CircularImageView civ;
    TextView tv;




    public ExpenseCardAdapterHolder(View itemView) {
        super(itemView);
        civ = (CircularImageView) itemView.findViewById(R.id.expense_member_image);
        tv = (TextView) itemView.findViewById(R.id.expense_member_name);

    }
public void setData(User u){

    civ.setImageResource(Integer.parseInt(u.getProfile_image()));
    tv.setText(u.getUser_name());

}



}
