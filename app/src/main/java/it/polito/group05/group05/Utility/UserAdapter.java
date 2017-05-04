package it.polito.group05.group05.Utility;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;
import java.util.StringTokenizer;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.User;

/**
 * Created by Marco on 28/03/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context context;
    List<User> users;
    public UserAdapter(List<User> objects, Context context) {
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
        final User currentUser = users.get(position);
        /*Picasso
                .with(context)
                .load(Integer.parseInt(currentUser.getProfile_image()))
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.ic_visibility_off)
                .transform(new PicassoRoundTransform())
                .into(holder.img_profile);*/
        holder.img_profile.setImageBitmap(currentUser.getProfile_image());
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
        CircleImageView img_profile;
        TextView user_name;
        TextView balance;
        TextView administrator;
        ImageView payByCard;
        FloatingActionButton color;

        public ViewHolder(View itemView) {
            super(itemView);
            img_profile = (CircleImageView) itemView.findViewById(R.id.iv_user_image);
            balance = (TextView)itemView.findViewById(R.id.tv_user_balance);
            user_name = (TextView)itemView.findViewById(R.id.tv_user_name);
            payByCard = (ImageView)itemView.findViewById(R.id.iv_paycard);
            administrator = (TextView)itemView.findViewById(R.id.tv_admin);
        }
    }




}
