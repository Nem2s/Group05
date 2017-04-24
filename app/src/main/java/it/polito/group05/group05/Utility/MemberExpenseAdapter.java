package it.polito.group05.group05.Utility;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.EventClasses.ExpenseDividerEvent;
import it.polito.group05.group05.Utility.EventClasses.PartecipantsNumberChangedEvent;
import it.polito.group05.group05.Utility.EventClasses.PriceChangedEvent;
import it.polito.group05.group05.Utility.EventClasses.PriceErrorEvent;

/**
 * Created by user on 04/04/2017.
 */

public class MemberExpenseAdapter extends RecyclerView.Adapter<MemberExpenseAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void OnItemClicked(User position);
    }

    private List<User> member;
    private Double cost_procapite;
    LayoutInflater lin;
    private double costPerUser;
    private Context context;
    Context c;

    public MemberExpenseAdapter(Context c, List<User> member, Context context) {
        lin = LayoutInflater.from(c);
        this.member = member;
        this.c = c;
        this.context = context;
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
        for (User u :
                member) {
            u.setCustomValue(0);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_expence_row, parent, false);
        ViewHolder holder = new ViewHolder(rootView);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final User  currentUser = member.get(position);
        holder.image_person.setImageBitmap(currentUser.getProfile_image());
        holder.name_person.setText(currentUser.getUser_name());
        holder.include_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(!holder.include_person.isChecked()) {
                holder.parent.setVisibility(View.INVISIBLE);
                currentUser.setSelected(false);
                currentUser.setCustomValue(0);
            }
            else {
                holder.parent.setVisibility(View.VISIBLE);
                currentUser.setSelected(true);
            }
                ExpenseDividerEvent event = (getCustoms() == null ? null : new ExpenseDividerEvent(getCustoms()));
                EventBus.getDefault().post(new PartecipantsNumberChangedEvent(currentUser,
                        currentUser.isSelected()  ? 1 : -1, position, event));
            }
        });
        if(!currentUser.hasCustomValue())
            holder.costo_person.setText(String.valueOf(costPerUser));
        holder.costo_person.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if(!hasFocus) {
                    if(cost_procapite == 0.0) {
                        holder.costo_person.setText(String.valueOf(0.0));
                        currentUser.setCustomValue(0);
                        EventBus.getDefault().post(new ExpenseDividerEvent(getCustoms()));
                    }
                    else if(cost_procapite == costPerUser)
                        return;
                    else {
                        currentUser.setCustomValue(cost_procapite);
                        EventBus.getDefault().post(new ExpenseDividerEvent(getCustoms()));
                    }
                }
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
                if(s.length() > 0){
                  try{
                      double costPerUser =  Double.parseDouble(s.toString().replace(',', '.'));
                      DecimalFormat df = new DecimalFormat("0.00");
                      String formate = df.format(costPerUser);
                      cost_procapite = Double.parseDouble(formate.replace(',', '.'));
                    }
                    catch (NumberFormatException e){
                        cost_procapite = 0.0;
                    }

                        //
                } else {
                    cost_procapite = 0.0;
                    holder.costo_person.setText(String.valueOf(0.0));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return member.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Pair<Integer, Double> getCustoms() {
        int i = 0;
        double v = 0;
        for (User u :
                member) {
            if (u.hasCustomValue()) {
                i++; v += u.getCustomValue();
            }
        }
        if(i == 0 || v == 0)
            return null;
        else
            return new Pair<>(i,v);

    }

    //VIEWHOLDER CLASS
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout parent;
        public CircularImageView image_person;
        public TextView name_person;
        public CheckBox include_person;
        public ImageView euro_person;
        public EditText costo_person;
        private boolean customValue;


        public ViewHolder(View itemView) {
            super(itemView);
            this.parent = (RelativeLayout)itemView.findViewById(R.id.et_quotae);
            this.image_person = (CircularImageView) itemView.findViewById(R.id.iv_group_image);
            this.name_person = (TextView) itemView.findViewById(R.id.tv_name_member);
            this.include_person = (CheckBox) itemView.findViewById(R.id.cb_include);
            this.euro_person = (ImageView) itemView.findViewById(R.id.euro_member);
            this.costo_person = (EditText) itemView.findViewById(R.id.et_ins);
            this.customValue = false;
        }

        public boolean isCustomValue() {
            return customValue;
        }

        public void setCustomValue(boolean customValue) {
            this.customValue = customValue;
        }
    }
}