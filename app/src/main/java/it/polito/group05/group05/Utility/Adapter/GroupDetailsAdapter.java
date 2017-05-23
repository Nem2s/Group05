package it.polito.group05.group05.Utility.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.Holder.MemberGroupDetailsHeaderHolder;
import it.polito.group05.group05.Utility.Holder.MemberGroupDetailsHolder;

/**
 * Created by Marco on 18/05/2017.
 */

public class GroupDetailsAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Object> users;
    private Map<String, Object> helper = new HashMap<>();

    final static int TYPE_HEADER = 0;
    final static int TYPE_ITEM = 1;

    public GroupDetailsAdapter(Context context, Collection<Object> users) {
        this.context = context;
        this.users = new ArrayList<>(users);

    }

    public synchronized int addUser(UserDatabase u) {
        int i = exists(u);
        if (i < 0) {
            i = getItemCount();
            this.users.add(u);
        } else {
            this.users.remove(i);
            this.users.add(i,u);
        }
        return i;
    }

    public synchronized int removeUser(UserDatabase u) {
        int i = exists(u);
        if(i != -1) {
            this.users.remove(i);

        }
        return i;
    }

    private synchronized int exists(UserDatabase u) {
        for(int i = 0; i < getItemCount(); i++) {
            if(((UserDatabase)users.get(i)).getId().equals(u.getId()))
                return i;
        }
        return -1;
    }

    public synchronized int getIndex(UserDatabase u) {
        for(int i = 0; i < getItemCount(); i++) {
            if(((UserDatabase)users.get(i)).getId().equals(u.getId()))
                return i;
        }
        return -1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_rv_group_details_header,
                    parent, false);

            return new MemberGroupDetailsHeaderHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_rv_group_details,
                    parent, false);

            return new MemberGroupDetailsHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(isCurrentUser(position))
            ((MemberGroupDetailsHeaderHolder)holder).setData(users.get(position), context);
        else
            ((MemberGroupDetailsHolder)holder).setData(users.get(position), context);
            }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isCurrentUser(position) ? TYPE_HEADER : TYPE_ITEM;
    }

    public boolean isCurrentUser(int position) {
        return ((UserDatabase)users.get(position)).getId().equals(Singleton.getInstance().getCurrentUser().getId());
    }




}
