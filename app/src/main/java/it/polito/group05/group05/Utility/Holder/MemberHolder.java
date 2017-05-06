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

    private  MemberHolder(View v){
        super(v);
        this.img_profile = (CircleImageView) itemView.findViewById(R.id.iv_user_image);;
        this.user_name=(TextView)itemView.findViewById(R.id.tv_user_name);
        this.balance= (TextView)itemView.findViewById(R.id.tv_user_balance);
        this.administrator= (TextView)itemView.findViewById(R.id.tv_admin);
        this.payByCard=(ImageView)itemView.findViewById(R.id.iv_paycard);

    }

    public void setData(Object c, Context context){

        if(!(c instanceof UserDatabase)) return;

    }


}
