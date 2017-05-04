package it.polito.group05.group05.Utility;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.UserGroup;

/**
 * Created by Marco on 08/04/2017.
 */

public class ChartUserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    List<UserGroup> users;
    public boolean isSelected;
    View v;

    public ChartUserListAdapter(List<UserGroup> list, Context context) {
        this.context = context;

        this.users = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart_user_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder h = (ViewHolder)holder;
        h.img_profile.setImageBitmap(users.get(position).getProfile_image());
        h.name.setText(users.get(position).getUser_name());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView img_profile;
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            img_profile = (CircleImageView)itemView.findViewById(R.id.iv_user_image);
            name = (TextView)itemView.findViewById(R.id.tv_user_name);
        }
    }

}
