package com.mad2017.group05;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.mad2017.group05.GeneralAdapter.c;

/**
 * Created by antonino on 02/05/2017.
 */

public class GeneralAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    List<Object> list;

    int type;

    LayoutInflater li;
    static public Context c;
    int type;
    //added view types
    private static final int GROUP_VIEW= R.layout.group_item_sample;
    private static final int EXPENSE_VIEW= R.layout.item_expense;
    private static final int USER_VIEW= R.layout.member_item_sample;
    private static final int USER_VIEW_EXPENSE= R.layout.expense_card_expansion;


    public GeneralAdapter(Context c, List<Object> lo, int type){
        this.c = c ;
        li = LayoutInflater.from(c);
        list = lo;
        this.type = type;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = li.inflate(type,parent,false);
        RecyclerView.ViewHolder v = null;
        switch(type){
            case GROUP_VIEW:
                v = GroupHolder.newInstance(rootView);
                break;
            case EXPENSE_VIEW:
                v =  ExpenseHolder.newInstance(rootView);
                break;
            case USER_VIEW:
                v = MemberHolder.newInstance(rootView);
                break;
            case USER_VIEW_EXPENSE:
                v = ExpenseCardHolder.newInstance(rootView);
                break;
            default:
        }
        return v;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((GeneralViewHolder )holder).setData(list.get(position -1), c);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

abstract class GeneralViewHolder extends RecyclerView.ViewHolder {
    public GeneralViewHolder(View itemView) {
        super(itemView);
    }
    abstract public void setData(Object c, Context context);
}
class ExpenseHolder extends GeneralViewHolder{
    ImageView expense_image;
    TextView name;
    TextView price;
    RecyclerView rv;
    TextView description;
    CardView cv;
    private ExpenseHolder(View itemView, ImageView expense_image, TextView name, TextView price, TextView description, CardView cv, RecyclerView rv) {
        super(itemView);
        this.expense_image = expense_image;
        this.name = name;
        this.price = price;
        this.description = description;
        this.cv = cv;
        this.rv = rv;
    }
    public static ExpenseHolder newInstance(View itemView) {
        ImageView expense_image = (ImageView) itemView.findViewById(R.id.expense_image);
        TextView name= (TextView) itemView.findViewById(R.id.expense_name);
        TextView price= (TextView) itemView.findViewById(R.id.expense_price);
        TextView description=(TextView) itemView.findViewById(R.id.expense_owner);
        CardView cv = (CardView) itemView.findViewById(R.id.card_expense);
        RecyclerView rv = (RecyclerView) itemView.findViewById(R.id.expense_rv);
        return new ExpenseHolder(itemView, expense_image, name, price, description, cv, rv);

    }

    public void setData(Object c, Context context){
        if(!(c instanceof Expense)) return;
    }
}

class MemberHolder extends GeneralViewHolder{
    CircleImageView img_profile;
    TextView user_name;
    TextView balance;
    TextView administrator;
    ImageView payByCard;
    FloatingActionButton color;



    public static MemberHolder newInstance(View v) {
        CircleImageView img_profile = (CircleImageView) itemView.findViewById(R.id.iv_user_image);
        TextView balance = (TextView)itemView.findViewById(R.id.tv_user_balance);
        TextView user_name = (TextView)itemView.findViewById(R.id.tv_user_name);
        ImageView payByCard = (ImageView)itemView.findViewById(R.id.iv_paycard);
        TextView administrator = (TextView)itemView.findViewById(R.id.tv_admin);

        return new MemberHolder(v,img_profile,balance,user_name,payByCard,administrator);
    }

    private  MemberHolder( View v,CircleImageView img_profile,TextView user_name,TextView balance,TextView administrator,ImageView payByCard,FloatingActionButton color) {
        super(v);
         this.img_profile = img_profile;
         this.user_name=user_name;
         this.balance=balance;
         this.administrator= administrator;
         this.payByCard=payByCard;
         this.color=color;
    }

    public void setData(Object c, Context context){
        if(!(c instanceof UserDatabase)) return;
    }


}

class GroupHolder extends GeneralViewHolder{
    CircleImageView groupProfile;
    TextView name;
    TextView balance;
    TextView badge;
    TextClock time;
    private GroupHolder(View view, CircleImageView groupProfile,TextView name,TextView balance,TextView badge,TextClock time){
        super(view);
        this.groupProfile=groupProfile;
        this.name=name;
        this.balance=balance;
        this.badge=badge;
        this.time=time;

    }

    public static GroupHolder newInstance(View v) {

        CircleImageView groupProfile = (CircleImageView)itemView.findViewById(R.id.iv_group_image);
        TextView balance = (TextView)itemView.findViewById(R.id.tv_group_balance);
        TextView name = (TextView)itemView.findViewById(R.id.tv_group_name);
        TextView badge = (TextView)itemView.findViewById(R.id.tv_badge_counter);
        TextClock time = (TextClock)itemView.findViewById(R.id.tc_last_message);
        return new GroupHolder(v,groupProfile,name,balance,badge,time);
    }
    public void setData(Object c, Context context){
        if(!(c instanceof Group)) return;
    }
}
class ExpenseCardHolder extends GeneralViewHolder{

    CircleImageView civ;
    TextView tv,tv_debt;
    private ExpenseCardHolder(View itemView,CircleImageView civ,TextView tv,TextView tv_debt) {
        super(itemView);
        this.civ = civ;
        this.tv = tv;
        this.tv_debt =tv_debt;
    }

    public static ExpenseCardHolder newInstance(View v) {

        CircleImageView civ = (CircleImageView) itemView.findViewById(R.id.expense_member_image);
        TextView tv = (TextView) itemView.findViewById(R.id.expense_member_name);
        TextView tv_debt = (TextView) itemView.findViewById(R.id.expense_member_debt);
        return fragment;
    }
    public void setData(Object u,Context c){}
}