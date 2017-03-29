package it.polito.group05.group05.Utility;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.R;

/**
 * Created by Marco on 28/03/2017.
 */

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(@NonNull Context context, List<User> objects) {
        super(context, 0, objects);
    }

    private static class ViewHolder {
        CircularImageView img_profile;
        TextView user_name;
        TextView balance;
        TextView administrator;
        ImageView payByCard;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = getItem(position);
        ViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.member_item_sample, parent, false);
            holder = new ViewHolder();
            holder.img_profile = (CircularImageView) convertView.findViewById(R.id.iv_user_image);
            holder.balance = (TextView)convertView.findViewById(R.id.tv_user_balance);
            holder.user_name = (TextView)convertView.findViewById(R.id.tv_user_name);
            holder.payByCard = (ImageView)convertView.findViewById(R.id.iv_paycard);
            holder.administrator = (TextView)convertView.findViewById(R.id.tv_admin);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder)convertView.getTag();

        if(user != null) {
            Picasso
                    .with(getContext())
                    .load(user.getProfile_image())
                    .placeholder(R.drawable.default_avatar)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.ic_visibility_off)
                    .transform(new PicassoRoundTransform())

                    .into(holder.img_profile);
            holder.balance.setText(user.getBalance());
            if(!user.isAdministrator())
                holder.administrator.setVisibility(View.INVISIBLE);
            if(!user.isCardEnabled())
                holder.payByCard.setVisibility(View.INVISIBLE);
            holder.user_name.setText(user.getUser_name());
        }

        holder.payByCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar
                        .make(view, "To be implemented...", Snackbar.LENGTH_SHORT)
                        .setAction("Ok", null)
                        .show();
            }
        });

        final View finalConvertView = convertView;
        return finalConvertView;
    }

}
