package it.polito.group05.group05.Utility;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;
import java.util.StringTokenizer;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.User;

/**
 * Created by Marco on 28/03/2017.
 */

public class UserAdapter extends ArrayAdapter<User> {

    Context context;
    ViewHolder holder;
    public UserAdapter(@NonNull Context context, List<User> objects) {
        super(context, 0, objects);
        this.context = context;
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
                    .load(Integer.parseInt(user.getProfile_image()))
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.ic_visibility_off)
                    .transform(new PicassoRoundTransform())
                    .into(holder.img_profile);
            holder.img_profile.setBorderColor(user.getUser_color());
            String text = "<font color='green'>" + user.getBalance().getCredit() + "</font> / <font color='red'>" + user.getBalance().getDebit() + "</font>";
            holder.balance.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
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
