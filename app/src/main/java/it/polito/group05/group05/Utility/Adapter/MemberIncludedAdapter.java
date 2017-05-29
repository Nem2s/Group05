package it.polito.group05.group05.Utility.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;
import it.polito.group05.group05.Utility.Holder.PersonSelectedHolder;

/**
 * Created by user on 29/05/2017.
 */

public class MemberIncludedAdapter extends RecyclerView.Adapter<PersonSelectedHolder> {
    private List<User_expense> users;
    Context context;

    public MemberIncludedAdapter(List<User_expense> users, Context context){
        this.users= users;
        this.context=context;
    }

    @Override
    public PersonSelectedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_row_included, parent, false);
        return new PersonSelectedHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PersonSelectedHolder holder, int position) {
       holder.setData(users.get(position),context);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
