package it.polito.group05.group05.Utility;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.Adapter.MemberExpandedAdapter;
import it.polito.group05.group05.Utility.Adapter.MemberIncludedAdapter;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;

import static it.polito.group05.group05.R.layout.custom_included;


/**
 * Created by user on 29/05/2017.
 */

public class CustomIncludedDialog extends DialogFragment {

    List<User_expense> list, newList;
    ExpenseDatabase expense;
    RecyclerView recyclerView;
    MemberIncludedAdapter memberAdapter;
    FloatingActionButton next;
    FragmentManager fm;
    CustomDialogFragment cdf;
    DatabaseReference fdb;


    public CustomIncludedDialog(List<User_expense> list, ExpenseDatabase e, DatabaseReference fdb) {
        this.list = list;
        this.expense = e;
        this.fdb = fdb;
        newList = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(custom_included, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_members);
        next = (FloatingActionButton) rootView.findViewById(R.id.next);
        LinearLayoutManager l = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(l);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int a =  (displaymetrics.heightPixels*40)/100;
        recyclerView.getLayoutParams().height =a;

        memberAdapter = new MemberIncludedAdapter(list, this.getActivity());
        recyclerView.setAdapter(memberAdapter);
        fm = getFragmentManager();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchSelection();
                cdf = new CustomDialogFragment(list, newList, expense, fdb);
                cdf.show(fm, "Tv-TAG");
                dismiss();
            }
        });
        this.getDialog().setTitle("Choose Friends");
        return rootView;


    }

    private void searchSelection() {
        for (int i = 0; i < list.size(); i++) {
            User_expense e = list.get(i);
            if(!e.isExcluded() || (e.getId().equals(Singleton.getInstance().getCurrentUser().getId()))){
                if(!newList.contains(e))
                {
                    newList.add(e);
                }}
        }
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