package it.polito.group05.group05.Utility.Holder;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.BaseClasses.Singleton;
import it.polito.group05.group05.Utility.BaseClasses.UserDatabase;

/**
 * Created by user on 03/05/2017.
 */

public class MemberIncludedHolder extends GeneralHolder {
    public RelativeLayout parent;
    public CircleImageView image_person;
    public TextView name_person;
    public CheckBox include_person;
    public ImageView euro_person;
    public EditText costo_person;
    private boolean customValue;
    private Double cost_procapite;
    //This map includes <IDPersona, Prezzo per la spesa>
    Map<String, Double> customPrices;
    //lista di uID degli utenti il cui prezzo è stato modificato custom e non deve essere toccato
    List<String> modified_prices;
    Double actualPrice;

    public MemberIncludedHolder(View itemView) {
        super(itemView);
        image_person = (CircleImageView) itemView.findViewById(R.id.iv_person_image);
        name_person= (TextView) itemView.findViewById(R.id.tv_name_member);
        include_person= (CheckBox) itemView.findViewById(R.id.cb_include);
        euro_person = (ImageView) itemView.findViewById(R.id.euro_member);
        costo_person = (EditText) itemView.findViewById(R.id.et_ins);

    }



    @Override
    public void setData(Object c, Context context) {
        if (!(c instanceof UserDatabase)) return;
        final UserDatabase userDatabase = (UserDatabase) c;
        name_person.setText(userDatabase.getName());
        image_person.setImageResource(R.drawable.group_profile);
        euro_person.setImageResource(R.drawable.euro);
        include_person.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        costo_person.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    try {
                        double costPerUser = Double.parseDouble(s.toString().replace(',', '.'));
                        DecimalFormat df = new DecimalFormat("0.00");
                        String formate = df.format(costPerUser);
                        Double cost_procapite = Double.parseDouble(formate.replace(',', '.'));
                    } catch (NumberFormatException e) {
                        cost_procapite = 0.0;
                    }
                    //
                } else {
                    cost_procapite = 0.0;
                    costo_person.setText(String.valueOf(0.0));
                }
                customPrices.put(userDatabase.getId(), cost_procapite);
                //Update all prices
                setAllPrices(customPrices, userDatabase.getId());
            }
        });
    }

    private void setAllPrices(Map<String, Double> customPrices, String id) {

        if (actualPrice != 0) {
            Double fixedValue = customPrices.get(id);
            if (fixedValue < actualPrice) {
                Double toDivide = actualPrice - fixedValue;
                modified_prices.add(id);
                for (String s : customPrices.keySet()) {
                    if (s != id) {
                        if (!modified_prices.contains(s)) {
                            int dividend = Singleton.getInstance().getmCurrentGroup().getMembers().size() - modified_prices.size();
                            if (dividend != 0) {
                                Double newPriceUser = toDivide / (double) dividend;
                                customPrices.remove(s);
                                customPrices.put(s, newPriceUser);
                                modified_prices.add(s);
                                actualPrice = upDateActualPrice(modified_prices, customPrices);

                            }
                        } else {
                            //ci sei solo tu nel gruppo
                        }
                    }
                }
            }

            //     Toast.makeText(MainActivity.this, "Insert a smaller value", Toast.LENGTH_SHORT).show();

        }
    }
    private Double upDateActualPrice(List<String> listID , Map<String, Double> mapIDPRICE ) {
        Double price = 0.0;
        Double tosubtract = 0.0;
        for(String s : mapIDPRICE.keySet()){
            for(String str : listID){
                if(s.equals(str)){
                    tosubtract += mapIDPRICE.get(s);
                }
            }
        }
        price = actualPrice - tosubtract;
        return price;
    }



}