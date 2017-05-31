package it.polito.group05.group05.Utility;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.R;
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


    public CustomIncludedDialog(List<User_expense> list, ExpenseDatabase e) {
        this.list = list;
        this.expense = e;
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
        memberAdapter = new MemberIncludedAdapter(list, this.getActivity());
        recyclerView.setAdapter(memberAdapter);
        fm = getFragmentManager();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchSelection();
                cdf = new CustomDialogFragment(list, newList, expense);
                cdf.show(fm, "Tv-TAG");
                dismiss();
            }
        });
        this.getDialog().setTitle("Include Friends");
        return rootView;

    }

    private void searchSelection() {
        for (int i = 0; i < list.size(); i++) {
            User_expense e = list.get(i);
            if (e.isIncluded() || (e.getId().equals(Singleton.getInstance().getCurrentUser().getId()))) {
                if (!newList.contains(e)) {
                    newList.add(e);
                }
            }
        }
    }

    private void removeSelection() {
        for (int i = 0; i < list.size(); i++) {
            User_expense e = list.get(i);
            if (e.isIncluded()) {
                e.setIncluded(false);
            }
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
