package it.polito.group05.group05.Utility.BaseClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by antonino on 04/04/2017.
 */

public class User_expense extends UserDatabase {
    GroupDatabase group;
    Expense expense;
    Double debt=0.0;
    TYPE_EXPENSE typeExpense;
    boolean isSelected;
    private double customValue;

    public User_expense(UserDatabase userDatabase){
        super();
        this.name = userDatabase.getName();
        this.id = userDatabase.getId();
        this.authKey = userDatabase.getAuthKey();
        this.telNumber = userDatabase.getTelNumber();
        this.email = userDatabase.getEmail();
        this.iProfile = userDatabase.getiProfile();
        this.balance = userDatabase.getBalance();
        this.customValue = 0.0;
        isSelected = false;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public void setCustomValue(Double customValue) {
        this.customValue = customValue;
    }

    public static List<UserDatabase> createListUserExpense(GroupDatabase g,Expense e){
        List<UserDatabase> user_expenses = new ArrayList<>();
        Map<String,Object> u = g.getMembers();
        for(Object i1 : u.values()){
            if(!(i1 instanceof UserDatabase)) continue;
            UserDatabase i = (UserDatabase)i1;
            User_expense x = new User_expense(g,e,e.getType(),i.getId());
            x.setProfileImage(i.getProfileImage());
            //   User_expense x = new User_expense();
            x.setId(i.getId());
            x.setName(i.getName());
            user_expenses.add(x);
        }
        return user_expenses;
    }
    public static void createListUserExpense(List<UserDatabase> users,Expense e){
        List<User_expense> user_expenses = new ArrayList<>();
    }

    public TYPE_EXPENSE getTypeExpense() {
        return typeExpense;
    }

    public void setTypeExpense(TYPE_EXPENSE typeExpense) {
        this.typeExpense = typeExpense;
    }



    public User_expense(GroupDatabase p,Expense s,TYPE_EXPENSE t, String user){
        this.group=p;
        this.expense=s;
        this.typeExpense=t;
        int i = user.compareTo(s.getOwner());
        if(t==TYPE_EXPENSE.MANDATORY && i!=0) {
            debt -= s.getPrice();
            debt/=(float)p.getMembers().size();
        }
        else if(t==TYPE_EXPENSE.MANDATORY && i==0) debt = s.getPrice()-(s.getPrice()/(float)p.getMembers().size());
        else if (t==TYPE_EXPENSE.NOTMANDATORY) debt = 0.0;
    }

    public GroupDatabase getGroup() {
        return group;
    }

    public void setGroup(GroupDatabase p) {
        this.group = p;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense s) {
        this.expense = s;
    }

    public Double getDebt() {
        return debt;
    }

    public void setDebt(Double debt) {
        this.debt = debt;
    }

    public TYPE_EXPENSE getTe() {
        return typeExpense;
    }

    public void setTe(TYPE_EXPENSE te) {
        this.typeExpense = te;
    }
}