package it.polito.group05.group05.Utility.BaseClasses;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.mikepenz.fastadapter.items.AbstractItem;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.Event.SelectionChangedEvent;

/**
 * Created by antonino on 01/06/2017.
 */

public class ContactItem extends AbstractItem<ContactItem, ContactItem.ContactHolder> {
    public UserContact ue;
    public Context context;

    @Override
    public void bindView(ContactHolder holder, List<Object> payloads) {
        super.bindView(holder, payloads);
        holder.setData(ue, context);
    }

    @Override
    public ContactHolder getViewHolder(View view) {
        return new ContactHolder(view);
    }

    @Override
    public int getType() {
        return R.id.contact_parent;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.invited_item_sample;
    }


    public class ContactHolder extends RecyclerView.ViewHolder {

        CircleImageView img_profile;
        TextView name;
        TextView number;
        TextView email;
        FloatingActionButton button;

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


        public ContactHolder(View itemView) {

            super(itemView);
            this.img_profile = (CircleImageView) itemView.findViewById(R.id.cv_invited_image);
            this.name = (TextView) itemView.findViewById(R.id.tv_invited_name);
            this.number = (TextView) itemView.findViewById(R.id.tv_invited_pnumber);
            this.email = (TextView) itemView.findViewById(R.id.tv_invited_email);
            this.button = (FloatingActionButton) itemView.findViewById(R.id.add_to_group_button);

        }
    }
}
