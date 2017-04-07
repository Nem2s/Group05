package it.polito.group05.group05.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.HomeScreen;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;

/**
 * Created by Marco on 04/04/2017.
 */

public class InvitedAdapter extends RecyclerView.Adapter<InvitedAdapter.ViewHolder> {

    Context context;
    List<UserContact> invited;
    ViewGroup parent;
    public List<User> selected;
    Button addButton;

    public InvitedAdapter(List<UserContact> invited, Context context) {
        this.context = context;
        this.invited = invited;
        selected = new ArrayList<>();
        addButton = (Button)((Activity)context).findViewById(R.id.add_to_group_button);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.invited_item_sample, parent, false);
        InvitedAdapter.ViewHolder vh = new InvitedAdapter.ViewHolder(v);
        this.parent = parent;
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int currentPosition = position;
        final UserContact currentUser = invited.get(position);
        /*Picasso
                .with(context)
                .load(Integer.parseInt(currentUser.getProfile_image()))
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.ic_visibility_off)
                .transform(new PicassoRoundTransform())
                .into(holder.img_profile);*/
        holder.img_profile.setImageBitmap(currentUser.getProfile_image());
        holder.button.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
        if(currentUser.isSelected())
            holder.button.setVisibility(View.VISIBLE);
        else
            holder.button.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.button.getVisibility() == View.INVISIBLE) {
                    holder.button.setVisibility(View.VISIBLE);
                    currentUser.setSelected(true);
                    AnimUtils.toggleOn(holder.button, 500, context);

                }
                else {
                    holder.button.setVisibility(View.INVISIBLE);
                    currentUser.setSelected(false);
                    AnimUtils.toggleOff(holder.button, 500, context);

                }
            }
        });
        holder.name.setText(currentUser.getUser_name());
    }

    @Override
    public int getItemCount() {
        return invited.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircularImageView img_profile;
        TextView name;
        FloatingActionButton button;

        public ViewHolder(View itemView) {
            super(itemView);
            img_profile = (CircularImageView)itemView.findViewById(R.id.cv_invited_image);
            name = (TextView)itemView.findViewById(R.id.tv_invited_name);
            button = (FloatingActionButton)itemView.findViewById(R.id.add_to_group_button);

        }

    }
}
