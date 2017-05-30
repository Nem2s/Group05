package it.polito.group05.group05.Utility;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.group05.group05.Expense_activity;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.Adapter.MemberExpandedAdapter;
import it.polito.group05.group05.Utility.BaseClasses.Expense;
import it.polito.group05.group05.Utility.BaseClasses.ExpenseDatabase;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;
import it.polito.group05.group05.Utility.HelperClasses.DB_Manager;

import static it.polito.group05.group05.R.layout.custom_prices;

/**
 * Created by user on 27/05/2017.
 */

public class CustomDialogFragment extends DialogFragment {

    List<User_expense> partecipants, listaCompleta;
    ExpenseDatabase expense;
    RecyclerView recyclerView;
    FloatingActionButton back, confirm, reset;
    MemberExpandedAdapter memberAdapter;
    private DatabaseReference fdb;
    Double totalPriceActual;
    CustomIncludedDialog c;
    FragmentManager fm;

    public CustomDialogFragment(List<User_expense> listOriginal, List<User_expense> list, ExpenseDatabase e) {
        this.listaCompleta = listOriginal;
        this.partecipants = list;
        expense = e;
        totalPriceActual = 0.0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(custom_prices, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_members);
        LinearLayoutManager l = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(l);

        memberAdapter = new MemberExpandedAdapter(partecipants, this.getActivity(), expense.getPrice());
        recyclerView.setAdapter(memberAdapter);
        if (expense.getPrice() != 0.0) {
            memberAdapter.changeTotal(expense.getPrice());
        }
        confirm = (FloatingActionButton) rootView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fdb = FirebaseDatabase.getInstance()
                        .getReference("expenses")
                        .child(Singleton.getInstance().getmCurrentGroup().getId())
                        .push();
                DatabaseReference fdbgroup = FirebaseDatabase.getInstance().getReference("groups").child(Singleton.getInstance().getmCurrentGroup().getId())
                        .child("lmTime");
                expense.setId(fdb.getKey());
                expense.setOwner(Singleton.getInstance().getCurrentUser().getId());
/*
                        if (nameFILE != null) {
                            expense.setFile(nameFILE);
                            upLoadFile(uri);
                        }
  */
                double price;
                double toSubtractOwner = 0.0;
                for (int i = 0; i < partecipants.size(); i++) {
                    if (!(partecipants.get(i).getId().equals(expense.getOwner()))) {
                        toSubtractOwner += partecipants.get(i).getCustomValue();
                    }
                }
                totalPriceActual = 0.0;
                Map<String, Object> map_payed = new HashMap<>();
                for (int i = 0; i < partecipants.size(); i++) {
                    partecipants.get(i).setIncluded(false);
                    price = partecipants.get(i).getCustomValue();
                    totalPriceActual += partecipants.get(i).getCustomValue();
                    String id = partecipants.get(i).getId();

                    if (partecipants.get(i).getId().equals(expense.getOwner())) {
                        expense.getMembers().put(partecipants.get(i).getId(), toSubtractOwner);
                        map_payed.put(id, true);
                    } else {
                        expense.getMembers().put(partecipants.get(i).getId(), (-1.00) * price);
                        map_payed.put(id, false);
                    }
                    DB_Manager.getInstance().updateGroupFlow(id, -1.00 * expense.getMembers().get(id));

                }

                if ((totalPriceActual - expense.getPrice()) > 0.001 || (totalPriceActual - expense.getPrice()) < -0.001) {
                    Snackbar.make(v, "Set prices again", Snackbar.LENGTH_SHORT).show();
                    memberAdapter.changeTotal(expense.getPrice());
                } else {
                    expense.setPayed(map_payed);
                    DB_Manager.getInstance().newhistory(Singleton.getInstance().getmCurrentGroup().getId(), expense);
                    fdb.setValue(expense);
                    getActivity().finish();
                }
                dismiss();
            }
        });
        reset = (FloatingActionButton) rootView.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberAdapter.changeTotal(expense.getPrice());
            }
        });
        back = (FloatingActionButton) rootView.findViewById(R.id.action_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < partecipants.size(); i++) {
                    partecipants.remove(i);
                }
                fm = getFragmentManager();
                c = new CustomIncludedDialog(listaCompleta, expense);
                c.show(fm, "TAG");
                dismiss();
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
