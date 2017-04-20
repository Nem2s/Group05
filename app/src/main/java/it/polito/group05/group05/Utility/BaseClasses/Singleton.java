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

    private List<Group> mCurrentGroups;
    private List<User> mCurrenUsersList;
    private Group mCurrentGroup;
    private String user;

    private Singleton(){
        mCurrentGroups = new ArrayList<>();
        mCurrentGroup = new Group();
        mCurrenUsersList = new ArrayList<>();
    }

    public static Singleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new Singleton();
        }
        return mInstance;
    }

    public void setmCurrentGroup(Group g) {
    this.mCurrentGroup = g;}

    public Group getmCurrentGroup() {
        return this.mCurrentGroup;
    }
    public void setmCurrentGroups(List<Group> g) {
        this.mCurrentGroups = g;}
    public void addGroup(Group g) {
        this.mCurrentGroups.add(g);
  }

  public void deleteGroup (Group g) {
      this.mCurrentGroups.remove(g);
  }

    public List<Group> getmCurrentGroups() {
        return this.mCurrentGroups;
    }
    public void setId(String s){this.user=new String(s);}
    public String getId() {
        return user;
    }

    public void clearGroups(){
        this.mCurrentGroups.clear();
    }

    public List<UserContact> createRandomListUsers(int n, Context context, Group g) {
        Random r = new Random();
        List<UserContact> users = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            UserContact u = new UserContact(""+i, "User " + i, new Balance(r.nextInt(n),r.nextInt(n)),
                    ImageUtils.getBitmpaFromDrawable(context.getResources().getDrawable( i%2 == 0 ? R.drawable.boy : R.drawable.girl)), g, true, false);
            users.add(u);
        }
        return users;
    }
}