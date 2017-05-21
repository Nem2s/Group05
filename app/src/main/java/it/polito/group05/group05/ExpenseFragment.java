package it.polito.group05.group05;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.firebase.ui.database.ChangeEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.Holder.ExpenseHolder;

import static it.polito.group05.group05.Group_Activity.toolbar;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExpenseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {
    FirebaseRecyclerAdapter ea;
    RecyclerView rv;
    List<Expense> expenses;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private OnFragmentInteractionListener mListener;



    public ExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ExpenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpenseFragment newInstance() {
        ExpenseFragment fragment = new ExpenseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    private static void hideViews() {
        toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private static void showViews() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        //fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_group_, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.expense_rv);
        rv.setHasFixedSize(false);
        final LinearLayoutManager ll =  new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,true);
        rv.setLayoutManager(ll);
        ll.setStackFromEnd(true);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("expenses").child(Singleton.getInstance().getIdCurrentGroup());
        ea = new FirebaseRecyclerAdapter<ExpenseDatabase,ExpenseHolder>(ExpenseDatabase.class,
                R.layout.item_expense,ExpenseHolder.class,ref.orderByChild("timestamp")) {
            @Override
            protected void onChildChanged(ChangeEventListener.EventType type, int index, int oldIndex) {
                super.onChildChanged(type, index, oldIndex);
                if(type== ChangeEventListener.EventType.ADDED)
                    ll.smoothScrollToPosition(rv,null,this.getItemCount());
                //aggiungere animazioni strane

            }
            @Override
            protected void populateViewHolder(ExpenseHolder viewHolder, ExpenseDatabase model, int position) {
                if(model==null) return;
                viewHolder.setData(model,getContext());

            }
        };



        rv.setAdapter(ea);



        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Pair<View, String> p1 = Pair.create((View)appBar, getString(R.string.transition_appbar));
                Pair<View, String> p2 = Pair.create((View)toolbar, getString(R.string.transition_toolbar));
                Pair<View, String> p3 = Pair.create((View)c, getString(R.string.transition_group_image));
                Pair<View, String> p4 = Pair.create((View)tv, getString(R.string.transition_text));
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, p1, p2, p3, p4);*/

                Intent i = new Intent(getActivity(), Expense_activity.class);

                startActivity(i);
                //startActivity(i, options.toBundle());
            }
        });
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
    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onStart() {super.onStart();}
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


}
