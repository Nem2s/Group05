package it.polito.group05.group05;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.ChangeEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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
    LinearLayoutManager ll;
    String ei;
    ImageView iv_noexpense;
    TextView tv_noexpense;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_group_, menu);
        MenuItem subMenu = menu.findItem(R.id.sort_action);
        inflater.inflate(R.menu.sort_menu, subMenu.getSubMenu());
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        QueryParam.Possibilities q = null;
        //noinspection SimplifiableIfStatement
        if (id == R.id.sort_price)
            q = QueryParam.Possibilities.BYPRICE;
        else if (id == R.id.sort_owner)
            q = QueryParam.Possibilities.BYOWNER;
        else if (id == R.id.sort_date)
            q = QueryParam.Possibilities.BYDATE;
        if (q != null)
            sortRecyclerViewBy(q);
        item.setChecked(true);
        return super.onOptionsItemSelected(item);
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
    }

    private static void showViews() {
        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        //fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ei = getActivity().getIntent().getStringExtra("expenseId");
        View rootView = inflater.inflate(R.layout.fragment_group_, container, false);
        setHasOptionsMenu(true);
        rv = (RecyclerView) rootView.findViewById(R.id.expense_rv);
        iv_noexpense = (ImageView)rootView.findViewById(R.id.iv_no_expenses);
        tv_noexpense = (TextView)rootView.findViewById(R.id.tv_no_expenses);
        rv.setHasFixedSize(true);
        ll = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true);
        rv.setLayoutManager(ll);
        ll.setStackFromEnd(true);



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("expenses").child(Singleton.getInstance().getIdCurrentGroup());
        ea = new FirebaseRecyclerAdapter<ExpenseDatabase,ExpenseHolder>(ExpenseDatabase.class,
                R.layout.item_expense,ExpenseHolder.class,ref.orderByChild("timestamp")) {


            @Override
            protected void onChildChanged(ChangeEventListener.EventType type, int index, int oldIndex) {
                super.onChildChanged(type, index, oldIndex);
                tv_noexpense.setVisibility(ea.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                iv_noexpense.setVisibility(ea.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                if(type== ChangeEventListener.EventType.ADDED) {
                    ll.smoothScrollToPosition(rv,null,this.getItemCount());
                }

                //aggiungere animazioni strane

            }
            @Override
            protected void populateViewHolder(ExpenseHolder viewHolder, ExpenseDatabase model, int position) {
                if(model==null) return;
                viewHolder.setData(model,getContext());

            }

            @Override
            protected void onDataChanged() {
                super.onDataChanged();
                tv_noexpense.setVisibility(ea.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                iv_noexpense.setVisibility(ea.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };


        rv.setAdapter(ea);


        super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event

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

    private void sortRecyclerViewBy(QueryParam.Possibilities param) {

        Query ref = FirebaseDatabase.getInstance().getReference("expenses").child(Singleton.getInstance().getmCurrentGroup().getId()).orderByChild(param.getValue());
        ea = new FirebaseRecyclerAdapter<ExpenseDatabase, ExpenseHolder>(ExpenseDatabase.class,
                R.layout.item_expense, ExpenseHolder.class, ref) {
            @Override
            protected void onChildChanged(ChangeEventListener.EventType type, int index, int oldIndex) {
                super.onChildChanged(type, index, oldIndex);
                if (type == ChangeEventListener.EventType.ADDED)
                    ll.smoothScrollToPosition(rv, null, this.getItemCount());
                //aggiungere animazioni strane

            }

            @Override
            protected void populateViewHolder(ExpenseHolder viewHolder, ExpenseDatabase model, int position) {
                if (model == null) return;
                viewHolder.setData(model, getContext());

            }
        };

        rv.setAdapter(ea);
    }


}

class QueryParam {
    public enum Possibilities {
        BYPRICE("price"),
        BYOWNER("owner"),
        BYDATE("timestamp");
        private String string;

        private Possibilities(String s) {
            this.string = s;
        }

        public String getValue() {
            return string;
        }
    }

    public QueryParam() {
    }
}
