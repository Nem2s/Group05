package it.polito.group05.group05.Utility.Holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.GroupDetailsActivity;
import it.polito.group05.group05.Group_Activity;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Balance;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.HelperClasses.AnimUtils;
import it.polito.group05.group05.Utility.HelperClasses.ImageUtils;


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
        ImageUtils.LoadImageGroup(groupProfile, context, g);
        name.setText(g.getName());
        time.setText(g.getLmTime());
        this.balance.setText(String.format("%.2f", Double.parseDouble(g.getMembers().get(Singleton.getInstance().getCurrentUser().getId()).toString())));

        Double x = Double.valueOf(balance.getText().toString().replace(",", "."));
        if(x >0.001)
        balance.setTextColor(Color.GREEN);
        else if(x <-0.001)
            balance.setTextColor(Color.RED);
        else{
            balance.setText("Saldato");

        }

        Map<String, Object> tmp = new HashMap<>(g.getMembers());

        for(String userID : tmp.keySet()){
            FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userID)
                    .child("userInfo")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()) return;
                            UserDatabase u = dataSnapshot.getValue(UserDatabase.class);
                            u.setBalance(new Balance(Double.valueOf(g.getMembers().get(u.getId()).toString()),Double.valueOf(g.getMembers().get(u.getId()).toString())));
                            g.getMembers().put(u.getId(), u);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }

        groupProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Singleton.getInstance().setmCurrentGroup(g);
                Singleton.getInstance().setIdCurrentGroup(g.getId());
                Pair<View, String> p1 = new Pair<View, String>((View)groupProfile, context.getResources().getString(R.string.transition_group_image));
                AnimUtils.startActivityWithAnimation((Activity)context, new Intent(context, GroupDetailsActivity.class), p1);
            }
        });


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