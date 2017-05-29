package it.polito.group05.group05.Utility;

import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    List<User_expense> partecipants;
    ExpenseDatabase expense;
    RecyclerView recyclerView;
    Button confirm,reset;
    MemberExpandedAdapter memberAdapter;
    private DatabaseReference fdb;
    Double totalPriceActual;

    public CustomDialogFragment(List<User_expense> list, ExpenseDatabase e){
        this.partecipants= list;
        expense= e;
        totalPriceActual = 0.0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(custom_prices, container ,false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_members);
        LinearLayoutManager l = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(l);
        memberAdapter = new MemberExpandedAdapter(partecipants, this.getActivity(), expense.getPrice());
      /*  DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                l.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        */
        recyclerView.setAdapter(memberAdapter);
        if(expense.getPrice()!= 0.0) {
            memberAdapter.changeTotal(expense.getPrice());
        }
        confirm = (Button) rootView.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expense.getName().toString().length() == 0 || expense.getPrice() == 0.0) {
                    Snackbar.make(v,"Invalid name",Snackbar.LENGTH_SHORT).show();
                }
                else {
                    if (expense.getPrice().toString().length() > 6)
                        Snackbar.make(v, "Price on max 6 characters", Snackbar.LENGTH_SHORT).show();
                    else {
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
  */                      double price;
                        double toSubtractOwner = 0.0;
                        for (int i = 0; i < partecipants.size(); i++) {
                            if (!(partecipants.get(i).getId().equals(expense.getOwner()))) {
                                toSubtractOwner += partecipants.get(i).getCustomValue();
                            }
                        }
                        totalPriceActual = 0.0;
                        Map<String, Object> map_payed = new HashMap<>();
                        for (int i = 0; i < partecipants.size(); i++) {
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

                    }
                }




                dismiss();
            }
        });
        reset = (Button) rootView.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberAdapter.changeTotal(expense.getPrice());
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
