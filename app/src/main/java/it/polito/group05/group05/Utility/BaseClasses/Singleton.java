package it.polito.group05.group05.Utility.BaseClasses;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.polito.group05.group05.R;
import it.polito.group05.group05.Utility.DB_Manager;
import it.polito.group05.group05.Utility.ImageUtils;

/**
 * Created by Marco on 28/03/2017.
 */

public class Singleton {
    private static Singleton mInstance = null;
    private List<GroupDatabase> mCurrentGroups;
    private List<UserDatabase> mCurrenUsersList;
    private GroupDatabase mCurrentGroup;
    private String userId;
    private Double price_expense;
    private UserDatabase currentUser;

    private Singleton(){
        mCurrentGroups = new ArrayList<>();
        mCurrentGroup = new GroupDatabase();
        mCurrenUsersList = new ArrayList<>();
        currentUser = new UserDatabase();
        price_expense= 0.0;
    }

    public static Singleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new Singleton();
        }
        return mInstance;
    }

    public void setmCurrentGroup(GroupDatabase g) {
    this.mCurrentGroup = g;}

    public GroupDatabase getmCurrentGroup() {
        return this.mCurrentGroup;
    }
    public void setmCurrentGroups(List<GroupDatabase> g) {
        this.mCurrentGroups = g;}
    public void addGroup(GroupDatabase g) {
        this.mCurrentGroups.add(g);
  }

  public void deleteGroup (GroupDatabase g) {
      this.mCurrentGroups.remove(g);
  }

    public List<GroupDatabase> getmCurrentGroups() {
        return this.mCurrentGroups;
    }
    public void setId(String s){this.userId=s;}
    public String getId() {
        return userId;
    }


    public void clearGroups(){
        this.mCurrentGroups.clear();
    }

    public List<UserContact> createRandomListUsers(int n, Context context, GroupDatabase g) {
        Random r = new Random();
        List<UserContact> users = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            UserContact u = new UserContact(""+i, "User " + i, new Balance(r.nextInt(n),r.nextInt(n)),
                    ImageUtils.getBitmpapFromDrawable(context.getResources().getDrawable( i%2 == 0 ? R.drawable.boy : R.drawable.girl)), g, true, false);
            users.add(u);
        }
        return users;
    }

    public int getPositionGroup(String id) {
        int i = 0;
        for (GroupDatabase g : this.mCurrentGroups) {
            if (g.getId().equals(id))
                return i;
            i++;
        }
        return -1;
    }

    public UserDatabase getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDatabase currentUser) {
        this.currentUser = currentUser;
        if(currentUser != null)
            userId=currentUser.getId();
    }

    public void setCurrentPrice( Double price ){
        this.price_expense= price;
    }
    public Double getCurrentPrice(){
        return price_expense;
    }

}