package it.polito.group05.group05;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //RecyclerView.Adapter personAdapter;
    FirebaseIndexRecyclerAdapter personAdapter;
    RecyclerView rv;
    Context context;

    private Map<String, Double> usersBalance = new HashMap<>();
    private Map<String, Double> totalExpenses  = new HashMap<>();
    private Map<String, Double> usersTotalBalance = new HashMap<>();


    private OnFragmentInteractionListener mListener;

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
         personAdapter = new FirebaseIndexRecyclerAdapter(UserDatabase.class,
                R.layout.item_person_details_frag,
                PersonHolder.class,
                usersGroupRef,
                usersRef){
            @Override
            protected void populateViewHolder(RecyclerView.ViewHolder viewHolder, Object model, int position) {
                ((PersonHolder)viewHolder).setData(model,getContext(), usersBalance, totalExpenses, usersTotalBalance);
                //((PersonHolder)viewHolder).setData(model,getContext());
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


        };
        /*Map<String,Object> tmp= new HashMap<>(Singleton.getInstance().getmCurrentGroup().getMembers());
        tmp.remove(Singleton.getInstance().getCurrentUser().getId());
        final List<Object> userlist = new ArrayList<>(tmp.values());

        personAdapter = new RecyclerView.Adapter()
        {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View rootView = LayoutInflater.from(getContext()).inflate(R.layout.item_person_details_frag,parent,false);
                PersonHolder holder = new PersonHolder(rootView);
                return holder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((PersonHolder)holder).setData(userlist.get(position),getContext(), usersBalance, totalExpenses, usersTotalBalance);
            }

            @Override
            public int getItemCount() {
                return userlist.size();
            }
        };*/
        rv.setAdapter(personAdapter);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                .getReference("expenses/" + Singleton.getInstance().getmCurrentGroup().getId())
                .orderByKey()
                //.equalTo()
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        parseSnapshotAdded(dataSnapshot);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        //parseSnapshotChanged(dataSnapshot);

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        parseSnapshotRemoved(dataSnapshot);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void parseSnapshotAdded(DataSnapshot snapshot)
    {
        ExpenseDatabase expenseDatabase = snapshot.getValue(ExpenseDatabase.class);
        for(String i : expenseDatabase.getMembers().keySet()) {
                if (usersTotalBalance.containsKey(i))
                    usersTotalBalance.put(i, usersTotalBalance.get(i) + expenseDatabase.getMembers().get(i));
                else
                    usersTotalBalance.put(i, expenseDatabase.getMembers().get(i));

                if(expenseDatabase.getMembers().get(Singleton.getInstance().getCurrentUser().getId()) > 0) {
                    if (usersBalance.containsKey(i))
                        usersBalance.put(i, usersBalance.get(i) + expenseDatabase.getMembers().get(i));
                    else
                        usersBalance.put(i, expenseDatabase.getMembers().get(i));
                }
                else {
                    if (expenseDatabase.getMembers().get(i) > 0) {
                        if (usersBalance.containsKey(i)) {
                            usersBalance.put(i, usersBalance.get(i) - expenseDatabase.getMembers().get(Singleton.getInstance().getCurrentUser().getId()));
                            totalExpenses.put(i, totalExpenses.get(i) + expenseDatabase.getPrice());//.getMembers().get(i));
                        }
                            else {
                            usersBalance.put(i, expenseDatabase.getMembers().get(Singleton.getInstance().getCurrentUser().getId()) * -1);
                            totalExpenses.put(i, expenseDatabase.getPrice());//getMembers().get(i));
                        }
                    }
                }
            personAdapter.notifyDataSetChanged();
        }
    }

    private void parseSnapshotRemoved(DataSnapshot snapshot)
    {
        ExpenseDatabase expenseDatabase = snapshot.getValue(ExpenseDatabase.class);
        for(String i : expenseDatabase.getMembers().keySet()) {
                usersTotalBalance.put(i, usersTotalBalance.get(i) - expenseDatabase.getMembers().get(i));

            if(expenseDatabase.getMembers().get(Singleton.getInstance().getCurrentUser().getId()) > 0) {
                    usersBalance.put(i, usersBalance.get(i) - expenseDatabase.getMembers().get(i));
            }
            else {
                if (expenseDatabase.getMembers().get(i) > 0) {
                        usersBalance.put(i, usersBalance.get(i) + expenseDatabase.getMembers().get(Singleton.getInstance().getCurrentUser().getId()));
                        totalExpenses.put(i, totalExpenses.get(i) - expenseDatabase.getPrice());
                }
            }
            personAdapter.notifyDataSetChanged();
        }
    }

}
