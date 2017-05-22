package it.polito.group05.group05.Utility.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.ChatFragment;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.ChatDatabase;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;

public class MessageAdapter extends FirebaseListAdapter<ChatDatabase> {
    private Activity activity;
    boolean isSender;
    private int color_users;
    private int color2;
    private int color;

    public MessageAdapter(ChatFragment chatfrag, Class<ChatDatabase> modelClass, int modelLayout, DatabaseReference ref) {
        super(chatfrag.getActivity(), modelClass, modelLayout, ref);
        activity=chatfrag.getActivity();
        color_users = Singleton.getInstance().getmCurrentGroup().getMembers().size();
        isSender = false;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ChatDatabase cdb = getItem(position);
        view = activity.getLayoutInflater().inflate(R.layout.message, viewGroup, false);
        if (cdb.getMessageUserId().equals(Singleton.getInstance().getCurrentUser().getId()))
            isSender= true;
        else
            isSender= false;

        populateView(view, cdb, position);

        return view;
    }

    @Override
    protected void populateView(final View v, ChatDatabase model, int position) {
        ChatDatabase cdb = getItem(position);
        TextView messageText = (TextView) v.findViewById(R.id.message_text);
        TextView messageUser = (TextView) v.findViewById(R.id.name_text);
        TextView messageTime = (TextView) v.findViewById(R.id.message_time);
        final CircleImageView c = (CircleImageView) v.findViewById(R.id.image_person);

        FirebaseDatabase.getInstance().getReference("users").child(cdb.getMessageUserId())
                        .child("userInfo")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) return;
                UserDatabase u = dataSnapshot.getValue(UserDatabase.class);
                Glide.with(v.getContext())
                        .using(new FirebaseImageLoader())
                        .load(FirebaseStorage.getInstance().getReference("users").child(u.getId()).child(u.getiProfile()))
                        .centerCrop()
                        .into(c);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        RelativeLayout message= (RelativeLayout) v.findViewById(R.id.message_container);
        LinearLayout mymessage = (LinearLayout) v.findViewById(R.id.first_linear);
      //  color1 = ContextCompat.getColor(v.getContext(), R.color.colorPrimaryLight);
        color2 = ContextCompat.getColor(v.getContext(), R.color.white);


        if(isSender){
            color = color2;
            messageUser.setTextColor(Color.rgb(15,61,72));
          //  leftArrow.setVisibility(View.GONE);
          //  rightArrow.setVisibility(View.VISIBLE);
            message.setGravity(Gravity.END);
            messageTime.setGravity(Gravity.START);
        }
        else{
            color = color2;
            messageUser.setTextColor(Color.rgb(62,104,115));

            //leftArrow.setVisibility(View.VISIBLE);
            //rightArrow.setVisibility(View.GONE);
            message.setGravity(Gravity.START);
        }

        ((GradientDrawable)mymessage.getBackground()).setColor(color);
      //  ((RotateDrawable) leftArrow.getBackground()).getDrawable().setColorFilter(color, PorterDuff.Mode.SRC);
       // ((RotateDrawable) rightArrow.getBackground()).getDrawable().setColorFilter(color, PorterDuff.Mode.SRC);
        messageText.setText(model.getMessageText());
        messageUser.setText(model.getMessageUser());
        messageTime.setText(DateFormat.format("hh:mm",model.getMessageTime()));
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }
}
