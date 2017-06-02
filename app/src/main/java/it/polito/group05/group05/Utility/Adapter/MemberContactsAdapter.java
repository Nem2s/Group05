/*
package it.polito.group05.group05.Utility.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.util.SortedList;
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

import it.polito.group05.group05.Utility.Interfaces.Namable;

*/
/**
 * Created by Marco on 25/05/2017.
 *//*


public class MemberContactsAdapter extends RecyclerView.Adapter<MemberContactsHolder> implements Filterable {

    Context context;
    SortedList<UserContact> contacts = new SortedList<UserContact>(UserContact.class, new SortedList.Callback<UserContact>() {
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


    public MemberContactsAdapter(List<UserContact> local_contacts, Context context) {
        this.contacts.addAll(local_contacts);
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

    public void add(UserContact model) {
        contacts.add(model);
    }

    public void remove(UserContact model) {
        contacts.remove(model);
    }

    public void add(List<UserContact> models) {
        contacts.addAll(models);
    }

    public void remove(List<UserContact> models) {
        contacts.beginBatchedUpdates();
        for (UserContact model : models) {
            contacts.remove(model);
        }
        contacts.endBatchedUpdates();
    }

    public void replaceAll(List<UserContact> models) {
        contacts.beginBatchedUpdates();
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.show();
        for (int i = contacts.size() - 1; i >= 0; i--) {
            final UserContact model = contacts.get(i);
            if (!models.contains(model)) {
                contacts.remove(model);
            }
        }
        contacts.addAll(models);
        contacts.endBatchedUpdates();
        dialog.dismiss();
    }

    public List<UserContact> retriveAll() {
        List<UserContact> list = new ArrayList<>();
        for(int i = contacts.size() -1 ; i >= 0 ; i--) {
            UserContact u = contacts.get(i);
            list.add(u);
        }
        return list;
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
                    orig = retriveAll();
                if (constraint != null) {
                    if (orig != null & orig.size() > 0) {
                        for (final UserContact u : orig) {
                            if (!((Namable)u).getName().toLowerCase().contains(constraint.toString().toLowerCase()))
                                remove(u);
                        }
                    }
                    oReturn.values = retriveAll();
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            }
        };
    }

    public List<UserContact> filter(String newText) {
        List<UserContact> res = new ArrayList<>();

        for(UserContact u : retriveAll()) {
            if (((Namable)u).getName().toLowerCase().contains(newText.toString().toLowerCase()))
                res.add(u);

        }
        return res;
    }
}
*/
