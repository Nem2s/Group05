package it.polito.group05.group05;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import it.polito.group05.group05.Utility.BaseClasses.HistoryClass;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.Holder.GeneralHolder;
import it.polito.group05.group05.Utility.Holder.HistoryHolder;

/**
 * Created by andre on 24-May-17.
 */

public class HistoryFragment extends Fragment {

    RecyclerView rv;
    //FirebaseRecyclerAdapter adapter;
    RecyclerView.Adapter adapter;
    List<HistoryClass> historyList = new ArrayList<>();

    public HistoryFragment() {
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retriveHistory();
        if (getArguments() != null) {
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_history, container, false);
        rv = (RecyclerView) root.findViewById(R.id.history_rv);
        final LinearLayoutManager ll=  new LinearLayoutManager(getActivity().getApplicationContext() ,LinearLayoutManager.VERTICAL,true);
        rv.setLayoutManager(ll);
        ll.setStackFromEnd(true);
        DatabaseReference historyref = FirebaseDatabase.getInstance().getReference("history/" + Singleton.getInstance().getmCurrentGroup().getId());
       /* adapter = new FirebaseRecyclerAdapter(HistoryClass.class, R.layout.history_element, HistoryHolder.class, historyref) {
            @Override
            protected void populateViewHolder(RecyclerView.ViewHolder viewHolder, Object model, int position) {
                ((HistoryHolder)viewHolder).setData(model,getActivity().getApplicationContext());
            }
        };*/
        adapter = new RecyclerView.Adapter(){

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View rootView = LayoutInflater.from(getContext()).inflate(R.layout.history_element,parent,false);
                GeneralHolder holder = new HistoryHolder(rootView);
                return holder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((GeneralHolder)holder).setData(historyList.get(position),getContext());
            }

            @Override
            public int getItemCount() {
                return historyList.size();
            }
        };

        rv.setAdapter(adapter);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("History");

        super.onCreateView(inflater, container, savedInstanceState);
        return root;
    }
    public void retriveHistory()
    {
        FirebaseDatabase.getInstance().getReference("history/" + Singleton.getInstance().getmCurrentGroup().getId())
                .orderByChild("when")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        HistoryClass h = dataSnapshot.getValue(HistoryClass.class);
                        historyList.add(h);
                        if(adapter!= null)
                            adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

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
