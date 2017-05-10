package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.firebase.ui.auth.ui.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.EventClasses.SelectionChangedEvent;

/**
 * Created by Marco on 05/05/2017.
 */

public class MemberInvitedHolder extends GeneralHolder{

    CircleImageView img_profile;
    TextView name;
    TextView number;
    TextView email;
    FloatingActionButton button;



    public  MemberInvitedHolder (View itemView) {
        super(itemView);
        this.img_profile = (CircleImageView)itemView.findViewById(R.id.cv_invited_image);
        this.name = (TextView)itemView.findViewById(R.id.tv_invited_name);
        this.number = (TextView)itemView.findViewById(R.id.tv_invited_pnumber);
        this.email = (TextView)itemView.findViewById(R.id.tv_invited_email);
        this.button = (FloatingActionButton)itemView.findViewById(R.id.add_to_group_button);

    }
    @Override
    public void setData(Object c, Context context) {
        final UserContact user = ((UserContact) c);
                Glide.with(context)
                        .using(new FirebaseImageLoader())
                        .load(FirebaseStorage.getInstance().getReference("users")
                                .child(user.getId())
                                .child(user.getiProfile()))
                        .asBitmap()
                        .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                        .into(img_profile);

                this.name.setText(user.getName());
                this.number.setText(user.getTelNumber());
                this.email.setText(user.getEmail());
                if (user.isSelected())
                    button.setVisibility(View.VISIBLE);
                else
                    button.setVisibility(View.GONE);

                this.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (button.getVisibility() != View.VISIBLE) {
                            user.setSelected(true);
                            button.show();
                        } else {
                            user.setSelected(false);
                            button.hide();
                        }
                        EventBus.getDefault().post(SelectionChangedEvent.onSelectionChangedEvent(user.isSelected()));
                        //Notifica che qualcuno Ã¨ selezionato sul Bus.
                    }
                });
            }

}