package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;

/**
 * Created by andre on 10-May-17.
 */

public class PersonHolder extends GeneralHolder {

    CardView cv;
    RelativeLayout rl_person;
    RelativeLayout rl;
    ImageView img_profile;
    TextView user_name;
    TextView balance;
    TextView total_balance;

    public PersonHolder(View v){
        super(v);
        this.rl = (RelativeLayout) itemView.findViewById(R.id.total_balace);
        this.rl_person = (RelativeLayout) itemView.findViewById(R.id.rl_person);
        this.cv = (CardView) itemView.findViewById(R.id.card_person);
        this.img_profile = (ImageView) itemView.findViewById(R.id.person_image);
        this.user_name = (TextView) itemView.findViewById(R.id.person_name);
        this.balance = (TextView) itemView.findViewById(R.id.person_personal_credit_debit);
        this.total_balance = (TextView) itemView.findViewById(R.id.info_total_credit_debit);
    }

    @Override
    public void setData(Object c, Context context) {
        if (!(c instanceof UserDatabase)) return;
        UserDatabase u = (UserDatabase) c;
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference("users").child(u.getId()).child(u.getiProfile()))
                .centerCrop()
                .into(img_profile);
        user_name.setText(u.getName());
        if(Singleton.getInstance().getUsersBalance().containsKey(u.getId()))
        {
            if (u.getId().equals(Singleton.getInstance().getCurrentUser().getId())) user_name.setText("You");
                balance.setText(String.format("%.2f €",Math.abs(Singleton.getInstance().getUsersBalance().get(u.getId()))));
            if(Singleton.getInstance().getUsersBalance().get(u.getId()) > 0){
                balance.setTextColor(context.getResources().getColor(R.color.debit_red));
            }
            else
                balance.setTextColor(context.getResources().getColor(R.color.credit_gree));
        }
        else
            balance.setText("0.00");

        total_balance.setText("Deve dare al gruppo 50 €");

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (total_balance.getVisibility() == View.GONE) {
                    rl.setVisibility(View.VISIBLE);
                    total_balance.setVisibility(View.VISIBLE);

                } else {
                    rl.setVisibility(View.GONE);
                    total_balance.setVisibility(View.GONE);
                }
            }
        });
    }


}
