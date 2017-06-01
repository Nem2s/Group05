package it.polito.group05.group05.Utility.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.util.SortedList;
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
import java.util.Collection;
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
    SortedList<UserContact> invited = new SortedList<UserContact>(UserContact.class, new SortedList.Callback<UserContact>() {
        @Override
        public int compare(UserContact o1, UserContact o2) {
            return (o1.getName().compareTo(o2.getName()));
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(UserContact oldItem, UserContact newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(UserContact item1, UserContact item2) {
            return item1 == item2;
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }
    });
    ViewGroup parent;
    public List<UserContact> selected;
    private List<UserContact> orig;

    public MemberInvitedAdapter(List<UserContact> invited, Context context) {
        this.context = context;
        this.invited.addAll(invited);
        selected = new ArrayList<>();

    }

    public void cleanUp() {
        this.invited.clear();
    }

    public void replaceAll(List<UserContact> models) {
        invited.beginBatchedUpdates();
        for (int i = invited.size() - 1; i >= 0; i--) {
            final UserContact model = invited.get(i);
            if (!models.contains(model)) {
                invited.remove(model);
            }
        }
        invited.addAll(models);
        invited.endBatchedUpdates();
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

    public List<UserContact> retriveAll() {
        List<UserContact> list = new ArrayList<>();
        for(int i = invited.size() -1 ; i >= 0 ; i--) {
            UserContact u = invited.get(i);
            list.add(u);
        }
        return list;
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
                    orig = retriveAll();
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
                replaceAll(new ArrayList<UserContact>((Collection<? extends UserContact>) filterResults.values));


            }
        };
    }

}
