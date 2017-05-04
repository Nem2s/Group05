package it.polito.group05.group05.Utility.BaseClasses;

/**
 * Created by user on 04/05/2017.
 */

 public class Singleton {
    private static final Singleton ourInstance = new Singleton();
    String currentGroupId;
    static public Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }
    public String getIdCurrentGroup(){
        return currentGroupId;
    }
    public void setIdCurrentGroup(String currentGroupId){
        this.currentGroupId=currentGroupId;
    }
}
