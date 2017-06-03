package it.polito.group05.group05.Utility.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.CurrentUser;
import it.polito.group05.group05.Utility.BaseClasses.GroupDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.Holder.GeneralHolder;
import it.polito.group05.group05.Utility.Holder.GroupHolder;

/**
 * Created by andre on 01-Jun-17.
 */

public class GroupAdapter extends RecyclerView.Adapter {

    private Context context;
    TextView tv_no_groups;
    ImageView iv_no_groups;
    ProgressBar pb;
    Map<String, GroupDatabase> groupMap = new HashMap<>();
    List<GroupDatabase> groupList = new ArrayList<>();

    public GroupAdapter(Context context, TextView tv_no_groups, ImageView iv_no_groups, ProgressBar pb) {
        this.context = context;
        this.tv_no_groups = tv_no_groups;
        this.iv_no_groups = iv_no_groups;
        this.pb = pb;
        tv_no_groups.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
        iv_no_groups.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);

        retriveGroups();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.group_item_sample, parent, false);
        GeneralHolder holder = new GroupHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String id = groupList.get(position).getId();
        groupList.get(position).getMembers().clear();
        groupList.get(position).setMembers(new HashMap<>(groupMap.get(id).getMembers()));
        ((GroupHolder)holder).setData(groupList.get(position),context);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public void retriveGroups() {
        FirebaseDatabase.getInstance().getReference("users").child(Singleton.getInstance().getCurrentUser().getId()).child("userGroups")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final String groupID = dataSnapshot.getKey();
                        if (!groupID.equals("00")) {
                            if (!((CurrentUser) Singleton.getInstance().getCurrentUser()).getGroups().contains(groupID))
                                ((CurrentUser) Singleton.getInstance().getCurrentUser()).getGroups().add(groupID);
                            FirebaseDatabase.getInstance().getReference("groups/" + groupID)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() == null) return;
                                            GroupDatabase g = dataSnapshot.getValue(GroupDatabase.class);
                                            if(g.getMembers().size() == 0 | g.getMembers().size() == 1) return;
                                            if(groupMap.containsKey(g.getId())) {
                                                for (GroupDatabase group : groupList) {
                                                    if (group.getId().equals(g.getId())) {
                                                        groupList.remove(group);
                                                        groupList.add(new GroupDatabase(g));
                                                        break;
                                                    }
                                                }
                                            }
                                            else groupList.add(new GroupDatabase(g));
                                            groupMap.put(dataSnapshot.getKey(), new GroupDatabase(g));
                                            tv_no_groups.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
                                            iv_no_groups.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
                                            pb.setVisibility(View.GONE);
                                            Collections.sort(groupList, new Comparator<GroupDatabase>() {
                                                @Override
                                                public int compare(GroupDatabase o1, GroupDatabase o2) {
                                                    return Long.compare(o2.getLmTime(), o1.getLmTime());
                                                }
                                            });

                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) return;
                        String g = dataSnapshot.getKey();
                        groupMap.remove(g);
                        for (GroupDatabase group : groupList) {
                            if (group.getId().equals(g)) {
                                notifyItemRemoved(groupList.indexOf(group));
                                groupList.remove(group);
                                break;
                            }
                        }
                        tv_no_groups.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
                        iv_no_groups.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}