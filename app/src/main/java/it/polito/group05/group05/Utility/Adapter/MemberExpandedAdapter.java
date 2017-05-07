package it.polito.group05.group05.Utility.Adapter;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;
import it.polito.group05.group05.Utility.Event.ExpenseDividerEvent;
import it.polito.group05.group05.Utility.Event.PartecipantsNumberChangedEvent;
import it.polito.group05.group05.Utility.Event.PriceChangedEvent;
import it.polito.group05.group05.Utility.Event.PriceErrorEvent;

/**
 * Created by user on 05/05/2017.
 */

public class MemberExpandedAdapter extends RecyclerView.Adapter<MemberExpandedAdapter.MemberIncludedHolder> {
    private List<User_expense> users;
    private Double cost_procapite;
    LayoutInflater lin;
    private double costPerUser;
    private boolean prezzocambiato = false;
    private double costAnna = 0.0;
    Context context;


///HOLDER
    public class MemberIncludedHolder extends RecyclerView.ViewHolder {
        public RelativeLayout parent;
        public CircleImageView image_person;
        public TextView name_person;
        public CheckBox include_person;
        public ImageView euro_person;
        public EditText costo_person;
        private boolean customValue;



        public MemberIncludedHolder(View itemView) {

            super(itemView);
            parent = (RelativeLayout) itemView.findViewById(R.id.et_quotae);
            image_person = (CircleImageView) itemView.findViewById(R.id.iv_person_image);
            name_person= (TextView) itemView.findViewById(R.id.tv_name_member);
            include_person= (CheckBox) itemView.findViewById(R.id.cb_include);
            euro_person = (ImageView) itemView.findViewById(R.id.euro_member);
            costo_person = (EditText) itemView.findViewById(R.id.et_ins);
            customValue = false;
        }

        public void setData(Object c, Context context, final int position) {
            if (!(c instanceof UserDatabase)) return;
            final UserDatabase userDatabase = (UserDatabase) c;
            final User_expense us= users.get(position);
            name_person.setText(userDatabase.getName());
            image_person.setImageResource(R.drawable.group_profile);
            euro_person.setImageResource(R.drawable.euro);
            include_person.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!include_person.isChecked()) {
                        parent.setVisibility(View.INVISIBLE);
                        us.setSelected(false);
                        users.get(position).setCustomValue(0.0).setSelected(false);
                        us.setCustomValue(0.0);
                        us.getExpense().getMembers().remove(us.getExpense().getMembers().get(us.getId()));
                        }
                    else {
                        users.get(position).setCustomValue(0.0).setSelected(true);
                        parent.setVisibility(View.VISIBLE);
                        us.setSelected(true);
                    }
                    ExpenseDividerEvent event = (getCustoms() == null ? null : new ExpenseDividerEvent(getCustoms()));
                    EventBus.getDefault().post(new PartecipantsNumberChangedEvent(us, us.isSelected()  ? 1 : -1, position, event));
                }
            });
            if(!us.hasCustomValue()) {
                costo_person.setText(String.valueOf(costPerUser));
                us.getExpense().getMembers().put(us.getId(), costPerUser);
            }
            else {
                costo_person.setText(String.valueOf(us.getCustomValue()));
                us.getExpense().getMembers().put(us.getId(),us.getCustomValue());
            }
           costo_person.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {

                    if(!hasFocus) {
                        if(cost_procapite == 0.0) {
                            costo_person.setText(String.valueOf(0.0));
                            us.getExpense().getMembers().put(us.getId(),0.0);
                            us.setCustomValue(0.0);
                            users.get(position).setCustomValue(0.0);
                            EventBus.getDefault().post(new ExpenseDividerEvent(getCustoms()));
                        }
                        else {
                            if (cost_procapite == costPerUser) {
                            //    costo_person.setText(String.valueOf(cost_procapite));
                                return;
                            } else {
                            //    if (prezzocambiato == true) {
                                                us.setCustomValue(cost_procapite)
                                                  .getExpense().getMembers()
                                                        .put(us.getId(),us.getCustomValue());

                             //   } else {
                                 //   us.setCustomValue(costAnna);
                                  //  users.get(position).setCustomValue(costAnna);
                                }//costo_person.setText(String.valueOf(cost_procapite));

                                EventBus.getDefault().post(new ExpenseDividerEvent(getCustoms()));
                            }
                    }

                }
            });
            costo_person.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //   if(s.length() > 0) costAnna = Double.parseDouble(s.toString().replace(',', '.'));
                  //  DecimalFormat df = new DecimalFormat("0.00");
                  //  String formate = df.format(costAnna);
                  // costAnna = Double.parseDouble(formate.replace(',', '.'));
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length() > 0){
                        try{
                            double costPerUser =  Double.parseDouble(s.toString().replace(',', '.'));
                            DecimalFormat df = new DecimalFormat("0.00");
                            String formate = df.format(costPerUser);
                            cost_procapite = Double.parseDouble(formate.replace(',', '.'));
                            prezzocambiato = true;
                        }

                        catch (NumberFormatException e){
                            cost_procapite = 0.0;
                        }

                    } else {
                        cost_procapite = 0.0;
                        costo_person.setText(String.valueOf(0.0));
                        us.getExpense().getMembers().put(us.getId(),0.0);
                    }
                }
            });
        }
        public boolean isCustomValue() {
            return customValue;
        }

        public void setCustomValue(boolean customValue) {
            this.customValue = customValue;
        }
    }
//
    public List<User_expense> getList(){return users;}

    public MemberExpandedAdapter(List<User_expense> users, Context context){
        this.users= users;

        this.context= context;
        cost_procapite= 0.0;
        setHasStableIds(true);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPriceChanged(PriceChangedEvent event) {
        this.costPerUser = event.getPrice();

        notifyDataSetChanged();
    }

    @Subscribe
    public void onPriceError(PriceErrorEvent event) {
        for (User_expense us : users) {
            us.setCustomValue(0.0);
            us.getExpense().getMembers().put(us.getId(),0.0);
        }
    }

    private Pair<Integer, Double> getCustoms() {


        int i = 0;
        double v = 0;
        for (User_expense us : users) {
            //User_expense us = new User_expense(u);
            if (us.hasCustomValue()) {
                i++;
                v += us.getCustomValue();
            }
        }
        if(i == 0 || v == 0)
            return null;
        else
            return new Pair<>(i,v);

    }
    @Override
    public MemberIncludedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_expence_row, parent, false);
        return new MemberIncludedHolder((itemView));
    }

    @Override
    public void onBindViewHolder(MemberIncludedHolder holder, int position) {
        holder.setData(users.get(position),context, position);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}


