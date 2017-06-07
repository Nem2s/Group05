package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.aesthetic.Aesthetic;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.ChatDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;

/**
 * Created by user on 22/05/2017.
 */

public class ChatHolder extends GeneralHolder {
    private TextView userName;
    private TextView text;
    private TextView time;
    private LinearLayout ll;
    private RelativeLayout overlay;
    private CircleImageView imageView;

    public ChatHolder(View v) {
        super(v);
        this.userName = (TextView) v.findViewById(R.id.name_text);
        this.text = (TextView) v.findViewById(R.id.message_text);
        this.time = (TextView) v.findViewById(R.id.message_time);
        this.imageView = (CircleImageView) v.findViewById(R.id.image_person);
        this.ll = (LinearLayout)v.findViewById(R.id.message);
        this.overlay = (RelativeLayout)v.findViewById(R.id.overlay_chat_message);
    }

    @Override
    public void setData(Object c, final Context context) {
        if (!(c instanceof ChatDatabase)) return;
        ChatDatabase cdb = (ChatDatabase) c;
        if(cdb.getMessageUserId().equals(Singleton.getInstance().getCurrentUser().getId())) {
            GradientDrawable bgShape = (GradientDrawable) ll.getBackground();
            GradientDrawable bgoverlay = (GradientDrawable) overlay.getBackground();
            Integer x =null;
            while (x==null) {
                try{
                    x=Aesthetic.get().colorWindowBackground().take(1).blockingFirst();
                bgShape.setColor(x);
                x=Aesthetic.get().colorAccent().take(1).blockingFirst();
                bgoverlay.setColor(x);}
                catch (NullPointerException e ){x=null;}
            }
            overlay.setAlpha(0.4f);
            text.setText(cdb.getMessageText());
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("HH:mm");
            time.setText(sdf.format(cdb.getMessageTime()));

            FirebaseDatabase.getInstance().getReference("users").child(cdb.getMessageUserId())
                    .child("userInfo")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) return;
                            UserDatabase u = dataSnapshot.getValue(UserDatabase.class);
                            Glide.with(context)
                                    .using(new FirebaseImageLoader())
                                    .load(FirebaseStorage.getInstance().getReference("users").child(u.getId()).child(u.getiProfile()))
                                    .centerCrop()
                                    .into(imageView);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        } else {
            GradientDrawable bgShape = (GradientDrawable) ll.getBackground();
            Integer x =null;
            while (x==null) {
                try {
                    x=Aesthetic.get().colorWindowBackground().take(1).blockingFirst();
                    bgShape.setColor(x);
                }
                catch (NullPointerException e ){x=null;}
            }
            text.setText(cdb.getMessageText());
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern("HH:mm");
            time.setText(sdf.format(cdb.getMessageTime()));
            userName.setText(cdb.getMessageUser());
            FirebaseDatabase.getInstance().getReference("users").child(cdb.getMessageUserId())
                    .child("userInfo")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) return;
                            UserDatabase u = dataSnapshot.getValue(UserDatabase.class);
                            Glide.with(context)
                                    .using(new FirebaseImageLoader())
                                    .load(FirebaseStorage.getInstance().getReference("users").child(u.getId()).child(u.getiProfile()))
                                    .centerCrop()
                                    .into(imageView);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }
    }
}

