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
import java.util.List;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.User;

/**
 * Created by user on 04/04/2017.
 */

public class MemberExpenseAdapter extends RecyclerView.Adapter<MemberExpenseAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void OnItemClicked(User position);
    }

    private final OnItemClickListener listener;
    private List<User> member;
    private Double cost_procapite;
    LayoutInflater lin;
    Context c;

    public MemberExpenseAdapter(Context c, List<User> member, OnItemClickListener listener) {
        lin = LayoutInflater.from(c);
        this.member = member;
        this.c = c;
        this.listener= listener;
        cost_procapite= 0.0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_expence_row, parent, false);
        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setData(member.get(position), listener);
        holder.include_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            member.get(position).setSelection();
            }
        });
        holder.costo_person.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s != null){
                  try{
                    cost_procapite =Double.parseDouble(s.toString().replace(',', '.'));
                    holder.costo_person.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                    }
                    catch (NumberFormatException e){
                        cost_procapite = 0.0;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return member.size();
    }



    //VIEWHOLDER CLASS
    public static class ViewHolder extends RecyclerView.ViewHolder {
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


        public void setData(final User u, final OnItemClickListener listener) {
            image_person.setImageBitmap(u.getProfile_image());
            name_person.setText(u.getUser_name());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClicked(u);
                }
            });
        }

    }
}