package it.polito.group05.group05.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import it.polito.group05.group05.GroupDetailsActivity;
import it.polito.group05.group05.Group_Activity;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;

/**
 * Created by Marco on 24/03/2017.
 */

public class GroupAdapter extends ArrayAdapter<Group> {

    Context context;
    Activity activity;
    private static class ViewHolder {
        CircularImageView groupProfile;
        TextView name;
        TextView balance;
        TextView badge;
        TextClock time;
    }

    public GroupAdapter(Context context, List<Group> objects, Activity activity) {
        super(context,0, objects);
        this.context = context;
        this.activity = activity;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Group group = getItem(position);

         final ViewHolder holder;

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
                    .load(Integer.parseInt(group.getGroupProfile()))
                    .placeholder(R.drawable.default_avatar)
                    .error(R.drawable.ic_visibility_off)
                    .transform(new PicassoRoundTransform())
                    .into(holder.groupProfile);


            holder.name.setText(group.getName());
            String text = "<font color='green'>" + group.getBalance().getCredit() + "</font> / <font color='red'>" + group.getBalance().getDebit() + "</font>";
            holder.balance.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);




            holder.time.setText(group.getLmTime());
            //group.setGroupColor(groupColor);
            if(group.getBadge() != null) {
                holder.badge.setText(group.getBadge());
                holder.time.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
            }
            else
                holder.badge.setVisibility(View.INVISIBLE);
            final View finalConvertView = convertView;

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Group_Activity.class);
                    intent.putExtra("Position", position);
                    Singleton.getInstance().setmCurrentGroup(group);
                    context.startActivity(intent);
            }
            });





            holder.groupProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Ordinary Intent for launching a new activity
                    Intent intent = new Intent(context, GroupDetailsActivity.class);
                    intent.putExtra("Position", position);

                    // Get the transition name from the string
                    String transitionImage = context.getString(R.string.transition_group_image);
                    String transitionFab = context.getString(R.string.transition_fab);

                    // Define the view that the animation will start from
                    View groupImage = view.findViewById(R.id.iv_group_image);
                    FloatingActionButton fab = (FloatingActionButton)activity.findViewById(R.id.fab);
                    Pair <View, String> p1 = Pair.create((View)groupImage, transitionImage);
                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, p1);
                    //Start the Intent
                    Singleton.getInstance().setmCurrentGroup(group);
                    //ActivityCompat.startActivity(context, intent, options.toBundle());
                    context.startActivity(intent, options.toBundle());
                    AnimUtils.toggleOff(fab, 250, context);

                }
            });
        }
        return convertView;
    }
}
