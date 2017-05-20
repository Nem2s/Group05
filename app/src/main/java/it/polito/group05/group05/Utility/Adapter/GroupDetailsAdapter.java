package it.polito.group05.group05.Utility.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

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
    private List<String> users;
    final static int TYPE_HEADER = 0;
    final static int TYPE_ITEM = 1;

    public GroupDetailsAdapter(Context context, List<String> users) {
        this.context = context;
        this.users = users;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
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
        FirebaseDatabase.getInstance().getReference("groups").child(Singleton.getInstance().getIdCurrentGroup()).child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseDatabase.getInstance().getReference("users").child(users.get(position)).child("userInfo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dsp) {
                        if (isCurrentUser(position))
                            ((MemberGroupDetailsHeaderHolder) holder).setData(dsp.getValue(UserDatabase.class), context);
                        else
                            ((MemberGroupDetailsHolder) holder).setData(dsp.getValue(UserDatabase.class), context);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
        return users.get(position).equals(Singleton.getInstance().getCurrentUser().getId());
    }


}
