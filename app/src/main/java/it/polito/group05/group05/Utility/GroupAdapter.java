package it.polito.group05.group05.Utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import it.polito.group05.group05.R;

/**
 * Created by Marco on 24/03/2017.
 */

public class GroupAdapter extends ArrayAdapter<Group> {

    private static class ViewHolder {
        ImageView groupProfile;
        TextView name;
        TextView balance;
    }

    public GroupAdapter(Context context, List<Group> objects) {
        super(context,0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Group group = getItem(position);
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_item_sample, parent, false);
            holder = new ViewHolder();
            holder.groupProfile = (ImageView)convertView.findViewById(R.id.group_image);
            holder.balance = (TextView)convertView.findViewById(R.id.tv_group_balance);
            holder.name = (TextView)convertView.findViewById(R.id.tv_group_name);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder)convertView.getTag();

        if(group != null) {
            holder.groupProfile = group.getGroupProfile();
            holder.name.setText(group.getName());
            holder.balance.setText(group.getBalance());
        }
        return convertView;
    }
}
