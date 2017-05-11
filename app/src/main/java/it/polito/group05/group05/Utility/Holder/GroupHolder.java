package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.content.Intent;
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
import it.polito.group05.group05.Group_Activity;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;


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
                    .using(new FirebaseImageLoader())
                    .load(FirebaseStorage.getInstance().getReference("groups").child(g.getId()).child(g.getPictureUrl()))
                .centerCrop()
                .placeholder(R.drawable.group_profile)
                .crossFade()
                .into(groupProfile);
        name.setText(g.getName());
        time.setText(g.getLmTime());

        Map<String, Object> tmp = new HashMap<>(g.getMembers());
        g.getMembers().clear();
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
                            g.getMembers().put(u.getId(), u);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }


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