package it.polito.group05.group05.Utility;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Balance;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.User;
import it.polito.group05.group05.Utility.BaseClasses.UserContact;

import static it.polito.group05.group05.Utility.MemberExpenseAdapter.ViewHolder.*;

/**
 * Created by user on 04/04/2017.
 */

public class MemberExpenseAdapter extends RecyclerView.Adapter<MemberExpenseAdapter.ViewHolder> {
    private List<User> member;
    LayoutInflater lin;
    Context c;

    public MemberExpenseAdapter(Context c, List<User> member) {
        lin = LayoutInflater.from(c);
        this.member = member;
        this.c = c;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_expence_row, parent, false);
        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setData(member.get(position));
       /*
        holder.costo_person.setText(String.valueOf(member.get(position).getBalance().getDebit()));
        holder.costo_person.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               holder.costo_person.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return member.size();
    }


    //VIEWHOLDER CLASS
    public class ViewHolder extends RecyclerView.ViewHolder {


        public CircularImageView image_person;
        public TextView name_person;
        public CheckBox include_person;
        public ImageView euro_person;
        public EditText costo_person;


        public ViewHolder(View itemView) {
            super(itemView);
            this.image_person = (CircularImageView) itemView.findViewById(R.id.iv_group_image);
            this.name_person = (TextView) itemView.findViewById(R.id.tv_name_member);
            this.include_person = (CheckBox) itemView.findViewById(R.id.cb_include);
            this.euro_person = (ImageView) itemView.findViewById(R.id.euro_member);
            this.costo_person = (EditText) itemView.findViewById(R.id.et_ins);
        }


        public void setData(User u) {
            image_person.setImageBitmap(u.getProfile_image());
            name_person.setText(u.getUser_name());
        }
    }
}