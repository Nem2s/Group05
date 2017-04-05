package it.polito.group05.group05.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.StringTokenizer;

import it.polito.group05.group05.GroupDetailsActivity;
import it.polito.group05.group05.Group_Activity;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;

/**
 * Created by Marco on 24/03/2017.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    Context context;
    List<Group> groups;
    public boolean isEnabled;

    public GroupAdapter(List<Group> objects, Context context) {
        this.context = context;
        this.groups = objects;
        this.isEnabled = true;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item_sample, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        /*Picasso
                .with(context)
                .load(groups.get(position).getGroupProfile())
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.ic_visibility_off)
                .transform(new PicassoRoundTransform())
                .into(holder.groupProfile);*/
        final int currentPosition = position;
        holder.groupProfile.setImageBitmap(groups.get(position).getGroupProfile());
        holder.name.setText(groups.get(position).getName());
        String text = "<font color='green'>" + groups.get(position).getBalance().getCredit() + "</font> / <font color='red'>" + groups.get(position).getBalance().getDebit() + "</font>";
        holder.balance.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);




        holder.time.setText(groups.get(position).getLmTime());
        //group.setGroupColor(groupColor);
        if(groups.get(position).getBadge() != null) {
            holder.badge.setText(groups.get(position).getBadge());
            holder.time.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }
        else
            holder.badge.setVisibility(View.INVISIBLE);

        holder.groupProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ordinary Intent for launching a new activity
                Intent intent = new Intent(context, GroupDetailsActivity.class);

                // Get the transition name from the string
                String transitionImage = context.getString(R.string.transition_group_image);
                String transitionFab = context.getString(R.string.transition_fab);

                // Define the view that the animation will start from
                View groupImage = view.findViewById(R.id.iv_group_image);
                FloatingActionButton fab = (FloatingActionButton)((Activity)context).findViewById(R.id.fab);
                Pair <View, String> p1 = Pair.create((View)groupImage, transitionImage);
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, p1);
                //Start the Intent
                Singleton.getInstance().setmCurrentGroup(groups.get(currentPosition));
                //ActivityCompat.startActivity(context, intent, options.toBundle());
                if(isEnabled)
                    context.startActivity(intent, options.toBundle());
                AnimUtils.toggleOff(fab, 250, context);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Group_Activity.class);
                intent.putExtra("Position", position);
                Singleton.getInstance().setmCurrentGroup(groups.get(position));
                String transitionFab = context.getString(R.string.transition_fab);
                FloatingActionButton fab = (FloatingActionButton)((Activity)context).findViewById(R.id.fab);
                Pair <View, String> p1 = Pair.create((View)fab, transitionFab);
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, p1);
                context.startActivity(intent, options.toBundle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircularImageView groupProfile;
        TextView name;
        TextView balance;
        TextView badge;
        TextClock time;



        public ViewHolder(View itemView) {
            super(itemView);
            groupProfile = (CircularImageView)itemView.findViewById(R.id.iv_group_image);
            balance = (TextView)itemView.findViewById(R.id.tv_group_balance);
            name = (TextView)itemView.findViewById(R.id.tv_group_name);
            badge = (TextView)itemView.findViewById(R.id.tv_badge_counter);
            time = (TextClock)itemView.findViewById(R.id.tc_last_message);
        }
    }


}
