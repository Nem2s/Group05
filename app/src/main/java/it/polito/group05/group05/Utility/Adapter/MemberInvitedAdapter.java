package it.polito.group05.group05.Utility.Adapter;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.R;

import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.Holder.MemberInvitedHolder;
import it.polito.group05.group05.Utility.Interfaces.Namable;


/**
 * Created by Marco on 05/05/2017.
 */

public class MemberInvitedAdapter extends RecyclerView.Adapter<MemberInvitedHolder> implements Filterable {

    Context context;
    List<UserContact> invited;
    ViewGroup parent;
    public List<UserContact> selected;
    private List<UserContact> orig;

    public MemberInvitedAdapter(List<UserContact> invited, Context context) {
        this.context = context;
        this.invited = invited;
        selected = new ArrayList<>();

    }

    public void cleanUp() {
        this.invited.clear();
    }

    @Override
    public MemberInvitedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.invited_item_sample, parent, false);
        MemberInvitedHolder vh = new MemberInvitedHolder(v);
        this.parent = parent;
        return vh;
    }

    @Override
    public void onBindViewHolder(final MemberInvitedHolder holder, int position) {
        final UserContact currentUser = invited.get(position);
        holder.setData(currentUser, context);
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
                            if (((Namable)u).getName().toLowerCase().contains(constraint.toString().toLowerCase()))
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

}
