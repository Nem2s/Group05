package it.polito.group05.group05.Utility.BaseClasses;


import android.graphics.Color;
import android.text.SpannableString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nekocode.badge.BadgeDrawable;

import it.polito.group05.group05.Utility.Interfaces.Namable;

public class GroupDatabase implements Namable {

    public String id;
    public String name;
    public Balance balance = new Balance(0,0);
    public String lmTime;
    public GroupColor groupColor;
    public String pictureUrl;
    public Map<String, Object> members;
    //public Map<String, Object> expenses;


    public GroupDatabase() {
        members = new HashMap<>();
    }

    public GroupDatabase(String id, String name, Balance balance, String lmTime, GroupColor groupColor) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.groupColor = groupColor;

       // setBadge(badgeCount);
    }

    public GroupDatabase(GroupDatabase gd) {
        this.name = gd.getName();
        this.balance= gd.getBalance();
        this.id = gd.getId();
        this.lmTime= gd.getLmTime();
        this.groupColor= gd.getGroupColor();
        this.pictureUrl = gd.getPictureUrl();
        members = new HashMap<>();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nome) {
        this.name = nome;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
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

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Map<String, Object> getMembers() {
        return members;
    }

    public UserDatabase getUserOwner (String id){
        if(!members.containsKey(id) ) return null;
        if(!(members.get(id) instanceof UserDatabase)) return null;
            return (UserDatabase) members.get(id);

    }

    public void setMembers(Map<String, Object> members) {
        this.members = members;
    }

    public Map<String, Object> setMembers(List<UserDatabase> users, String id)
    {
        Map<String, Object> memb = new HashMap<String, Object>();
        for(UserDatabase u : users){
            memb.put(u.getName(), true);
        }

        memb.put(id, true);
        this.setMembers(memb);
        return memb;
    }

    public float getTotal() {
        return (float) (this.balance.getCredit() - this.balance.getDebit());
    }

}
