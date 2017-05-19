package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.Adapter.MemberExpandedAdapter;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;

/**
 * Created by user on 14/05/2017.
 */

public class MemberIncludedHolder extends RecyclerView.ViewHolder {
    private List<User_expense> users;
    public RelativeLayout parent;
    public CircleImageView image_person;
    public TextView name_person;
    public ImageView euro_person;
    public EditText costo_person;
    private boolean customValue;


    public MemberIncludedHolder(View itemView, List<User_expense> users ) {
        super(itemView);
        this.users= users;
        parent = (RelativeLayout) itemView.findViewById(R.id.et_quotae);
        image_person = (CircleImageView) itemView.findViewById(R.id.iv_person_image);
        name_person= (TextView) itemView.findViewById(R.id.tv_name_member);
        euro_person = (ImageView) itemView.findViewById(R.id.euro_member);
        costo_person = (EditText) itemView.findViewById(R.id.et_ins);
        customValue = false;
    }
    public boolean isCustomValue() {
        return customValue;
    }
    //
    public List<User_expense> getList(){return users;}

    public void setCustomValue(boolean customValue) {
        this.customValue = customValue;
    }
}
