package it.polito.group05.group05.Utility.BaseClasses;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.SpannableString;

import com.pkmmte.view.CircularImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import cn.nekocode.badge.BadgeDrawable;

/**
 * Created by Marco on 24/03/2017.
 */

public class Group {

    private String name;
    private Balance balance;
    private BadgeDrawable badge;
    private String lmTime;
    private Bitmap groupProfile;
    private GroupColor groupColor;
    private TreeMap<String, User> members;
    private TreeMap<String, Expense> expenses;

    public Group(String groupName, Balance currentBalance, Bitmap groupProfile, String lmTime, int badgeCount) {
        this.name = groupName;
        this.balance = currentBalance;
        this.groupProfile = groupProfile;
        this.lmTime = lmTime;
        setBadge(badgeCount);
        this.members = new TreeMap<String, User>();
    }
    public Group(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public Bitmap getGroupProfile() {
        return groupProfile;
    }

    public void setGroupProfile(Bitmap image) {
        groupProfile = image;
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
                        .badgeColor(Color.parseColor("#01D1B1"))
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

    public GroupColor getGroupColor() {
        return groupColor;
    }

    public void setGroupColor(GroupColor groupColor) {
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
    public User getMember(String id){
        return members.get(id);
    }
    public List<User> getMembers(){
        ArrayList<User> new_list= new ArrayList<>(members.values());
        return new_list;
    }

    public void addMember( User u){
        if(!members.containsKey(u.getId())){
            members.put(u.getId(), u);
        }
    }

}
