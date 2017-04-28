package it.polito.group05.group05.Utility;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import java.util.List;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.UserGroup;

/**
 * Created by Marco on 28/03/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context context;
    List<UserGroup> users;
    public UserAdapter(List<UserGroup> objects, Context context) {
        this.context = context;
        this.users = objects;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_item_sample, parent, false);
        UserAdapter.ViewHolder vh = new UserAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        final int currentPosition = position;
        final UserGroup currentUser;
        holder.img_profile.setImageBitmap(users.get(position).getProfile_image());
        if(users.get(position) instanceof UserGroup)
            currentUser =(UserGroup) users.get(position);
        else
            currentUser = new UserGroup(users.get(position));


        if(!currentUser.isAdministrator())
            holder.administrator.setVisibility(View.INVISIBLE);
        if(!currentUser.isCardEnabled())
            holder.payByCard.setVisibility(View.INVISIBLE);
        holder.user_name.setText(currentUser.getUser_name());


        holder.payByCard.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Snackbar
                    .make(view, "To be implemented...", Snackbar.LENGTH_SHORT)
                    .setAction("Ok", null)
                    .show();
        }
    });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CircularImageView img_profile;
        TextView user_name;
        TextView balance;
        TextView administrator;
        ImageView payByCard;
        FloatingActionButton color;

        public ViewHolder(View itemView) {
            super(itemView);
            img_profile = (CircularImageView) itemView.findViewById(R.id.iv_user_image);
            balance = (TextView)itemView.findViewById(R.id.tv_user_balance);
            user_name = (TextView)itemView.findViewById(R.id.tv_user_name);
            payByCard = (ImageView)itemView.findViewById(R.id.iv_paycard);
            administrator = (TextView)itemView.findViewById(R.id.tv_admin);
        }
    }




}
