package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.Group_Activity;
import it.polito.group05.group05.MainActivity;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;


/**
 * Created by user on 03/05/2017.
 */
public class GroupHolder extends GeneralHolder {
    CircleImageView groupProfile;
    TextView name;
    TextView balance;
    TextView badge;
    TextClock time;
    LinearLayout ll;

    public  GroupHolder(View itemView) {
        super(itemView);
        ll = (LinearLayout)itemView.findViewById(R.id.group_item_sample_ll);
        this.groupProfile=(CircleImageView)itemView.findViewById(R.id.iv_group_image);
        this.balance=(TextView)itemView.findViewById(R.id.tv_group_balance);
        this.name=(TextView)itemView.findViewById(R.id.tv_group_name);
        this.badge=(TextView)itemView.findViewById(R.id.tv_badge_counter);
        this.time=(TextClock)itemView.findViewById(R.id.tc_last_message);

    }
    public void setData(Object c, final Context context){
        if(!(c instanceof GroupDatabase)) return;
        final GroupDatabase g = (GroupDatabase) c;
        //groupProfile.setImageResource(R.drawable.boy);
            Glide.with(context)
                .load(g.pictureUrl)
                .centerCrop()
                .placeholder(R.drawable.group_profile)
                .crossFade()
                .into(groupProfile);
        name.setText(g.getName());
        time.setText(g.getLmTime());
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singleton.getInstance().setmCurrentGroup(g);
                   Singleton.getInstance().setIdCurrentGroup(g.getId());
                   Intent i = new Intent(context,Group_Activity.class);
                   context.startActivity(i);
            }
        });

    }
}