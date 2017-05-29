package it.polito.group05.group05.Utility.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;
import it.polito.group05.group05.Utility.Holder.MemberContactsHolder;
import it.polito.group05.group05.Utility.Interfaces.Namable;

/**
 * Created by Marco on 25/05/2017.
 */

public class MemberContactsAdapter extends RecyclerView.Adapter<MemberContactsHolder> implements Filterable {

    Context context;
    List<UserContact> contacts; //IMPLEMENTARE SORTEDLIST! https://stackoverflow.com/a/30429439/5382017
    ViewGroup parent;
    public List<UserContact> selected;
    private List<UserContact> orig;


    public MemberContactsAdapter(List<UserContact> local_contacts, Context context) {
        this.contacts = local_contacts;
        this.context = context;
        this.selected = new ArrayList<>();
    }

    @Override
    public MemberContactsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_sample, parent, false);
        MemberContactsHolder vh = new MemberContactsHolder(v);
        this.parent = parent;
        return vh;
    }

    @Override
    public void onBindViewHolder(MemberContactsHolder holder, int position) {
        final UserContact currentUser = contacts.get(position);
        holder.setData(currentUser, context);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<UserContact> results = new ArrayList<UserContact>();
                if (orig == null)
                    orig = contacts;
                if (constraint != null) {
                    if (orig != null & orig.size() > 0) {
                        for (final UserContact u : orig) {
                            if (((Namable)u).getName().toLowerCase().startsWith(constraint.toString().toLowerCase()))
                                results.add(u);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contacts = (ArrayList<UserContact>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
