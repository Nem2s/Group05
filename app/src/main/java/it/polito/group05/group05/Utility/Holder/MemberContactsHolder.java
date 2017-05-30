package it.polito.group05.group05.Utility.Holder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import org.greenrobot.eventbus.EventBus;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.Event.SelectionChangedEvent;

/**
 * Created by Marco on 25/05/2017.
 */

public class MemberContactsHolder extends GeneralHolder {

    CircleImageView img_profile;
    TextView name;
    TextView number;
    Button button;


    public MemberContactsHolder(View itemView) {
        super(itemView);
        this.img_profile = (CircleImageView)itemView.findViewById(R.id.cv_invited_image);
        this.name = (TextView)itemView.findViewById(R.id.tv_invited_name);
        this.number = (TextView)itemView.findViewById(R.id.tv_invited_pnumber);
        this.button = (Button) itemView.findViewById(R.id.button_invite);

    }

    @Override
    public void setData(final Object c, final Context context) {
        final UserContact user = ((UserContact) c);
        if(user.getiProfile() != null)
            this.img_profile.setImageURI(Uri.parse(user.getiProfile()));
        else
            img_profile.setImageDrawable(context.getResources().getDrawable(R.drawable.user_placeholder));
        this.name.setText(user.getName());
        this.number.setText(user.getTelNumber());

        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent("android.intent.action.MAIN");

                sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators("+39" + user.getTelNumber()) + "@s.whatsapp.net");//phone number without "+" prefix
                try {
                    context.startActivity(sendIntent);
                } catch (Exception e) {
                    Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

    }
}
