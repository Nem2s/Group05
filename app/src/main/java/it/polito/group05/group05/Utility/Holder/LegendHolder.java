package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.HelperClasses.ImageUtils;

/**
 * Created by Marco on 23/05/2017.
 */

public class LegendHolder extends GeneralHolder {


    private final FloatingActionButton fab_group_color;
    private TextView tv_groupName;
    private TextView tv_groupBalance;

    public LegendHolder(View itemView) {
        super(itemView);
        fab_group_color = (FloatingActionButton)itemView. findViewById(R.id.fab_group_color);
        tv_groupName = (TextView)itemView.findViewById(R.id.tv_group_name);
        tv_groupBalance = (TextView)itemView.findViewById(R.id.tv_group_balance);

    }

    @Override
    public void setData(Object c, Context context) {


    }

    public void setData(Object groupDatabase, Integer integer, Context context) {
        GroupDatabase g = (GroupDatabase)groupDatabase;
        int color  = integer;

        fab_group_color.setBackgroundTintList(ColorStateList.valueOf(color));
        tv_groupName.setText(g.getName());

        float v = Float.valueOf(g.getMembers().get(Singleton.getInstance().getCurrentUser().getId()).toString());
        String s = String.format("%.2f", v);
        String res;
        if (v > 0) {
            res = "+" + s + " €";
            tv_groupBalance.setText(res);
            tv_groupBalance.setTextColor(context.getResources().getColor(R.color.green_400));
        }

        else {
            res = s + " €";
            tv_groupBalance.setText(res);
            tv_groupBalance.setTextColor(context.getResources().getColor(R.color.red_400));
        }
    }
}
