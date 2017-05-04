package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;


/**
 * Created by user on 03/05/2017.
 */


public class MemberHolder extends GeneralHolder {
    CircleImageView img_profile;
    TextView user_name;
    TextView balance;
    TextView administrator;
    ImageView payByCard;
    FloatingActionButton color;



    public static it.polito.group05.group05.Utility.Holder.MemberHolder newInstance(View itemView) {
        CircleImageView img_profile = (CircleImageView) itemView.findViewById(R.id.iv_user_image);
        TextView balance = (TextView)itemView.findViewById(R.id.tv_user_balance);
        TextView user_name = (TextView)itemView.findViewById(R.id.tv_user_name);
        ImageView payByCard = (ImageView)itemView.findViewById(R.id.iv_paycard);
        TextView administrator = (TextView)itemView.findViewById(R.id.tv_admin);
        return new it.polito.group05.group05.Utility.Holder.MemberHolder(itemView,img_profile,user_name,balance,administrator,payByCard);
    }

    private  MemberHolder( View v,CircleImageView img_profile,TextView user_name,TextView balance,TextView administrator,ImageView payByCard) {
        super(v);
        this.img_profile = img_profile;
        this.user_name=user_name;
        this.balance=balance;
        this.administrator= administrator;
        this.payByCard=payByCard;
        //this.color=color;
    }

    public void setData(Object c, Context context){
        if(!(c instanceof UserDatabase)) return;
    }


}
