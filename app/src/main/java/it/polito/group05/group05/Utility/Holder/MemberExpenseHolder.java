package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.HelperClasses.ImageUtils;

/**
 * Created by Marco on 27/05/2017.
 */

public class MemberExpenseHolder extends GeneralHolder {

    CircleImageView cv;
    TextView name;
    TextView price;
    UserDatabase user;
    Context context;

    public MemberExpenseHolder(View itemView) {
        super(itemView);
        this.cv = (CircleImageView) itemView.findViewById(R.id.cv_image);
        this.name = (TextView) itemView.findViewById(R.id.tv_name);
        this.price = (TextView) itemView.findViewById(R.id.tv_price);

    }

    @Override
    public void setData(Object c, Context context) {

    }


    public void setData(Object c, Context context, Map<String, Double> e) {
        user = (UserDatabase) c;
        this.context = context;
        ImageUtils.LoadUserImageProfile(cv, context, user);
        if (user.getId().equals(Singleton.getInstance().getCurrentUser().getId())) {
            name.setText("You");
        } else
            name.setText(user.getName());
        double d = Math.abs(e.get(user.getId()));
        d = Math.round(d);
        this.price.setText(String.format("%.2f", d) + " €");

    }
}
