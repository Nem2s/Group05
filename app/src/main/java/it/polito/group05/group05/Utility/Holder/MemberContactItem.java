package it.polito.group05.group05.Utility.Holder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.aesthetic.AestheticButton;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.ui.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialize.holder.StringHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;

import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.Event.SelectionChangedEvent;
import it.polito.group05.group05.Utility.HelperClasses.ImageUtils;

/**
 * Created by Marco on 25/05/2017.
 */

public class MemberContactItem extends AbstractItem<MemberContactItem, MemberContactItem.ViewHolder> {

    public String header;
    public String imgUri;
    public String name;
    public String number;


    public static MemberContactItem fromUserContact(UserContact u) {
        MemberContactItem item = new MemberContactItem();
        item.name = u.getName();
        item.number = u.getTelNumber();
        item.imgUri = u.getiProfile();
        return item;
    }

    public static List<MemberContactItem> fromUserContactList(List<UserContact> list) {
        List<MemberContactItem> res = new ArrayList<>();
        for(UserContact u : list) {
            res.add(fromUserContact(u));
        }
        return res;
    }

    public MemberContactItem withHeader(String header) {
        this.header = header;
        return this;
    }

    public MemberContactItem withName(String name) {
        this.name = name;
        return this;
    }

    public MemberContactItem withNumber(String number) {
        this.number = number;
        return this;
    }

    public MemberContactItem withImgUri(String uri) {
        this.imgUri = uri;
        return this;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getType() {
        return R.id.contact_parent;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.contact_item_sample;

    }



    @Override
    public void bindView(final ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        final Context context = holder.itemView.getContext();
        holder.name.setText(name);
        holder.number.setText(number);
        if(imgUri != null)
            holder.img_profile.setImageURI(Uri.parse(imgUri));
        else
            holder.img_profile.setImageDrawable(context.getResources().getDrawable(R.drawable.user_placeholder));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = holder.number.getText().toString();
                String smsBody =  "Let's try out Share Cash! It's awesome!\n\nhttps://h5uqp.app.goo.gl/ZjWo";
                Uri uri = Uri.parse("smsto:" + phoneNumber);
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, uri);
                smsIntent.putExtra("sms_body", smsBody);
                context.startActivity(smsIntent);
            }
        });

    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_profile;
        TextView name;
        TextView number;
        AestheticButton button;

        public ViewHolder(View itemView) {
            super(itemView);
            this.img_profile = (CircleImageView)itemView.findViewById(R.id.cv_invited_image);
            this.name = (TextView)itemView.findViewById(R.id.tv_invited_name);
            this.number = (TextView)itemView.findViewById(R.id.tv_invited_pnumber);
            this.button = (AestheticButton)itemView.findViewById(R.id.button_invite);
        }
    }
}
