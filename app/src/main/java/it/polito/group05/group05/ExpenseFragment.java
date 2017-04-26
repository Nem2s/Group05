package it.polito.group05.group05;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.Group;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.ExpenseAdapter;
import it.polito.group05.group05.Utility.HideScrollListener;

import static it.polito.group05.group05.Group_Activity.fab;
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
    ExpenseAdapter ea;
    RecyclerView rv;
    List<Expense> expenses;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private OnFragmentInteractionListener mListener;

    @Override
    public void onResume() {
        super.onResume();
        if(ea != null) {
            expenses.clear();
            expenses.addAll(Singleton.getInstance().getmCurrentGroup().getExpenses());
            ea.notifyDataSetChanged();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        ea.notifyDataSetChanged();
    }

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

        //FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) fab.getLayoutParams();
        //int fabBottomMargin = lp.bottomMargin;
        //fab.animate().translationY(fab.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private static void showViews() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final Group currentGroup = Singleton.getInstance().getmCurrentGroup();
        View rootView = inflater.inflate(R.layout.fragment_group_, container, false);
        rv = (RecyclerView) rootView.findViewById(R.id.expense_rv);
        rv.setOnScrollListener(new HideScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }
            @Override
            public void onShow() {
                showViews();
            }

        });

        expenses =new ArrayList<>(currentGroup.getExpenses());

        ea = new ExpenseAdapter(getContext(),expenses);
        LinearLayoutManager llm = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        rv.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        rv.setLayoutManager(llm);
        rv.setAdapter(ea);

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


}
