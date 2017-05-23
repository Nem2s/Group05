package it.polito.group05.group05;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.Holder.GeneralHolder;
import it.polito.group05.group05.Utility.Holder.PersonHolder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupDetailsFragment extends Fragment {

    RecyclerView.Adapter personAdapter;
    RecyclerView rv;
    Context context;

    public GroupDetailsFragment() {
        // Required empty public constructor

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GroupDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupDetailsFragment newInstance() {
        GroupDetailsFragment fragment = new GroupDetailsFragment();
        //  Bundle args = new Bundle();
        //  args.putString(ARG_PARAM1, param1);
        //  args.putString(ARG_PARAM2, param2);
        //  fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Singleton.getInstance().getUsersBalance().clear();
        retriveExpense();
        super.onCreate(savedInstanceState);
  /*      if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_group_details, container, false);
        rv = (RecyclerView) rootView.findViewById(R.id.detail_fragment_rv);
        rv.setHasFixedSize(false);
        final LinearLayoutManager ll=  new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true);
        rv.setLayoutManager(ll);
        ll.setStackFromEnd(true);
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference usersGroupRef = FirebaseDatabase.getInstance().getReference("groups").child(Singleton.getInstance().getIdCurrentGroup()).child("members");
        /*FirebaseIndexRecyclerAdapter personAdapter = new FirebaseIndexRecyclerAdapter(UserDatabase.class,
                R.layout.item_person_details_frag,
                PersonHolder.class,
                usersGroupRef,
                usersRef){
            @Override
            protected void populateViewHolder(RecyclerView.ViewHolder viewHolder, Object model, int position) {
                ((PersonHolder)viewHolder).setData(model,getContext());
            }

            @Override
            protected Object parseSnapshot(DataSnapshot snapshot) {
                //if(snapshot.getKey().equals(Singleton.getInstance().getCurrentUser().getId())) return null;
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.getKey().equals("userInfo"))
                        return child.getValue(UserDatabase.class);
                }
                return null;
                //return super.parseSnapshot(sp);
            }


        };*/

        final List<UserDatabase> userlist = new ArrayList<>();
        for(Object u : Singleton.getInstance().getmCurrentGroup().getMembers().values())
        {
            if (!(u instanceof UserDatabase)) continue;
            UserDatabase tmp = (UserDatabase) u;
            if (tmp.getId().equals(Singleton.getInstance().getCurrentUser().getId())) continue;
            userlist.add(tmp);
        }
        personAdapter = new RecyclerView.Adapter()
        {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View rootView = LayoutInflater.from(getContext()).inflate(R.layout.item_person_details_frag,parent,false);
                GeneralHolder holder = new PersonHolder(rootView);
                return holder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((GeneralHolder)holder).setData(userlist.get(position),getContext());
            }

            @Override
            public int getItemCount() {
                return userlist.size();
            }
        };
        rv.setAdapter(personAdapter);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("History");
        return rootView;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void retriveExpense(){
        FirebaseDatabase.getInstance()
                .getReference("expenses")
                .orderByKey()
                .equalTo(Singleton.getInstance().getmCurrentGroup().getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child1 : dataSnapshot.getChildren()) {
                            for (DataSnapshot child2 : child1.getChildren()) {
                                //for (DataSnapshot child3 : child2.getChildren())
                                //    if (child3.getKey().equals("members"))
                                //parseSnapshot1(child2);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void parseSnapshot1(DataSnapshot snapshot)
    {
        //Map<String, Double> expenseDatabase = (Map<String, Double>) snapshot.getValue();
        ExpenseDatabase expenseDatabase = snapshot.getValue(ExpenseDatabase.class);
        for(String i : expenseDatabase.getMembers().keySet()) {
            //if(expenseDatabase.getMembers().containsKey(Singleton.getInstance().getCurrentUser().getId()) && !i.equals(Singleton.getInstance().getCurrentUser().getId())) {
                if(expenseDatabase.getMembers().get(Singleton.getInstance().getCurrentUser().getId()) > 0) {
                    if (Singleton.getInstance().getUsersBalance().containsKey(i))
                        Singleton.getInstance().getUsersBalance().put(i, Singleton.getInstance().getUsersBalance().get(i) + expenseDatabase.getMembers().get(i));
                    else
                        Singleton.getInstance().getUsersBalance().put(i, expenseDatabase.getMembers().get(i));
                }
                else {
                    if (expenseDatabase.getMembers().get(i) > 0) {
                        if (Singleton.getInstance().getUsersBalance().containsKey(i))
                            Singleton.getInstance().getUsersBalance().put(i, Singleton.getInstance().getUsersBalance().get(i) - expenseDatabase.getMembers().get(Singleton.getInstance().getCurrentUser().getId()));
                        else
                            Singleton.getInstance().getUsersBalance().put(i, expenseDatabase.getMembers().get(Singleton.getInstance().getCurrentUser().getId())*-1);
                    }
                }
            //}
//            personAdapter.notifyDataSetChanged();
        }
    }

}
