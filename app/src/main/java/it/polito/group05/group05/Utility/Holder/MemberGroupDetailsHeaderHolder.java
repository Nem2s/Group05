package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;
import it.polito.group05.group05.Utility.HelperClasses.ImageUtils;

/**
 * Created by Marco on 17/05/2017.
 */

public class MemberGroupDetailsHeaderHolder extends GeneralHolder {
    private CircleImageView cv_userImage;
    private TextView tv_userName;
    private Button buttonLeave;

    public MemberGroupDetailsHeaderHolder(View itemView) {
        super(itemView);

        this.cv_userImage = (CircleImageView) itemView.findViewById(R.id.cv_userimage);
        this.tv_userName = (TextView) itemView.findViewById(R.id.tv_user_name);
        this.buttonLeave = (Button) itemView.findViewById(R.id.button_leave);
    }

    @Override
    public void setData(Object c, final Context context) {
        if (!(c instanceof UserDatabase)) return;
        final UserDatabase user = Singleton.getInstance().getCurrentUser();
        ImageUtils.LoadMyImageProfile(cv_userImage, context);
        tv_userName.setText("You");

        buttonLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB_Manager.getInstance().leaveGroup(user, context);
            }
        });

    }
}
