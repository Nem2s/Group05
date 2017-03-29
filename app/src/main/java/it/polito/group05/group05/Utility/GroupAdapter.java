package it.polito.group05.group05.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.List;

import it.polito.group05.group05.HomeScreen;
import it.polito.group05.group05.R;

/**
 * Created by Marco on 24/03/2017.
 */

public class GroupAdapter extends ArrayAdapter<Group> {

    private static class ViewHolder {
        CircularImageView groupProfile;
        TextView name;
        TextView balance;
        TextView badge;
        TextClock time;
    }

    public GroupAdapter(Context context, List<Group> objects) {
        super(context,0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Group group = getItem(position);

         ViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_item_sample, parent, false);
            holder = new ViewHolder();
            holder.groupProfile = (CircularImageView) convertView.findViewById(R.id.iv_group_image);
            holder.balance = (TextView)convertView.findViewById(R.id.tv_group_balance);
            holder.name = (TextView)convertView.findViewById(R.id.tv_group_name);
            holder.badge = (TextView)convertView.findViewById(R.id.tv_badge_counter);
            holder.time = (TextClock)convertView.findViewById(R.id.tc_last_message);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder)convertView.getTag();

        if(group != null) {

            int groupColor = -1;
            Picasso
                    .with(getContext())
                    .load(group.getGroupProfile())
                    .placeholder(R.drawable.default_avatar)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .error(R.drawable.ic_visibility_off)
                    .transform(new PicassoRoundTransform())
                    .into(holder.groupProfile);


            Picasso
                    .with(getContext())
                    .load(group.getGroupProfile())
                    .resize(100, 100)
                    .centerCrop()
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                            group.setGroupColor(ColorUtils.getDominantColor(bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
            holder.name.setText(group.getName());
            holder.balance.setText(group.getBalance());
            holder.time.setText(group.getLmTime());
            group.setGroupColor(groupColor);
            if(group.getBadge() != null) {
                holder.badge.setText(group.getBadge());
                holder.time.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
            }
            else
                holder.badge.setVisibility(View.INVISIBLE);
            final View finalConvertView = convertView;

            holder.groupProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Ordinary Intent for launching a new activity
                    Intent intent = new Intent(getContext(), ProfileImageActivity.class);

                    // Get the transition name from the string
                    String transitionName = getContext().getString(R.string.transition_string);

                    // Define the view that the animation will start from
                    View viewStart = finalConvertView.findViewById(R.id.iv_group_image);

                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getContext(),
                                    viewStart,   // Starting view
                                    transitionName    // The String
                            );
                    //Start the Intent
                    int[] screenLocation = new int[2];
                    viewStart.getLocationOnScreen(screenLocation);
                    Singleton.getInstance().setmCurrentGroup(group);
                    ActivityCompat.startActivity(getContext(), intent, options.toBundle());
                }
            });
        }
        return convertView;
    }
}
