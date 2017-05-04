package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;


/**
 * Created by user on 03/05/2017.
 */
public class GroupHolder extends GeneralHolder {
    CircleImageView groupProfile;
    TextView name;
    TextView balance;
    TextView badge;
    TextClock time;
    private GroupHolder(View view, CircleImageView groupProfile, TextView name, TextView balance, TextView badge, TextClock time){
        super(view);
        this.groupProfile=groupProfile;
        this.name=name;
        this.balance=balance;
        this.badge=badge;
        this.time=time;
    }

    public static it.polito.group05.group05.Utility.Holder.GroupHolder newInstance(View itemView) {

        CircleImageView groupProfile = (CircleImageView)itemView.findViewById(R.id.iv_group_image);
        TextView balance = (TextView)itemView.findViewById(R.id.tv_group_balance);
        TextView name = (TextView)itemView.findViewById(R.id.tv_group_name);
        TextView badge = (TextView)itemView.findViewById(R.id.tv_badge_counter);
        TextClock time = (TextClock)itemView.findViewById(R.id.tc_last_message);
        return new it.polito.group05.group05.Utility.Holder.GroupHolder(itemView,groupProfile,name,balance,badge,time);
    }
    public void setData(Object c, Context context){
        if(!(c instanceof GroupDatabase)) return;
    }
}