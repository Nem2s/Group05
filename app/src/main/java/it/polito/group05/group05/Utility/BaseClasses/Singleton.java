package it.polito.group05.group05.Utility.BaseClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marco on 28/03/2017.
 */

public class Singleton {
    private static Singleton mInstance = null;

    private List<Group> mCurrentGroups;
    private Group mCurrentGroup;
    private String user;

    private Singleton(){
        mCurrentGroups = new ArrayList<>();
        mCurrentGroup = new Group();
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
    public List<Group> getmCurrentGroups() {
        return this.mCurrentGroups;
    }
    public void setId(String s){this.user=new String(s);}
    public String getId() {
        return user;
    }
}