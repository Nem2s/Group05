package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.MainActivity;
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

    public  GroupHolder(View itemView) {
        super(itemView);
        this.groupProfile=(CircleImageView)itemView.findViewById(R.id.iv_group_image);
        this.balance=(TextView)itemView.findViewById(R.id.tv_group_balance);
        this.name=(TextView)itemView.findViewById(R.id.tv_group_name);
        this.badge=(TextView)itemView.findViewById(R.id.tv_badge_counter);
        this.time=(TextClock)itemView.findViewById(R.id.tc_last_message);

    }
    public void setData(Object c, Context context){
        if(!(c instanceof GroupDatabase)) return;
        GroupDatabase g = (GroupDatabase) c;
        //groupProfile.setImageResource(R.drawable.boy);
        Glide.with(MainActivity.context)
             .load(g.pictureUrl)
             .centerCrop()
             //.placeholder(R.drawable.loading_spinner)
             .crossFade()
             .into(groupProfile);
        name.setText(g.getName());
        time.setText(g.getLmTime());
    }
}