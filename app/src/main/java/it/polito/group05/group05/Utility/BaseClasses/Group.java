package it.polito.group05.group05.Utility.BaseClasses;

import android.graphics.Color;
import android.text.SpannableString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private String groupProfile;
    private int groupColor;
    private Map<String, User> members;
    private Map<String, Expense> expenses ;

    public Group(String groupName, Balance currentBalance, String groupProfile, String lmTime, int badgeCount) {
        this.name = groupName;
        this.balance = currentBalance;
        this.groupProfile = groupProfile;
        this.lmTime = lmTime;
        setBadge(badgeCount);
        this.members = new TreeMap<>();
        this.expenses = new TreeMap<>();
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

    public Expense getExpense(String id) {
        return expenses.get(id);
    }
    public List<Expense> getExpenses() {
        return (List<Expense>)expenses.values();
    }

    public void addExpense( Expense e){

            expenses.put(e.getId(), e);

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
