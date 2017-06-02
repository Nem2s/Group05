package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.afollestad.aesthetic.AestheticButton;
import com.bumptech.glide.Glide;

import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.mikepenz.fastadapter.items.AbstractItem;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.Event.SelectionChangedEvent;
import it.polito.group05.group05.Utility.HelperClasses.AnimUtils;

/**
 * Created by Marco on 05/05/2017.
 */

public class MemberInvitedItem extends AbstractItem<MemberInvitedItem, MemberInvitedItem.ViewHolder> {

    public String name;
    String id;
    String number;
    String email;
    String imgUri;
    UserContact user;


    public static MemberInvitedItem fromUserContact(UserContact u) {
        MemberInvitedItem item = new MemberInvitedItem();
        item.name = u.getName();
        item.number = u.getTelNumber();
        item.imgUri = u.getiProfile();
        item.email = u.getEmail();
        item.id = u.getId();
        u.setSelected(false);
        item.user = u;
        return item;
    }

    public void setSelected(boolean b) {
        user.setSelected(b);
    }

    public boolean isSelected() {
        return user.isSelected();
    }

    public static List<MemberInvitedItem> fromUserContactList(List<UserContact> list) {
        List<MemberInvitedItem> res = new ArrayList<>();
        for(UserContact u : list) {
            res.add(fromUserContact(u));
        }
        return res;
    }



    @Override
    public void bindView(ViewHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);

        final Context context = holder.itemView.getContext();
        holder.name.setText(name);
        holder.number.setText(number);
        if(email != null)
            holder.email.setText(email);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference("users")
                        .child(id)
                        .child(imgUri))
                .asBitmap()
                .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                .into(holder.img_profile);
        holder.button.setVisibility(View.INVISIBLE);


    }



    @Override
    public MemberInvitedItem.ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getType() {
        return R.id.invited_parent;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.invited_item_sample;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_profile;
        TextView name;
        TextView number;
        TextView email;
        FloatingActionButton button;

        public ViewHolder(View itemView) {
            super(itemView);
            this.img_profile = (CircleImageView)itemView.findViewById(R.id.cv_invited_image);
            this.name = (TextView)itemView.findViewById(R.id.tv_invited_name);
            this.number = (TextView)itemView.findViewById(R.id.tv_invited_pnumber);
            this.email = (TextView)itemView.findViewById(R.id.tv_invited_email);
            this.button = (FloatingActionButton) itemView.findViewById(R.id.add_to_group_button);
        }

        public void handleButton(boolean selected, Context context) {
            if(selected) button.hide();
            else button.show();
        }
    }


}