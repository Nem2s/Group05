package it.polito.group05.group05.Utility.BaseClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antonino on 26/04/2017.
 */

public class UserGroup extends User {
    boolean isAdministrator=false,isCardEnabled=false;
    Balance balance;
    float tot_expenses;
    double customValue;
    int user_color;

public static List<UserGroup> listUserGroup(List<User> userList ){
    List<UserGroup> userGroupList = new ArrayList<>();

    for(User u : userList)
        userGroupList.add(new UserGroup(u));
return userGroupList;

}
    public UserGroup (){}
    public UserGroup (User u){
        super.setContacts(u.getContacts());
        super.setId(u.getId());
        super.setProfile_image(u.getProfile_image());
        super.setUser_name(u.getUser_name());
        super.setUser_group(u.getUser_group());
        super.setSelected(false);

    }
    public boolean hasCustomValue() {
        return customValue > 0;
    }
    public double getCustomValue() {
        return customValue;
    }

    public void setCustomValue(double customValue) {
        this.customValue = customValue;
    }
    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }
    public boolean isAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(boolean administrator) {
        isAdministrator = administrator;
    }

    public boolean isCardEnabled() {
        return isCardEnabled;
    }

    public void setCardEnabled(boolean cardEnabled) {
        isCardEnabled = cardEnabled;
    }

    public double getCurrentBalance(){
        return balance.getCredit() - balance.getDebit();
    }

    public int generateRandomColor() {

        // This is the base color which will be mixed with the generated one
        return 3;

    }

    public float getTot_expenses() {
        return tot_expenses;
    }

    public void setTot_expenses(float tot_expenses) {
        this.tot_expenses = tot_expenses;
    }

    public int getUser_color() {
        return user_color;
    }

}
