package it.polito.group05.group05.Utility.Adapter;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;
import it.polito.group05.group05.Utility.Holder.MemberIncludedHolder;

/**
 * Created by user on 05/05/2017.
 */

public class MemberExpandedAdapter extends RecyclerView.Adapter<MemberIncludedHolder> {
    private List<User_expense> users;

    private double total;
    private double costPerson;
    private double differece = 0.0;
    private boolean bind = true;
    Context context;
    int numberOfPersonCustom =0;
    double newTotal = 0.0;


    public MemberExpandedAdapter(List<User_expense> users, Context context, double total){
        this.users= users;
        this.context= context;
        this.total = total;


    }

    @Override
    public MemberIncludedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_expence_row, parent, false);
        return new MemberIncludedHolder(itemView, users);
    }

    @Override
    public void onBindViewHolder(final MemberIncludedHolder holder, int position) {
        bind = true;
        final User_expense ue = users.get(position);
        final int pos = position;
        holder.name_person.setText(ue.getName());
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReference("users")
                        .child(ue.getId())
                        .child(ue.getiProfile()))
                .asBitmap()
                .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                .into(holder.image_person);
        holder.euro_person.setImageResource(R.drawable.euro);
        holder.costo_person.setText(String.valueOf(ue.getCustomValue()));

        if(bind){
            setListeners(ue, holder);
        }
        bind = false;
    }

    private void setListeners(User_expense ue, MemberIncludedHolder holder) {
       final User_expense user = ue;
        holder.costo_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setSelected(true);

            }
        });
        holder.costo_person.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    user.setCustomValue(Double.valueOf(s.toString().replace(',', '.')));
                    if(user.isSelected()){
                        Double tmp= total;
                        int count=0;
                        for (User_expense e : users){
                            if(e.isSelected()) {
                                count++;
                                tmp -= e.getCustomValue();
                            }
                        }
                        for (int e=0;e<users.size(); e++)
                        {
                            if(!users.get(e).isSelected()) {
                                Double tmpD=Double.parseDouble(Integer.toString(users.size()-count));
                                if(tmpD<0.9) return;
                                users.get(e).setCustomValue(tmp / tmpD);
                                notifyItemChanged(e);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void changeTotal(double total){
        this.total = total;

        for(int j =0 ; j < users.size(); j++){
            User_expense e = users.get(j);
                e.setCustomValue(total / (users.size()));
        }
        notifyDataSetChanged();
    }

}



