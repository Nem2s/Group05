package it.polito.group05.group05.Utility;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import java.util.List;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.User;

import static android.R.id.list;

/**
 * Created by user on 04/04/2017.
 */

public class MemberExpenseAdapter extends RecyclerView.Adapter<MemberExpenseAdapter.ViewHolder> {

    List<User> member;
    LayoutInflater lin;
    Context c;

    //VIEWHOLDER CLASS
    public static class ViewHolder extends RecyclerView.ViewHolder {


        public CircularImageView image_person;
        public TextView name_person;
        public CheckBox include_person;
        public ImageView euro_person;
        public EditText costo_person;

        public ViewHolder(View itemView){
            super(itemView);
            image_person= (CircularImageView) itemView.findViewById(R.id.iv_group_image);
            name_person= (TextView) itemView.findViewById(R.id.tv_name_member);
            include_person = (CheckBox) itemView.findViewById(R.id.cb_include);
            euro_person= (ImageView) itemView.findViewById(R.id.euro_member);
            costo_person= (EditText) itemView.findViewById(R.id.et_ins);

        }

        public void setData(User u){
            image_person.setImageResource(Integer.parseInt(u.getProfile_image()));
            name_person.setText(u.getUser_name());
        }
    }

    public MemberExpenseAdapter(Context c, List<User> member){
        lin= LayoutInflater.from(c);
        this.member=member;
        this.c = c;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = lin.inflate(R.layout.member_expence_row, parent, false);
        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(member.get(position));

    }

    @Override
    public int getItemCount() {
        return member.size();
    }


}
