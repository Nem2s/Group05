package it.polito.group05.group05.Utility.Adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.User_expense;
import it.polito.group05.group05.Utility.Holder.MemberIncludedHolder;

/**
 * Created by user on 05/05/2017.
 */

public class MemberExpandedAdapter extends RecyclerView.Adapter<MemberIncludedHolder> {
    private List<User_expense> users;

    private double total;
    Context context;


    public MemberExpandedAdapter(List<User_expense> users, Context context, double total) {
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
    public void onBindViewHolder(final MemberIncludedHolder holder, final int position) {
        final User_expense ue = users.get(position);
        final int pos = position;
        ue.setExcluded(false);
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
        holder.costo_person.setText(String.format("%.2f", ue.getCustomValue()));
        holder.costo_person.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    switch (v.getId()) {
                        case R.id.et_ins:
                            ue.setSelected(true);
                            holder.costo_person.setEnabled(true);
                            holder.costo_person.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    if (s.length() > 0) {
                                        double actualPrice = 0.0;
                                        actualPrice = Double.valueOf(s.toString().replace(',', '.'));
                                        double round = new BigDecimal(actualPrice).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                        if (actualPrice > total) {
                                            Toast.makeText(context, "Invalid Price", Toast.LENGTH_SHORT).show();
                                            holder.costo_person.setText("");
                                        } else {
                                            ue.setCustomValue(round);
                                            if (ue.isSelected()) {
                                                Double tmp = total;
                                                int count = 0;
                                                for (User_expense e : users) {
                                                    if (e.isSelected()) {
                                                        count++;
                                                        tmp -= e.getCustomValue();
                                                    }
                                                }
                                                for (int e = 0; e < users.size(); e++) {
                                                    if (!users.get(e).isSelected()) {
                                                        Double tmpD = Double.parseDouble(Integer.toString(users.size() - count));
                                                        if (tmpD < 0.9) return;
                                                        double round2 = new BigDecimal(tmp / tmpD)
                                                                .setScale(2, RoundingMode.HALF_UP)
                                                                .doubleValue();

                                                        if(round2 > 0){
                                                            users.get(e).setCustomValue(round2);
                                                            notifyItemChanged(e);
                                                        }
                                                        else {
                                                            Toast.makeText(context, "Invalid Price", Toast.LENGTH_SHORT).show();
                                                            holder.costo_person.setText("");
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            });
                            break;
                    }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void changeTotal(double total) {
        this.total = total;
        for (int j = 0; j < users.size(); j++) {
            User_expense e = users.get(j);
          //  double round3 = new BigDecimal(total / (users.size())).setScale(2, RoundingMode.HALF_UP).doubleValue();
            e.setCustomValue(total / (users.size()));
            e.setSelected(false);
            notifyItemChanged(j);
        }
     //   notifyDataSetChanged();
    }

}
