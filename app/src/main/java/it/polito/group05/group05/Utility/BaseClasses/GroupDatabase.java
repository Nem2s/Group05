package it.polito.group05.group05.Utility.BaseClasses;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.SpannableString;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.nekocode.badge.BadgeDrawable;

/**
 * Created by Marco on 24/03/2017.
 */

public class Group {

    private String name;
    private String groupID;
    private Balance balance;
    private BadgeDrawable badge;
    private String lmTime;
    private Bitmap groupProfile;
    private GroupColor groupColor;
    private HashMap<String, UserDatabase> members;
    private HashMap<String, ExpenseDatabase> expenses;

    public Group(String groupName, Balance currentBalance, Bitmap groupProfile, String lmTime, int badgeCount) {
        this.name = groupName;
        this.balance = currentBalance;
        this.groupProfile = groupProfile;
        this.lmTime = lmTime;
        setBadge(badgeCount);
        this.members = new HashMap<>();
        this.expenses = new HashMap<>();
    }

    public Group(String groupName, String Id,  Balance currentBalance, String lmTime, int badgeCount) {
        this.name = groupName;
        this.groupID = Id;
        this.balance = currentBalance;
        this.groupProfile = groupProfile;
        this.lmTime = lmTime;
        setBadge(badgeCount);
        this.members = new HashMap<>();
        this.expenses = new HashMap<>();
    }

    public Group(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
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
                        .badgeColor(Color.parseColor("#FFC107"))
                        .textSize(30)
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

    public ExpenseDatabase getExpense(String id) {
        return expenses.get(id);
    }
    public Collection<ExpenseDatabase> getExpenses() {
        List l = new ArrayList(expenses.values());
         Collections.sort(l, new Comparator<ExpenseDatabase>() {
            @Override
            public int compare(ExpenseDatabase o1, ExpenseDatabase o2) {
                return o1.getTimestamp().compareTo(o2.getTimestamp());
            }
        });
        return l;
    }

    public void addExpense( ExpenseDatabase e){

            expenses.put(e.getId(), e);

    }

    public void setBadge(BadgeDrawable badge) {
        this.badge = badge;
    }

    public void setMembers(HashMap<String, UserDatabase> members) {
        this.members = members;
    }

    public void setExpenses(HashMap<String, ExpenseDatabase> expenses) {
        this.expenses = expenses;
    }

    public Map<String,Balance> debtUsers(){


        Map<String, Balance> f = new TreeMap<String,Balance>();
        for( ExpenseDatabase i : expenses.values())
            if(i.getType()!=TYPE_EXPENSE.NOTMANDATORY)
                for(UserDatabase u : i.getPartecipants())
                    if(u instanceof User_expense) {
                        if(((User_expense) u).getDebt()>0){
                            Double d =(f.get(u.getId()).getCredit()+((User_expense) u).getDebt());
                            f.get(u.getId()).setCredit(d);
                        }
                        else{
                            Double d =(f.get(u.getId()).getDebit()-((User_expense) u).getDebt());
                            f.get(u.getId()).setDebit(d);

                        }

                    }
        return f;
    }
    public UserDatabase getMember(String id){
        return members.get(id);
    }


    public List<UserDatabase> getMembers(){
        ArrayList<UserDatabase> new_list= new ArrayList<>(members.values());
        return new_list;

    }

    public List<String> getMembersId(){
        ArrayList<String> new_list= new ArrayList<>(members.keySet());
        return new_list;

    }

    public void addMember( UserDatabase u){
        members.put(u.getId(), u);
    }
    public void addMember(String id){
            members.put(id, null);
    }

}
