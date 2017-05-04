package it.polito.group05.group05.Utility.BaseClasses;

/**
 * Created by user on 04/05/2017.
 */

 public class Singleton {
    private static final Singleton ourInstance = new Singleton();
    private GroupDatabase mCurrentGroup;
    private UserDatabase currentUser;
    String currentGroupId;
    private String userId;


    private Singleton() {
    }

    public GroupDatabase getmCurrentGroup() {
        return mCurrentGroup;
    }

    public void setmCurrentGroup(GroupDatabase mCurrentGroup) {
        this.mCurrentGroup = mCurrentGroup;
    }

    public UserDatabase getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDatabase currentUser) {
        this.currentUser = currentUser;
    }

    private Double price_expense;



    public Double getPrice_expense() {
        return price_expense;
    }

    public void setPrice_expense(Double price_expense) {
        this.price_expense = price_expense;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    static public Singleton getInstance() {
        return ourInstance;
    }


    public String getIdCurrentGroup(){
        return currentGroupId;
    }
    public void setIdCurrentGroup(String currentGroupId){
        this.currentGroupId=currentGroupId;
    }
}
