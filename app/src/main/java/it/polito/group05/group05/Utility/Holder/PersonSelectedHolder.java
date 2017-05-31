package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;

/**
 * Created by user on 29/05/2017.
 */

public class PersonSelectedHolder extends GeneralHolder {
    CircleImageView img_profile;
    TextView name;
    Switch switchButton;
    Context context;

    public PersonSelectedHolder(View itemView) {
        super(itemView);
        this.img_profile = (CircleImageView) itemView.findViewById(R.id.iv_person_image);
        this.name = (TextView) itemView.findViewById(R.id.tv_name_member);
        this.switchButton = (Switch) itemView.findViewById(R.id.switch_button);
    }

    @Override
    public void setData(Object c, final Context context) {
        if (!(c instanceof User_expense)) return;
        final User_expense us = (User_expense) c;
        name.setText(us.getName());
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference("users")
                        .child(us.getId())
                        .child(us.getiProfile()))
                .asBitmap()
                .placeholder(R.drawable.user_placeholder)
                .into(img_profile);
        switchButton.setChecked(true);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()) {
                    us.setExcluded(false);
                } else {
                    if (!(us.getId().equals(Singleton.getInstance().getCurrentUser().getId())))
                        us.setExcluded(true);
                    else {
                        Toast.makeText(context, "You have already payed", Toast.LENGTH_SHORT).show();

                        switchButton.setChecked(true);
                    }
                }
            }
        });

    }
}

