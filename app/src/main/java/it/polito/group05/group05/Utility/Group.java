package it.polito.group05.group05.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.text.SpannableString;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import cn.nekocode.badge.BadgeDrawable;
import it.polito.group05.group05.R;

/**
 * Created by Marco on 24/03/2017.
 */

public class Group {

    private String name;
    private String balance;
    private BadgeDrawable badge;
    private String lmTime;
    private String groupProfile;
    private int groupColor;
    private TreeMap<String, User> members;
    private TreeMap<String, Expense> expenses;

    public Group(String groupName, String currentBalance, String groupProfile, String lmTime, BadgeDrawable badge) {
        this.name = groupName;
        this.balance = currentBalance;
        this.groupProfile = groupProfile;
        this.lmTime = lmTime;
        this.badge = badge;
    }
    public Group(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getGroupProfile() {
        return groupProfile;
    }

    public void setGroupProfile(String Uri) {
        groupProfile = Uri;
    }

    public SpannableString getBadge() {
        if(badge != null)
            return badge.toSpannable();
        return null;
    }

    public void setBadge(int count) {
        if(count == 0)
            return;
             badge = new BadgeDrawable.Builder()
                        .type(BadgeDrawable.TYPE_NUMBER)
                        .badgeColor(Color.parseColor("#FFC107"))
                        .textSize(40)
                        .number(count)
                        .build();

    }

    public String getLmTime() {
        return lmTime;
    }

    public void setLmTime(String lmTime) {
        this.lmTime = lmTime;
    }

    public int getGroupColor() {
        return groupColor;
    }

    public void setGroupColor(int groupColor) {
        this.groupColor = groupColor;
    }

    public Expense getExpenses(String id) {
        return expenses.get(id);
    }
    public void addExpense( Expense e){
        if(!expenses.containsKey(e.getId())){
            expenses.put(e.getId(), e);
        }
    }
    public User getUsers(String id){
        return members.get(id);
    }

    public void addUser( User u){
        if(!members.containsKey(u.getId())){
            members.put(u.getId(), u);
        }
    }

}
