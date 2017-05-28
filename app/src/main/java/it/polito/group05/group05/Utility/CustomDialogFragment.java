package it.polito.group05.group05.Utility;

import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import it.polito.group05.group05.Expense_activity;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.Adapter.MemberExpandedAdapter;
import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;

import static it.polito.group05.group05.R.layout.custom_prices;

/**
 * Created by user on 27/05/2017.
 */

public class CustomDialogFragment extends DialogFragment {

    List<User_expense> partecipants;
    ExpenseDatabase e;
    RecyclerView recyclerView;
    Button confirm,reset;
    MemberExpandedAdapter memberAdapter;

    public CustomDialogFragment(List<User_expense> list, ExpenseDatabase e){
        this.partecipants= list;
        this.e= e;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(custom_prices, container ,false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_members);
        LinearLayoutManager l = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(l);
        memberAdapter = new MemberExpandedAdapter(partecipants, this.getActivity(), e.getPrice());
      /*  DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                l.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        */
        recyclerView.setAdapter(memberAdapter);
        if(e.getPrice()!= 0.0) {
            memberAdapter.changeTotal(e.getPrice());
        }
        confirm = (Button) rootView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        reset = (Button) rootView.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberAdapter.changeTotal(e.getPrice());
            }
        });
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = RecyclerView.LayoutParams.MATCH_PARENT;
        params.height = RecyclerView.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        this.getDialog().setTitle("Set Custom Prices");

        return rootView;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = RecyclerView.LayoutParams.MATCH_PARENT;
        params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }
}
