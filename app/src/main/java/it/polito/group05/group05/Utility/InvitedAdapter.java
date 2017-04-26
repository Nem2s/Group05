package it.polito.group05.group05.Utility;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.EventClasses.SelectionChangedEvent;

/**
 * Created by Marco on 04/04/2017.
 */

public class InvitedAdapter extends RecyclerView.Adapter<InvitedAdapter.ViewHolder> implements Filterable {

    Context context;
    List<UserContact> invited;
    ViewGroup parent;
    public List<User> selected;
    private List<UserContact> orig;
    private int validGroup;

    public InvitedAdapter(List<UserContact> invited, Context context) {
        this.context = context;
        this.invited = invited;
        selected = new ArrayList<>();
        validGroup = 0;

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
        if(currentUser.isSelected())
            holder.button.setVisibility(View.VISIBLE);
        else
            holder.button.setVisibility(View.INVISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.button.getVisibility() != View.VISIBLE) {
                    validGroup++;
                    currentUser.setSelected(true);
                   holder.button.show();

                }
                else {
                    validGroup--;
                    currentUser.setSelected(false);
                    holder.button.hide();

                }
                EventBus.getDefault().post(new SelectionChangedEvent(isValid()));
            }
        });
        holder.name.setText(currentUser.getUser_name());
    }

    public boolean isValid() {
        return validGroup > 0;
    }


    @Override
    public int getItemCount() {
        return invited.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<UserContact> results = new ArrayList<UserContact>();
                if (orig == null)
                    orig = invited;
                if (constraint != null) {
                    if (orig != null & orig.size() > 0) {
                        for (final UserContact u : orig) {
                            if (u.getUser_name().toLowerCase().contains(constraint.toString().toLowerCase()))
                                results.add(u);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                invited = (ArrayList<UserContact>)filterResults.values;
                notifyDataSetChanged();
            }
        };
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
